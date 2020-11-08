package iskallia.vault.skill;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.network.ModNetwork;
import iskallia.vault.network.message.VaultLevelMessage;
import iskallia.vault.util.NetcodeUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.UUID;

public class PlayerVaultStats implements INBTSerializable<CompoundNBT> {

    private final UUID uuid;
    private int vaultLevel;
    private int exp;
    private int unspentSkillPts;

    public PlayerVaultStats(UUID uuid) {
        this.uuid = uuid;
    }

    public int getVaultLevel() {
        return vaultLevel;
    }

    public int getExp() {
        return exp;
    }

    public int getUnspentSkillPts() {
        return unspentSkillPts;
    }

    public int getTnl() {
        return ModConfigs.LEVELS_META.getLevelMeta(this.vaultLevel).tnl;
    }

    /* --------------------------------------- */

    public PlayerVaultStats setVaultLevel(MinecraftServer server, int level) {
        this.vaultLevel = level;
        this.exp = 0;
        sync(server);

        return this;
    }

    public PlayerVaultStats addVaultExp(MinecraftServer server, int exp) {
        int tnl;
        this.exp += exp;

        while (this.exp >= (tnl = getTnl())) {
            this.vaultLevel++;
            this.unspentSkillPts++;
            this.exp -= tnl; // Carry extra exp to next level!
        }

        sync(server);

        return this;
    }

    public PlayerVaultStats spendSkillPoints(MinecraftServer server, int amount) {
        this.unspentSkillPts -= amount;

        sync(server);

        return this;
    }

    public PlayerVaultStats reset(MinecraftServer server) {
        this.vaultLevel = 0;
        this.exp = 0;
        this.unspentSkillPts = 0;

        sync(server);

        return this;
    }

    public PlayerVaultStats addSkillPoints(int amount) {
        this.unspentSkillPts += amount;
        return this;
    }

    /* --------------------------------------- */

    public void sync(MinecraftServer server) {
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
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.vaultLevel = nbt.getInt("vaultLevel");
        this.exp = nbt.getInt("exp");
        this.unspentSkillPts = nbt.getInt("unspentSkillPts");
        this.vaultLevel = nbt.getInt("vaultLevel");
    }
}
