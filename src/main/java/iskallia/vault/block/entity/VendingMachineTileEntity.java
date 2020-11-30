package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.item.ItemTraderCore;
import iskallia.vault.vending.TraderCore;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class VendingMachineTileEntity extends TileEntity {

    private List<TraderCore> cores = new ArrayList<>();

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
        ListNBT list = new ListNBT();
        for (TraderCore core : cores) {
            list.add(TraderCore.writeToNBT(core));
        }
        compound.put("coresList", list);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        ListNBT list = nbt.getList("coresList", Constants.NBT.TAG_COMPOUND);
        for (INBT tag : list) {
            CompoundNBT coreNBT = (CompoundNBT) tag;
            TraderCore core = TraderCore.readFromNBT(coreNBT);
            cores.add(core);
        }
        super.read(state, nbt);
    }

    public void addCore(TraderCore core) {
        this.cores.add(core);
    }

    public void printCores() {
        for (TraderCore core : cores) {
            System.out.println("------ " + core.getName() + "'s trades ------");
            if (core.getTrade().getBuy() != null) {
                System.out.println("Buy: " + core.getTrade().getBuy().toString());
                ItemStack buy = core.getTrade().getBuy().toStack();
                ItemEntity buyItem = new ItemEntity(this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), buy);
                this.world.addEntity(buyItem);
            }
            if (core.getTrade().getExtra() != null) {
                System.out.println("Extra: " + core.getTrade().getExtra().toString());
                ItemStack extra = core.getTrade().getExtra().toStack();
                ItemEntity extraItem = new ItemEntity(this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), extra);
                this.world.addEntity(extraItem);
            }
            if (core.getTrade().getSell() != null) {
                System.out.println("Sell: " + core.getTrade().getSell().toString());
                ItemStack sell = core.getTrade().getSell().toStack();
                ItemEntity sellItem = new ItemEntity(this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), sell);
                this.world.addEntity(sellItem);
            }
        }
    }

    public void ejectCores() {
        for (TraderCore core : cores) {
            ItemEntity entity = new ItemEntity(this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), ItemTraderCore.getStack(core));
            this.world.addEntity(entity);
        }
    }
}
