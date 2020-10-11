package iskallia.vault.ability;

import iskallia.vault.init.ModConfigs;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class AbilityTree implements INBTSerializable<CompoundNBT> {

    private final UUID uuid;
    private List<AbilityNode<?>> nodes = new ArrayList<>();

    public AbilityTree(UUID uuid) {
        this.uuid = uuid;
        this.add(null, ModConfigs.ABILITIES.getAll().stream()
                .map(abilityGroup -> new AbilityNode<>(abilityGroup, abilityGroup.getLevels() - 1))
                .toArray(AbilityNode<?>[]::new));
    }

    public List<AbilityNode<?>> getNodes() {
        return this.nodes;
    }

    public AbilityNode getNodeByName(String name) {
        return this.nodes.stream().filter(node -> node.getGroup().getParentName().equals(name))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown ability name -> " + name));
    }

    public AbilityTree add(MinecraftServer server, AbilityNode<?>... nodes) {
        for (AbilityNode<?> node : nodes) {
            this.runIfPresent(server, player -> node.getAbility().onAdded(player));
            this.nodes.add(node);
        }

        return this;
    }

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

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT list = new ListNBT();
        this.nodes.stream().map(AbilityNode::toNBT).forEach(list::add);
        nbt.put("Nodes", list);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT list = nbt.getList("Nodes", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < list.size(); i++) {
            this.add(null, AbilityNode.fromNBT(list.getCompound(i), PlayerAbility.class));
        }
    }

}
