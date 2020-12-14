package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.world.data.VaultSetsData;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class RelicStatueTileEntity extends TileEntity {

    protected String relicSet;

    public RelicStatueTileEntity() {
        super(ModBlocks.RELIC_STATUE_TILE_ENTITY);
        relicSet = VaultSetsData.RelicSet.DRAGON.getName();
    }

    public String getRelicSet() {
        return relicSet;
    }

    public void setRelicSet(String relicSet) {
        this.relicSet = relicSet;
    }

    public void sendUpdates() {
        this.world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
        this.world.notifyNeighborsOfStateChange(pos, this.getBlockState().getBlock());
        markDirty();
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putString("RelicSet", relicSet);
        return super.write(nbt);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        this.relicSet = nbt.getString("RelicSet");
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        nbt.putString("RelicSet", relicSet);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(state, tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getNbtCompound();
        handleUpdateTag(getBlockState(), nbt);
    }

}
