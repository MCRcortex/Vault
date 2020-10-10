package iskallia.vault.world.data;

import iskallia.vault.Vault;
import iskallia.vault.ability.AbilityNode;
import iskallia.vault.ability.AbilityTree;
import iskallia.vault.init.ModFeatures;
import iskallia.vault.mixin.ChunkGeneratorMixin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.BiomeRegistry;
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

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerAbilitiesData extends WorldSavedData {

	protected static final String DATA_NAME = Vault.MOD_ID + "_PlayerAbilities";

	private Map<UUID, AbilityTree> playerMap = new HashMap<>();

	public PlayerAbilitiesData() {
		this(DATA_NAME);
	}

	public PlayerAbilitiesData(String name) {
		super(name);
	}

	public AbilityTree getAbilities(PlayerEntity player) {
		return this.getAbilities(player.getUniqueID());
	}

	public AbilityTree getAbilities(UUID uuid) {
		return this.playerMap.computeIfAbsent(uuid, AbilityTree::new);
	}

	public PlayerAbilitiesData add(ServerPlayerEntity player, AbilityNode<?>... nodes) {
		this.getAbilities(player).add(player.getServer(), nodes);
		return this;
	}

	public PlayerAbilitiesData remove(ServerPlayerEntity player, AbilityNode<?>... nodes) {
		this.getAbilities(player).remove(player.getServer(), nodes);
		return this;
	}

	public PlayerAbilitiesData tick(MinecraftServer server) {
		this.playerMap.values().forEach(abilityTree -> abilityTree.tick(server));
		return this;
	}

	@SubscribeEvent
	public static void onTick(TickEvent.WorldTickEvent event) {
		if(event.side == LogicalSide.SERVER) {
			get((ServerWorld)event.world).tick(((ServerWorld)event.world).getServer());
		}
	}

	public static boolean generated = false;

	@SubscribeEvent
	public static void onTick(TickEvent.PlayerTickEvent event) {
		if(event.side == LogicalSide.SERVER) {
			get((ServerWorld)event.player.world).getAbilities(event.player);

			//todo: remove
			//if(event.player.world.getDimensionKey() != Vault.WORLD_KEY) {
			//	ServerWorld world = event.player.getServer().getWorld(Vault.WORLD_KEY);
			//	event.player.changeDimension(world);

				/*
				DimensionStructuresSettings.field_236191_b_ = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
						.putAll(DimensionStructuresSettings.field_236191_b_)
						.put(VAULT, new StructureSeparationSettings(1, 0, -1)).build();

				world.getChunkProvider().getChunkGenerator().func_235957_b_().func_236195_a_().put(ModStructures.VAULT,
						new StructureSeparationSettings(1, 0, -1));*/
			/*
			if(generated)return;
			generated = true;
			ServerWorld world = (ServerWorld)event.player.world;

				((IChunkGeneratorAccessor)world.getChunkProvider().getChunkGenerator()).callFunc_242705_a(ModFeatures.VAULT_FEATURE,
						world.func_241828_r(), world.func_241112_a_(), world.getChunk(100, 100),
						world.getStructureTemplateManager(), world.getSeed(), new ChunkPos(100, 100), BiomeRegistry.PLAINS);
			//}*/
		}
	}

	@Override
	public void read(CompoundNBT nbt) {
		ListNBT playerList = nbt.getList("PlayerEntries", Constants.NBT.TAG_STRING);
		ListNBT abilityList = nbt.getList("AbilityEntries", Constants.NBT.TAG_COMPOUND);

		if(playerList.size() != abilityList.size()) {
			throw new IllegalStateException("Map doesn't have the same amount of keys as values");
		}

		for(int i = 0; i < playerList.size(); i++) {
			this.getAbilities(UUID.fromString(playerList.getString(i))).deserializeNBT(abilityList.getCompound(i));
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		ListNBT playerList = new ListNBT();
		ListNBT abilityList = new ListNBT();

		this.playerMap.forEach((uuid, abilityTree) -> {
			playerList.add(StringNBT.valueOf(uuid.toString()));
			abilityList.add(abilityTree.serializeNBT());
		});

		nbt.put("PlayerEntries", playerList);
		nbt.put("AbilityEntries", abilityList);
		return nbt;
	}

	public static PlayerAbilitiesData get(ServerWorld world) {
		return world.getServer().func_241755_D_().getSavedData().getOrCreate(PlayerAbilitiesData::new, DATA_NAME);
	}

}
