package iskallia.vault.world.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import iskallia.vault.Vault;
import iskallia.vault.altar.PedestalItem;
import iskallia.vault.altar.AltarInfusion;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class PlayerVaultAltarData extends WorldSavedData {

	protected static final String DATA_NAME = Vault.MOD_ID + "_PlayerAltarData";
	private Map<UUID, PedestalItem[]> playerMap = new HashMap<>();

	public PlayerVaultAltarData() {
		super(DATA_NAME);
	}

	public PedestalItem[] getRequiredItems(PlayerEntity player) {
		return getRequiredItems(player.getUniqueID());
	}

	public PedestalItem[] getRequiredItems(UUID uuid) {
		return playerMap.get(uuid);
	}

	public Map<UUID, PedestalItem[]> getMap() {
		return playerMap;
	}

	@Override
	public void read(CompoundNBT nbt) {
		ListNBT playerList = nbt.getList("PlayerEntries", Constants.NBT.TAG_STRING);
		ListNBT requiredItemsList = nbt.getList("RequiredItemsEntries", Constants.NBT.TAG_COMPOUND);

		if (playerList.size() != requiredItemsList.size()) {
			throw new IllegalStateException("Map doesn't have the same amount of keys as values");
		}

		for (int i = 0; i < playerList.size(); i++) {
			UUID playerUUID = UUID.fromString(playerList.getString(i));
			playerMap.put(playerUUID, AltarInfusion.deserialize(requiredItemsList.getCompound(i)));
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		ListNBT playerList = new ListNBT();
		ListNBT requredItemsList = new ListNBT();

		this.playerMap.forEach((uuid, requiredItems) -> {
			playerList.add(StringNBT.valueOf(uuid.toString()));
			requredItemsList.add(AltarInfusion.serialize(requiredItems));
		});

		nbt.put("PlayerEntries", playerList);
		nbt.put("RequiredItemsEntries", requredItemsList);

		return nbt;
	}

	public static PlayerVaultAltarData get(ServerWorld world) {
		return world.getServer().func_241755_D_().getSavedData().getOrCreate(PlayerVaultAltarData::new, DATA_NAME);
	}

}
