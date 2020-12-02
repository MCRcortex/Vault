package iskallia.vault.container;

import iskallia.vault.block.VendingMachineBlock;
import iskallia.vault.block.entity.VendingMachineTileEntity;
import iskallia.vault.init.ModContainers;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VendingMachineContainer extends Container {

    protected VendingMachineTileEntity tileEntity;

    public VendingMachineContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(ModContainers.VENDING_MACHINE_CONTAINER, windowId);

        BlockState blockState = world.getBlockState(pos);
        this.tileEntity = VendingMachineBlock.getVendingMachineTile(world, pos, blockState);

        // Player Inventory
        for (int i1 = 0; i1 < 3; ++i1) {
            for (int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(playerInventory, k1 + i1 * 9 + 9,
                        167 + k1 * 18,
                        86 + i1 * 18));
            }
        }

        // Player Hotbar
        for (int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(playerInventory, j1, 167 + j1 * 18, 144));
        }
    }

    public VendingMachineTileEntity getTileEntity() {
        return tileEntity;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

}
