package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class VaultPortalTileEntity extends TileEntity {

    private String playerBossName;

    public VaultPortalTileEntity() {
        super(ModBlocks.VAULT_PORTAL_TILE_ENTITY);
    }


    public void sendUpdates() {
        this.world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
        this.world.notifyNeighborsOfStateChange(pos, this.getBlockState().getBlock());
        markDirty();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (playerBossName != null)
            compound.putString("playerBossName", playerBossName);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        if (nbt.contains("playerBossName"))
            playerBossName = nbt.getString("playerBossName");
        super.read(state, nbt);
    }

    public String getPlayerBossName() {
        return playerBossName;
    }

    public void setPlayerBossName(String name) {
        this.playerBossName = name;
    }
}
