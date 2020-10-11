package iskallia.vault.world.data;

import iskallia.vault.Vault;
import iskallia.vault.init.ModFeatures;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VaultRaidData extends WorldSavedData {

	protected static final String DATA_NAME = Vault.MOD_ID + "_VaultRaid";

	private Map<UUID, VaultRaid> activeRaids = new HashMap<>();
	private int xOffset = 0;

	public VaultRaidData() {
		this(DATA_NAME);
	}

	public VaultRaidData(String name) {
		super(name);
	}

	public VaultRaid getActiveFor(ServerPlayerEntity player) {
		return this.activeRaids.get(player.getUniqueID());
	}

	public VaultRaid startNew(ServerPlayerEntity player) {
		VaultRaid raid = new VaultRaid(player.getUniqueID(), new MutableBoundingBox(
				this.xOffset, 0, 0, this.xOffset += 2000, 256, 2000
		));

		ServerWorld world = player.getServer().getWorld(Vault.WORLD_KEY);
		player.teleport(world, raid.box.minX, 256, raid.box.minZ, player.rotationYaw, player.rotationPitch);

		player.getServer().runAsync(() -> {
			ChunkPos chunkPos = new ChunkPos(raid.box.minX >> 4, raid.box.minZ >> 4);
			IChunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
			StructureStart<?> start = world.func_241112_a_().func_235013_a_(SectionPos.from(chunk.getPos(), 0), ModFeatures.VAULT_FEATURE.field_236268_b_, chunk);
			int i = start != null ? start.getRefCount() : 0;

			StructureSeparationSettings settings = new StructureSeparationSettings(1, 0, -1);

			start = ModFeatures.VAULT_FEATURE.func_242771_a(world.func_241828_r(),
					world.getChunkProvider().generator, world.getChunkProvider().generator.getBiomeProvider(),
					world.getStructureTemplateManager(), world.getSeed(), chunkPos,
					BiomeRegistry.PLAINS, i, settings);

			start.func_230366_a_(world, world.func_241112_a_(), world.getChunkProvider().generator, new Random(),
					MutableBoundingBox.func_236990_b_(), chunkPos);
		});

		if(this.activeRaids.containsKey(player.getUniqueID())) {
			this.activeRaids.get(player.getUniqueID()).ticksLeft = 0;
		}

		this.activeRaids.put(raid.getPlayerId(), raid);
		this.markDirty();
		return raid;
	}

	public void tick(ServerWorld world) {
		this.activeRaids.values().forEach(VaultRaid::tick);

		if(this.activeRaids.values().removeIf(VaultRaid::isComplete)) {
			this.markDirty();
		}
	}

	@SubscribeEvent
	public static void onTick(TickEvent.WorldTickEvent event) {
		if(event.side == LogicalSide.SERVER) {
			get((ServerWorld)event.world).tick((ServerWorld)event.world);
		}
	}

	@Override
	public void read(CompoundNBT nbt) {
		this.activeRaids.clear();

		nbt.getList("ActiveRaids", Constants.NBT.TAG_COMPOUND).forEach(raidNBT -> {
			VaultRaid raid = VaultRaid.fromNBT((CompoundNBT)raidNBT);
			this.activeRaids.put(raid.getPlayerId(), raid);
		});

		this.xOffset = nbt.getInt("XOffset");
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		ListNBT raidsList = new ListNBT();
		this.activeRaids.values().forEach(raid -> raidsList.add(raid.serializeNBT()));
		nbt.put("ActiveRaids", raidsList);
		nbt.putInt("XOffset", this.xOffset);
		return nbt;
	}

	public static VaultRaidData get(ServerWorld world) {
		return world.getServer().func_241755_D_().getSavedData().getOrCreate(VaultRaidData::new, DATA_NAME);
	}

}
