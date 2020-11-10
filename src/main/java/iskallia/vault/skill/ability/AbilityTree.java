package iskallia.vault.skill.ability;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.network.ModNetwork;
import iskallia.vault.network.message.AbilityCooldownMessage;
import iskallia.vault.network.message.AbilityFocusMessage;
import iskallia.vault.network.message.AbilityKnownOnesMessage;
import iskallia.vault.skill.ability.type.PlayerAbility;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.type.PlayerTalent;
import iskallia.vault.util.NetcodeUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AbilityTree implements INBTSerializable<CompoundNBT> {

    private final UUID uuid;
    private List<AbilityNode<?>> nodes = new ArrayList<>();
    private int focusedAbilityIndex;
    private boolean active;
    private int cooldownTicks;

    public AbilityTree(UUID uuid) {
        this.uuid = uuid;
        this.add(null, ModConfigs.ABILITIES.getAll().stream()
                .map(abilityGroup -> new AbilityNode<>(abilityGroup, 0))
                .toArray(AbilityNode[]::new));
    }

    public List<AbilityNode<?>> getNodes() {
        return nodes;
    }

    public List<AbilityNode<?>> learnedNodes() {
        return nodes.stream()
                .filter(AbilityNode::isLearned)
                .collect(Collectors.toList());
    }

    public AbilityNode<?> getFocusedAbility() {
        List<AbilityNode<?>> learnedNodes = learnedNodes();
        if (learnedNodes.size() == 0) return null;
        return learnedNodes.get(focusedAbilityIndex);
    }

    public AbilityNode<?> getNodeOf(AbilityGroup<?> abilityGroup) {
        return this.getNodeByName(abilityGroup.getParentName());
    }

    public AbilityNode<?> getNodeByName(String name) {
        return this.nodes.stream().filter(node -> node.getGroup().getParentName().equals(name))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown ability name -> " + name));
    }

    public boolean isActive() {
        return active;
    }

    /* ---------------------------------- */

    public AbilityTree scrollUp(MinecraftServer server) {
        System.out.println("Scroll up");
        List<AbilityNode<?>> learnedNodes = learnedNodes();

        if (learnedNodes.size() != 0) {
            AbilityNode<?> previouslyFocused = getFocusedAbility();
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                previouslyFocused.getAbility().onBlur(player);
            });

            this.focusedAbilityIndex++;
            if (this.focusedAbilityIndex >= learnedNodes.size())
                this.focusedAbilityIndex -= learnedNodes.size();
            this.active = false;

            AbilityNode<?> newFocused = getFocusedAbility();
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                newFocused.getAbility().onFocus(player);
            });

            syncFocusedIndex(server);
        }

        return this;
    }

    public AbilityTree scrollDown(MinecraftServer server) {
        System.out.println("Scroll down");
        List<AbilityNode<?>> learnedNodes = learnedNodes();

        if (learnedNodes.size() != 0) {
            AbilityNode<?> previouslyFocused = getFocusedAbility();
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                previouslyFocused.getAbility().onBlur(player);
            });

            this.focusedAbilityIndex--;
            if (this.focusedAbilityIndex < 0)
                this.focusedAbilityIndex += learnedNodes.size();
            this.active = false;

            AbilityNode<?> newFocused = getFocusedAbility();
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                newFocused.getAbility().onFocus(player);
            });

            syncFocusedIndex(server);
        }

        return this;
    }

    public void keyDown(MinecraftServer server) {
        System.out.println("Key down");

        AbilityNode<?> focusedAbility = getFocusedAbility();

        if (focusedAbility == null) return;

        PlayerAbility.Behavior behavior = focusedAbility.getAbility().getBehavior();

        if (behavior == PlayerAbility.Behavior.HOLD_TO_ACTIVATE) {
            active = true;
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                focusedAbility.getAbility().onAction(player, active);
            });
            putOnCooldown(server);
        }
    }

    public void keyUp(MinecraftServer server) {
        System.out.println("Key up");

        AbilityNode<?> focusedAbility = getFocusedAbility();

        if (focusedAbility == null) return;

        PlayerAbility.Behavior behavior = focusedAbility.getAbility().getBehavior();

        if (behavior == PlayerAbility.Behavior.PRESS_TO_TOGGLE) {
            active = !active;
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                focusedAbility.getAbility().onAction(player, active);
            });
            putOnCooldown(server);

        } else if (behavior == PlayerAbility.Behavior.HOLD_TO_ACTIVATE) {
            active = false;
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                focusedAbility.getAbility().onAction(player, active);
            });
            putOnCooldown(server);

        } else if (behavior == PlayerAbility.Behavior.RELEASE_TO_PERFORM) {
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                focusedAbility.getAbility().onAction(player, active);
            });
            putOnCooldown(server);
        }
    }

    public void putOnCooldown(MinecraftServer server) {
        this.cooldownTicks = ModConfigs.ABILITIES.cooldownTicks;
        syncCooldownTicks(server);
    }

    public AbilityTree upgradeAbility(MinecraftServer server, AbilityNode<?> abilityNode) {
        this.remove(server, abilityNode);

        AbilityGroup<?> abilityGroup = ModConfigs.ABILITIES.getByName(abilityNode.getGroup().getParentName());
        AbilityNode<?> upgradedAbilityNode = new AbilityNode<>(abilityGroup, abilityNode.getLevel() + 1);
        this.add(server, upgradedAbilityNode);

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

    /* ---------------------------------- */

    public void tick(TickEvent.PlayerTickEvent event) {
        AbilityNode<?> focusedAbility = getFocusedAbility();

        if (focusedAbility != null) {
            focusedAbility.getAbility().onTick(event.player, isActive());
            cooldownTicks = Math.max(0, cooldownTicks - 1);
        }
    }

    public void sync(MinecraftServer server) {
        syncTree(server);
        syncFocusedIndex(server);
        syncCooldownTicks(server);
    }

    public void syncTree(MinecraftServer server) {
        NetcodeUtils.runIfPresent(server, this.uuid, player -> {
            ModNetwork.channel.sendTo(
                    new AbilityKnownOnesMessage(this),
                    player.connection.netManager,
                    NetworkDirection.PLAY_TO_CLIENT
            );
        });
    }

    public void syncFocusedIndex(MinecraftServer server) {
        NetcodeUtils.runIfPresent(server, this.uuid, player -> {
            ModNetwork.channel.sendTo(
                    new AbilityFocusMessage(this.focusedAbilityIndex),
                    player.connection.netManager,
                    NetworkDirection.PLAY_TO_CLIENT
            );
        });
    }

    public void syncCooldownTicks(MinecraftServer server) {
        NetcodeUtils.runIfPresent(server, this.uuid, player -> {
            ModNetwork.channel.sendTo(
                    new AbilityCooldownMessage(this.cooldownTicks),
                    player.connection.netManager,
                    NetworkDirection.PLAY_TO_CLIENT
            );
        });
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        ListNBT list = new ListNBT();
        this.nodes.stream().map(AbilityNode::serializeNBT).forEach(list::add);
        nbt.put("Nodes", list);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT list = nbt.getList("Nodes", Constants.NBT.TAG_COMPOUND);
        this.nodes.clear();
        for (int i = 0; i < list.size(); i++) {
            this.add(null, AbilityNode.fromNBT(list.getCompound(i), PlayerAbility.class));
        }
    }

}
