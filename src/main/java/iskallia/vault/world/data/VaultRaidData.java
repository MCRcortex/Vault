package iskallia.vault.world.data;

import iskallia.vault.Vault;
import iskallia.vault.init.ModFeatures;
import iskallia.vault.init.ModStructures;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VaultRaidData extends WorldSavedData {

	protected static final String DATA_NAME = Vault.MOD_ID + "_VaultRaid";

	private Map<UUID, VaultRaid> activeRaids = new HashMap<>();
	private Map<UUID, Integer> playerLevels = new HashMap<>();
	private int xOffset = 0;

	public VaultRaidData() {
		this(DATA_NAME);
	}

	public VaultRaidData(String name) {
		super(name);
	}

	public VaultRaid getAt(BlockPos pos) {
		return this.activeRaids.values().stream().filter(raid -> raid.box.isVecInside(pos)).findFirst().orElse(null);
	}

	public VaultRaid getActiveFor(ServerPlayerEntity player) {
		return this.activeRaids.get(player.getUniqueID());
	}

	public int getLevel(ServerPlayerEntity player) {
		return this.playerLevels.computeIfAbsent(player.getUniqueID(), uuid -> 0);
	}

	public VaultRaid startNew(ServerPlayerEntity player) {
		VaultRaid raid = new VaultRaid(player.getUniqueID(), new MutableBoundingBox(
				this.xOffset, 0, 0, this.xOffset += VaultRaid.REGION_SIZE, 256, VaultRaid.REGION_SIZE
		), PlayerAbilitiesData.get(player.getServerWorld()).getAbilities(player).getVaultLevel());

		if(this.activeRaids.containsKey(player.getUniqueID())) {
			this.activeRaids.get(player.getUniqueID()).ticksLeft = 0;
		}

		this.activeRaids.put(raid.getPlayerId(), raid);
		this.markDirty();

		player.getServer().runAsync(() -> {
			try {
				ServerWorld world = player.getServer().getWorld(Vault.WORLD_KEY);
				ChunkPos chunkPos = new ChunkPos((raid.box.minX + raid.box.getXSize() / 2) >> 4, (raid.box.minZ + raid.box.getZSize() / 2) >> 4);

				StructureSeparationSettings settings = new StructureSeparationSettings(1, 0, -1);

				StructureStart<?> start = ModFeatures.VAULT_FEATURE.func_242771_a(world.func_241828_r(),
						world.getChunkProvider().generator, world.getChunkProvider().generator.getBiomeProvider(),
						world.getStructureTemplateManager(), world.getSeed(), chunkPos,
						BiomeRegistry.PLAINS, 0, settings);


				//This is some cursed calculations, don't ask me how it works.
				int chunkRadius  = VaultRaid.REGION_SIZE >> 5;

				for(int x = -chunkRadius; x <= chunkRadius; x += 17) {
					for(int z = -chunkRadius; z <= chunkRadius; z += 17) {
						world.getChunk(chunkPos.x + x, chunkPos.z + z, ChunkStatus.EMPTY, true).func_230344_a_(ModStructures.VAULT, start);
					}
				}

				raid.searchForStart(world, chunkPos);
				raid.teleportToStart(world, player);
			} catch(Exception e) {
				e.printStackTrace();
			}
		});

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
		this.playerLevels.clear();

		nbt.getList("ActiveRaids", Constants.NBT.TAG_COMPOUND).forEach(raidNBT -> {
			VaultRaid raid = VaultRaid.fromNBT((CompoundNBT)raidNBT);
			this.activeRaids.put(raid.getPlayerId(), raid);
		});

		nbt.getList("PlayerLevels", Constants.NBT.TAG_STRING).forEach(levelNBT -> {
			String[] data = levelNBT.getString().split(Pattern.quote("@"));
			this.playerLevels.put(UUID.fromString(data[0]), Integer.parseInt(data[1]));
		});

		this.xOffset = nbt.getInt("XOffset");
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		ListNBT raidsList = new ListNBT();
		this.activeRaids.values().forEach(raid -> raidsList.add(raid.serializeNBT()));
		nbt.put("ActiveRaids", raidsList);

		ListNBT levelsList = new ListNBT();
		this.playerLevels.forEach((uuid, level) -> levelsList.add(StringNBT.valueOf(uuid.toString() + "@" + level)));
		nbt.put("PlayerLevels", levelsList);

		nbt.putInt("XOffset", this.xOffset);
		return nbt;
	}

	public static VaultRaidData get(ServerWorld world) {
		return world.getServer().func_241755_D_().getSavedData().getOrCreate(VaultRaidData::new, DATA_NAME);
	}

}
