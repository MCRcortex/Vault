package iskallia.vault.world.data;

import iskallia.vault.Vault;
import iskallia.vault.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VaultSetsData extends WorldSavedData {

	protected static final String DATA_NAME = Vault.MOD_ID + "_VaultSets";

	private Map<UUID, Set<String>> playerData = new HashMap<>();

	public VaultSetsData() {
		super(DATA_NAME);
	}

	public VaultSetsData(String name) {
		super(name);
	}

	public int getExtraTime(UUID playerId) {
		return this.playerData.getOrDefault(playerId, Collections.emptySet()).size();
	}

	@SubscribeEvent
	public static void onCrafted(PlayerEvent.ItemCraftedEvent event) {
		//TODO: ur bad iGoodie, git gut
	}

	@Override
	public void read(CompoundNBT nbt) {
		NBTHelper.readMap(nbt, "Sets", ListNBT.class, list -> {
			return IntStream.range(0, list.size()).mapToObj(list::getString).collect(Collectors.toSet());
		});
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		NBTHelper.writeMap(compound, "Sets", this.playerData, ListNBT.class, strings -> {
			ListNBT list = new ListNBT();
			strings.forEach(s -> list.add(StringNBT.valueOf(s)));
			return list;
		});

		return compound;
	}

	public static VaultSetsData get(ServerWorld world) {
		return world.getServer().func_241755_D_()
				.getSavedData().getOrCreate(VaultSetsData::new, DATA_NAME);
	}

}
