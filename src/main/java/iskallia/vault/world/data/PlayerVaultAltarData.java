package iskallia.vault.world.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import iskallia.vault.Vault;
import iskallia.vault.altar.AltarInfusion;
import iskallia.vault.altar.RequiredItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.NoteBlockEvent;

public class PlayerVaultAltarData extends WorldSavedData implements Supplier {

    protected static final String DATA_NAME = Vault.MOD_ID + "_PlayerAltarData";
    public static PlayerVaultAltarData instance;
    private Map<UUID, RequiredItem[]> playerMap = new HashMap<>();

    public PlayerVaultAltarData() {
        super(DATA_NAME);
    }

    public static PlayerVaultAltarData get(ServerWorld world) {
        DimensionSavedDataManager storage = world.getSavedData();
        Supplier<PlayerVaultAltarData> sup = new PlayerVaultAltarData();
        PlayerVaultAltarData data = storage.getOrCreate(sup, DATA_NAME);
        if (data == null) {
            data = new PlayerVaultAltarData();
            storage.set(data);
        }
        return data;
    }

    public boolean playerExists(UUID id) {
        return playerMap.containsKey(id);
    }

    public PlayerVaultAltarData addPlayer(UUID id, RequiredItem[] items) {
        playerMap.put(id, items);
        markDirty();
        return this;
    }

    public PlayerVaultAltarData removePlayer(UUID id) {
        playerMap.remove(id);
        markDirty();
        return this;
    }

    public PlayerVaultAltarData clear() {
        playerMap.clear();
        markDirty();
        return this;
    }

    public RequiredItem[] getRequiredItems(PlayerEntity player) {
        return getRequiredItems(player.getUniqueID());
    }

    public RequiredItem[] getRequiredItems(UUID uuid) {
        return playerMap.get(uuid);
    }

    public Map<UUID, RequiredItem[]> getMap() {
        return playerMap;
    }

    @Override
    public void read(CompoundNBT nbt) {
        Vault.LOGGER.info("Reading PlayerVaultAltarData...");
        ListNBT playerList = nbt.getList("PlayerEntries", Constants.NBT.TAG_STRING);
        ListNBT requiredItemsList = nbt.getList("RequiredItemsEntries", Constants.NBT.TAG_COMPOUND);
        Vault.LOGGER.info("PlayerCount: " + playerList.size());
        Vault.LOGGER.info("RequiredItemsCount: " + requiredItemsList.size());

        if (playerList.size() != requiredItemsList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }

        for (int i = 0; i < playerList.size(); i++) {
            UUID playerUUID = UUID.fromString(playerList.getString(i));
            Vault.LOGGER.info("PlayerUUID: " + playerUUID);
            RequiredItem[] items = AltarInfusion.deserialize(requiredItemsList.getCompound(i));
            for (RequiredItem item : items) {
                Vault.LOGGER.info("Item: " + item.getItem().getDisplayName().getString());
            }
            playerMap.put(playerUUID, items);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        ListNBT playerList = new ListNBT();
        ListNBT requiredItemsList = new ListNBT();

        this.playerMap.forEach((uuid, requiredItems) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));
            requiredItemsList.add(AltarInfusion.serialize(requiredItems));
        });

        nbt.put("PlayerEntries", playerList);
        nbt.put("RequiredItemsEntries", requiredItemsList);

        return nbt;
    }

    @Override
    public Object get() {
        return this;
    }
}
