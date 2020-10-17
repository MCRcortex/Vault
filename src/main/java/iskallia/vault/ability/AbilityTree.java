package iskallia.vault.ability;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.network.ModNetwork;
import iskallia.vault.network.message.VaultLevelMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class AbilityTree implements INBTSerializable<CompoundNBT> {

    private final UUID uuid;
    private int vaultLevel;
    private int exp;
    private int unspentSkillPts;
    private List<AbilityNode<?>> nodes = new ArrayList<>();

    public AbilityTree(UUID uuid) {
        this.uuid = uuid;
        this.add(null, ModConfigs.ABILITIES.getAll().stream()
                .map(abilityGroup -> new AbilityNode<>(abilityGroup, abilityGroup.getLevels() - 1))
                .toArray(AbilityNode<?>[]::new));
    }

    public int getVaultLevel() {
        return vaultLevel;
    }

    public int getExp() {
        return exp;
    }

    public int getTnl() {
        return ModConfigs.LEVELS_META.getLevelMeta(this.vaultLevel).tnl;
    }

    public int getUnspentSkillPts() {
        return unspentSkillPts;
    }

    public List<AbilityNode<?>> getNodes() {
        return this.nodes;
    }

    public AbilityNode<?> getNodeByName(String name) {
        return this.nodes.stream().filter(node -> node.getGroup().getParentName().equals(name))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown ability name -> " + name));
    }

    /* ------------------------------------ */

    public AbilityTree add(MinecraftServer server, AbilityNode<?>... nodes) {
        for (AbilityNode<?> node : nodes) {
            this.runIfPresent(server, player -> node.getAbility().onAdded(player));
            this.nodes.add(node);
        }

        return this;
    }

    public AbilityTree setLevel(MinecraftServer server, int level) {
        this.vaultLevel = level;
        this.exp = 0;

        syncLevelInfo(server);

        return this;
    }

    public AbilityTree addExp(MinecraftServer server, int exp) {
        int tnl;
        this.exp += exp;

        while (this.exp >= (tnl = getTnl())) {
            this.vaultLevel++;
            this.unspentSkillPts++;
            this.exp -= tnl; // Carry extra exp to next level!
        }

        syncLevelInfo(server);

        return this;
    }

    /* ------------------------------------ */

    public AbilityTree tick(MinecraftServer server) {
        this.runIfPresent(server, player -> this.nodes.forEach(node -> node.getAbility().tick(player)));
        return this;
    }

    public AbilityTree remove(MinecraftServer server, AbilityNode<?>... nodes) {
        for (AbilityNode<?> node : nodes) {
            this.runIfPresent(server, player -> node.getAbility().onRemoved(player));
            this.nodes.remove(node);
        }

        return this;
    }

    public boolean runIfPresent(MinecraftServer server, Consumer<ServerPlayerEntity> action) {
        if (server == null) return false;
        ServerPlayerEntity player = server.getPlayerList().getPlayerByUUID(this.uuid);
        if (player == null) return false;
        action.accept(player);
        return true;
    }

    /* ------------------------------------ */

    public void syncLevelInfo(MinecraftServer server) {
        runIfPresent(server, player -> {
            ModNetwork.channel.sendTo(
                    new VaultLevelMessage(this.vaultLevel, this.exp, this.getTnl()),
                    player.connection.netManager,
                    NetworkDirection.PLAY_TO_CLIENT
            );
        });
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putInt("vaultLevel", vaultLevel);

        nbt.putInt("exp", exp);

        nbt.putInt("unspentSkillPts", unspentSkillPts);

        ListNBT list = new ListNBT();
        this.nodes.stream().map(AbilityNode::serializeNBT).forEach(list::add);
        nbt.put("Nodes", list);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.vaultLevel = nbt.getInt("vaultLevel");

        this.exp = nbt.getInt("exp");

        this.unspentSkillPts = nbt.getInt("unspentSkillPts");

        this.vaultLevel = nbt.getInt("vaultLevel");

        ListNBT list = nbt.getList("Nodes", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            this.add(null, AbilityNode.fromNBT(list.getCompound(i), PlayerAbility.class));
        }
    }

}
