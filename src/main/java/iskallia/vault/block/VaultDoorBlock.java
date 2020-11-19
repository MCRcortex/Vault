package iskallia.vault.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class VaultDoorBlock extends DoorBlock {

    protected Item keyItem;

    public VaultDoorBlock(Item keyItem) {
        super(Properties.create(Material.WOOD, MaterialColor.DIAMOND)
                .hardnessAndResistance(-1.0F, 3600000.0F)
                .sound(SoundType.METAL)
                .notSolid());
        this.setDefaultState(this.getStateContainer().getBaseState()
                .with(FACING, Direction.NORTH)
                .with(OPEN, Boolean.FALSE)
                .with(HINGE, DoorHingeSide.LEFT)
                .with(POWERED, Boolean.FALSE)
                .with(HALF, DoubleBlockHalf.LOWER));
        this.keyItem = keyItem;
    }

    public Item getKeyItem() {
        return keyItem;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack heldStack = player.getHeldItem(hand);
        Boolean isOpen = state.get(OPEN);

        if (!isOpen && heldStack.getItem() == getKeyItem()) {
            heldStack.shrink(1);
            return super.onBlockActivated(state, worldIn, pos, player, hand, hit);
        }

        return ActionResultType.PASS;
    }


}
