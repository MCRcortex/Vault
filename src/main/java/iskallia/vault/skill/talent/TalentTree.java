package iskallia.vault.skill.talent;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.network.ModNetwork;
import iskallia.vault.network.message.VaultLevelMessage;
import iskallia.vault.skill.talent.type.PlayerTalent;
import iskallia.vault.util.NetcodeUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TalentTree implements INBTSerializable<CompoundNBT> {

    private final UUID uuid;
    private int vaultLevel;
    private int exp;
    private int unspentSkillPts;
    private List<TalentNode<?>> nodes = new ArrayList<>();

    public TalentTree(UUID uuid) {
        this.uuid = uuid;
        this.add(null, ModConfigs.TALENTS.getAll().stream()
                .map(abilityGroup -> new TalentNode<>(abilityGroup, 0))
                .toArray(TalentNode<?>[]::new));
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

    public List<TalentNode<?>> getNodes() {
        return this.nodes;
    }

    public TalentNode<?> getNodeOf(TalentGroup<?> talentGroup) {
        return getNodeByName(talentGroup.getParentName());
    }

    public TalentNode<?> getNodeByName(String name) {
        return this.nodes.stream().filter(node -> node.getGroup().getParentName().equals(name))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown ability name -> " + name));
    }

    /* ------------------------------------ */

    public TalentTree add(MinecraftServer server, TalentNode<?>... nodes) {
        for (TalentNode<?> node : nodes) {
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (node.isLearned()) {
                    node.getAbility().onAdded(player);
                }
            });
            this.nodes.add(node);
        }

        return this;
    }

    public TalentTree setVaultLevel(MinecraftServer server, int level) {
        this.vaultLevel = level;
        this.exp = 0;

        syncLevelInfo(server);

        return this;
    }

    public TalentTree addVaultExp(MinecraftServer server, int exp) {
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

    public TalentTree spendSkillPoints(MinecraftServer server, int amount) {
        this.unspentSkillPts -= amount;

        syncLevelInfo(server);

        return this;
    }

    public TalentTree addSkillPoints(int amount) {
        this.unspentSkillPts += amount;
        return this;
    }

    public TalentTree upgradeAbility(MinecraftServer server, TalentNode<?> talentNode) {
        this.remove(server, talentNode);

        TalentGroup<?> talentGroup = ModConfigs.TALENTS.getByName(talentNode.getGroup().getParentName());
        TalentNode<?> upgradedTalentNode = new TalentNode<>(talentGroup, talentNode.getLevel() + 1);
        this.add(server, upgradedTalentNode);

        this.spendSkillPoints(server, upgradedTalentNode.getAbility().getCost());

        return this;
    }

    /* ------------------------------------ */

    public TalentTree tick(MinecraftServer server) {
        NetcodeUtils.runIfPresent(server, this.uuid, player -> {
            this.nodes.stream().filter(TalentNode::isLearned)
                    .forEach(node -> node.getAbility().tick(player));
        });
        return this;
    }

    public TalentTree remove(MinecraftServer server, TalentNode<?>... nodes) {
        for (TalentNode<?> node : nodes) {
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (node.isLearned()) {
                    node.getAbility().onRemoved(player);
                }
            });
            this.nodes.remove(node);
        }

        return this;
    }

    /* ------------------------------------ */

    public void syncLevelInfo(MinecraftServer server) {
        NetcodeUtils.runIfPresent(server, this.uuid, player -> {
            ModNetwork.channel.sendTo(
                    new VaultLevelMessage(this.vaultLevel, this.exp, this.getTnl(), this.unspentSkillPts),
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
        this.nodes.stream().map(TalentNode::serializeNBT).forEach(list::add);
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
        this.nodes.clear();
        for (int i = 0; i < list.size(); i++) {
            this.add(null, TalentNode.fromNBT(list.getCompound(i), PlayerTalent.class));
        }
    }

}
