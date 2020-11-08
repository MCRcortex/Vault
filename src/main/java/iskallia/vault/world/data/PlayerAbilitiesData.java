package iskallia.vault.world.data;

import iskallia.vault.Vault;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
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

    private Map<UUID, TalentTree> playerMap = new HashMap<>();

    public PlayerAbilitiesData() {
        this(DATA_NAME);
    }

    public PlayerAbilitiesData(String name) {
        super(name);
    }

    public TalentTree getAbilities(PlayerEntity player) {
        return this.getAbilities(player.getUniqueID());
    }

    public TalentTree getAbilities(UUID uuid) {
        return this.playerMap.computeIfAbsent(uuid, TalentTree::new);
    }

    public PlayerAbilitiesData resetAbilityTree(ServerPlayerEntity player) {
        UUID uniqueID = player.getUniqueID();

        TalentTree oldTalentTree = playerMap.get(uniqueID);
        if (oldTalentTree != null) {
            for (TalentNode<?> node : oldTalentTree.getNodes()) {
                if (node.isLearned())
                    node.getTalent().onRemoved(player);
            }
        }

        TalentTree talentTree = new TalentTree(uniqueID);
        this.playerMap.put(uniqueID, talentTree);

        talentTree.syncLevelInfo(player.getServer());

        markDirty();
        return this;
    }

    public PlayerAbilitiesData setVaultLevel(ServerPlayerEntity player, int level) {
        this.getAbilities(player).setVaultLevel(player.getServer(), level);

        markDirty();
        return this;
    }

    public PlayerAbilitiesData addVaultExp(ServerPlayerEntity player, int exp) {
        this.getAbilities(player).addVaultExp(player.getServer(), exp);

        markDirty();
        return this;
    }

    public PlayerAbilitiesData add(ServerPlayerEntity player, TalentNode<?>... nodes) {
        this.getAbilities(player).add(player.getServer(), nodes);

        markDirty();
        return this;
    }

    public PlayerAbilitiesData remove(ServerPlayerEntity player, TalentNode<?>... nodes) {
        this.getAbilities(player).remove(player.getServer(), nodes);

        markDirty();
        return this;
    }

    public PlayerAbilitiesData upgradeAbility(ServerPlayerEntity player, TalentNode<?> talentNode) {
        this.getAbilities(player).upgradeTalent(player.getServer(), talentNode);

        markDirty();
        return this;
    }

    public PlayerAbilitiesData spendSkillPts(ServerPlayerEntity player, int amount) {
        this.getAbilities(player).spendSkillPoints(player.getServer(), amount);

        markDirty();
        return this;
    }

    public PlayerAbilitiesData tick(MinecraftServer server) {
        this.playerMap.values().forEach(abilityTree -> abilityTree.tick(server));
        return this;
    }

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            get((ServerWorld) event.world).tick(((ServerWorld) event.world).getServer());
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            get((ServerWorld) event.player.world).getAbilities(event.player);
        }
    }

    @Override
    public void read(CompoundNBT nbt) {
        ListNBT playerList = nbt.getList("PlayerEntries", Constants.NBT.TAG_STRING);
        ListNBT abilityList = nbt.getList("AbilityEntries", Constants.NBT.TAG_COMPOUND);

        if (playerList.size() != abilityList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }

        for (int i = 0; i < playerList.size(); i++) {
            UUID playerUUID = UUID.fromString(playerList.getString(i));
            this.getAbilities(playerUUID).deserializeNBT(abilityList.getCompound(i));
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
        return world.getServer().func_241755_D_()
                .getSavedData().getOrCreate(PlayerAbilitiesData::new, DATA_NAME);
    }

}
