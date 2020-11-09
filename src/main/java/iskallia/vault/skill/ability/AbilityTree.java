package iskallia.vault.skill.ability;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.NetcodeUtils;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AbilityTree {

    private final UUID uuid;
    private List<AbilityNode<?>> nodes = new ArrayList<>();

    public AbilityTree(UUID uuid) {
        this.uuid = uuid;
    }

    public List<AbilityNode<?>> getNodes() {
        return nodes;
    }

    public AbilityNode<?> getNodeOf(AbilityGroup<?> abilityGroup) {
        return this.getNodeByName(abilityGroup.getParentName());
    }

    public AbilityNode<?> getNodeByName(String name) {
        return this.nodes.stream().filter(node -> node.getGroup().getParentName().equals(name))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown ability name -> " + name));
    }

    /* ---------------------------------- */

    public AbilityTree upgradeAbility(MinecraftServer server, AbilityNode<?> abilityNode) {
        this.remove(server, abilityNode);

        AbilityGroup<?> abilityGroup = ModConfigs.ABILITIES.getByName(abilityNode.getGroup().getParentName());
        AbilityNode<?> upgradedAbilityNode = new AbilityNode<>(abilityGroup, abilityNode.getLevel() + 1);
        this.add(server, upgradedAbilityNode);

        // TODO: Spend skill point

        return this;
    }

    /* ---------------------------------- */

    public AbilityTree add(MinecraftServer server, AbilityNode<?>... nodes) {
        for (AbilityNode<?> node : nodes) {
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (node.isLearned()) {
                    node.getAbility().onAdded(player);
                }
            });
            this.nodes.add(node);
        }

        return this;
    }

    public AbilityTree remove(MinecraftServer server, AbilityNode<?>... nodes) {
        for (AbilityNode<?> node : nodes) {
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (node.isLearned())
                    node.getAbility().onRemoved(player);
            });
            this.nodes.remove(node);
        }

        return this;
    }

}
