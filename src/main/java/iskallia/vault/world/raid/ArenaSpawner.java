package iskallia.vault.world.raid;

import iskallia.vault.entity.EntityScaler;
import iskallia.vault.entity.FighterEntity;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModEntities;
import iskallia.vault.world.data.StreamData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class ArenaSpawner implements INBTSerializable<CompoundNBT> {

	private final ArenaRaid raid;
	public final List<UUID> fighters = new ArrayList<>();
	public final List<UUID> bosses = new ArrayList<>();
	private final int bossCount;

	private List<StreamData.Subscribers.Instance> subscribers = new ArrayList<>();
	private boolean started;

	public ArenaSpawner(ArenaRaid raid, int bossCount) {
		this.raid = raid;
		this.bossCount = bossCount;
	}

	public boolean hasStarted() {
		return this.started;
	}

	public void addFighters(List<StreamData.Subscribers.Instance> fighterNames) {
		this.subscribers = new ArrayList<>(fighterNames);
	}

	public void start(ServerWorld world) {
		this.fighters.clear();
		this.bosses.clear();

		int maxMonths = 0;

		for(int i = 0; i < this.subscribers.size(); i++) {
			double radius = 40.0D;
			double a = ((double)i / this.subscribers.size()) * 2.0D * Math.PI;
			double s = Math.sin(a), c = Math.cos(a);
			double r = Math.sqrt((radius * radius) / (s * s + c * c));

			BlockPos pos = this.raid.getCenter().add(s * r, 256 - this.raid.getCenter().getY(), c * r);

			FighterEntity fighter = ModEntities.ARENA_FIGHTER.create(world).changeSize(1.2F);
			fighter.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.2F, pos.getZ() + 0.5F, 0.0F, 0.0F);
			fighter.setCustomName(new StringTextComponent(this.subscribers.get(i).getName()));
			this.fighters.add(fighter.getUniqueID());

			fighter.enablePersistence();
			world.addEntity(fighter);

			EntityScaler.scaleArena(fighter, this.subscribers.get(i).getMonths(), world.getRandom());
			maxMonths = Math.max(maxMonths, this.subscribers.get(i).getMonths());
		}

		for(int i = 0; i < this.bossCount; i++) {
			FighterEntity boss = ModEntities.ARENA_BOSS.create(world).changeSize(1.2F);
			BlockPos pos = this.toTop(world, new BlockPos(this.raid.getCenter().getX(), 0, this.raid.getCenter().getZ()));
			boss.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.2F, pos.getZ() + 0.5F, 0.0F, 0.0F);
			this.bosses.add(boss.getUniqueID());

			boss.enablePersistence();
			world.addEntity(boss);

			EntityScaler.scaleArena(boss, maxMonths, world.getRandom());
		}

		this.started = true;
	}

	public int getFighterCount() {
		return this.fighters.size();
	}

	public BlockPos toTop(ServerWorld world, BlockPos pos) {
		return pos.up(world.getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.FULL, true)
				.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, pos.getX(), pos.getZ()) + 1);
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		ListNBT bossList = new ListNBT();
		ListNBT fighterList = new ListNBT();
		ListNBT subscriberList = new ListNBT();

		this.bosses.forEach(uuid -> bossList.add(StringNBT.valueOf(uuid.toString())));
		this.fighters.forEach(uuid -> fighterList.add(StringNBT.valueOf(uuid.toString())));
		this.subscribers.forEach(sub -> subscriberList.add(sub.serializeNBT()));

		nbt.put("BossList", bossList);
		nbt.put("FighterList", fighterList);
		nbt.put("SubscriberList", subscriberList);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.bosses.clear();
		this.fighters.clear();

		ListNBT bossList = nbt.getList("BossList", Constants.NBT.TAG_STRING);
		ListNBT fighterList = nbt.getList("FighterList", Constants.NBT.TAG_STRING);
		ListNBT subscriberList = nbt.getList("SubscriberList", Constants.NBT.TAG_STRING);

		IntStream.range(0, bossList.size()).mapToObj(i -> UUID.fromString(bossList.getString(i))).forEach(this.bosses::add);
		IntStream.range(0, fighterList.size()).mapToObj(i -> UUID.fromString(fighterList.getString(i))).forEach(this.fighters::add);
		IntStream.range(0, subscriberList.size()).forEach(i -> {
			StreamData.Subscribers.Instance sub = new StreamData.Subscribers.Instance();
			sub.deserializeNBT(subscriberList.getCompound(i));
			this.subscribers.add(sub);
		});
	}

}
