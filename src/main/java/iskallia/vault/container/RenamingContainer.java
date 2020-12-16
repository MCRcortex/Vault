package iskallia.vault.container;

import iskallia.vault.init.ModContainers;
import iskallia.vault.util.RenameType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.math.BlockPos;


public class RenamingContainer extends Container {

    private RenameType type;
    private int slot;
    private BlockPos pos;

    public RenamingContainer(int windowId) {
        super(ModContainers.RENAMING_CONTAINER, windowId);
        System.out.println("++-----------BLANK CONTAINER-----------++");
    }

    public RenamingContainer(int windowId, RenameType type, int slot) {
        super(ModContainers.RENAMING_CONTAINER, windowId);
        System.out.println("++-----------TRADER CORE-----------++");
        System.out.println("Type: " + type);
        System.out.println("Slot: " + slot);
        this.type = type;
        this.slot = slot;

    }

    public RenamingContainer(int windowId, RenameType type, BlockPos pos) {
        super(ModContainers.RENAMING_CONTAINER, windowId);
        System.out.println("++-----------PLAYER STATUE-----------++");
        System.out.println("Type: " + type);
        System.out.println("Slot: " + pos);
        this.type = type;
        this.pos = pos;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public RenameType getRenameType() {
        return type;
    }

    public int getSlot() {
        return slot;
    }

    public BlockPos getPos() {
        return pos;
    }
}
