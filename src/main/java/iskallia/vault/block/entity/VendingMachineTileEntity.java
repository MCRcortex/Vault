package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class VendingMachineTileEntity extends TileEntity {

    public VendingMachineTileEntity() {
        super(ModBlocks.VENDING_MACHINE_TILE_ENTITY);
    }


    public void sendUpdates() {
        this.world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
        this.world.notifyNeighborsOfStateChange(pos, this.getBlockState().getBlock());
        markDirty();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
    }
}
