package iskallia.vault.world.raid;

import iskallia.vault.entity.FighterEntity;
import iskallia.vault.init.ModEntities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class ArenaSpawner implements INBTSerializable<CompoundNBT> {

	private final ArenaRaid raid;
	public final List<UUID> fighters = new ArrayList<>();
	public final List<UUID> bosses = new ArrayList<>();
	private final int bossCount;

	private List<String> fighterNames = new ArrayList<>();
	private boolean started;

	public ArenaSpawner(ArenaRaid raid, int bossCount) {
		this.raid = raid;
		this.bossCount = bossCount;
	}

	public boolean hasStarted() {
		return this.started;
	}

	public void addFighters(Collection<String> fighterNames) {
		this.fighterNames = new ArrayList<>(fighterNames);
	}

	public void start(ServerWorld world) {
		this.fighters.clear();
		this.bosses.clear();

		for(int i = 0; i < this.bossCount; i++) {
			FighterEntity boss = ModEntities.ARENA_BOSS.create(world).changeSize(3.0F);
			BlockPos pos = this.toTop(world, this.raid.getCenter());
			boss.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.2F, pos.getZ() + 0.5F, 0.0F, 0.0F);
			this.bosses.add(boss.getUniqueID());
			world.summonEntity(boss);
		}

		for(int i = 0; i < this.fighterNames.size(); i++) {
			double radius = 40.0D;
			double a = ((double)i / this.fighterNames.size()) * 2.0D * Math.PI;
			double s = Math.sin(a), c = Math.cos(a);
			double r = Math.sqrt((radius * radius) / (s * s + c * c));
			BlockPos pos = this.toTop(world, this.raid.getCenter().add(s * r, 0, c * r));

			FighterEntity fighter = ModEntities.ARENA_FIGHTER.create(world).changeSize(1.2F);
			fighter.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.2F, pos.getZ() + 0.5F, 0.0F, 0.0F);
			fighter.setCustomName(new StringTextComponent(this.fighterNames.get(0)));
			this.fighters.add(fighter.getUniqueID());
			world.summonEntity(fighter);
		}

		this.started = true;
	}

	public int getFighterCount() {
		return this.fighters.size();
	}

	public BlockPos toTop(ServerWorld world, BlockPos pos) {
		world.getChunk(pos); //Force chunk loading.
		return world.getHeight(Heightmap.Type.MOTION_BLOCKING, pos);
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		ListNBT bossList = new ListNBT();
		ListNBT fighterList = new ListNBT();
		ListNBT fighterNamesList = new ListNBT();

		this.bosses.forEach(uuid -> bossList.add(StringNBT.valueOf(uuid.toString())));
		this.fighters.forEach(uuid -> fighterList.add(StringNBT.valueOf(uuid.toString())));
		this.fighterNames.forEach(name -> fighterNamesList.add(StringNBT.valueOf(name)));

		nbt.put("BossList", bossList);
		nbt.put("FighterList", fighterList);
		nbt.put("FighterNamesList", fighterNamesList);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.bosses.clear();
		this.fighters.clear();

		ListNBT bossList = nbt.getList("BossList", Constants.NBT.TAG_STRING);
		ListNBT fighterList = nbt.getList("FighterList", Constants.NBT.TAG_STRING);
		ListNBT fighterNamesList = nbt.getList("FighterNamesList", Constants.NBT.TAG_STRING);

		IntStream.range(0, bossList.size()).mapToObj(i -> UUID.fromString(bossList.getString(i))).forEach(this.bosses::add);
		IntStream.range(0, fighterList.size()).mapToObj(i -> UUID.fromString(fighterList.getString(i))).forEach(this.fighters::add);
		IntStream.range(0, fighterNamesList.size()).forEach(i -> this.fighterNames.add(fighterNamesList.getString(i)));
	}

}
