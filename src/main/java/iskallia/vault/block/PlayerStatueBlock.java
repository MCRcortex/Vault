package iskallia.vault.block;

import iskallia.vault.block.entity.PlayerStatueTileEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class PlayerStatueBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final VoxelShape SHAPE = Block.makeCuboidShape(1, 0, 1, 15, 5, 15);

    public PlayerStatueBlock() {
        super(Properties.create(Material.ROCK, MaterialColor.STONE)
                .hardnessAndResistance(1, 1)
                .notSolid()
                .doesNotBlockMovement());

        this.setDefaultState(this.stateContainer.getBaseState()
                .with(FACING, Direction.SOUTH));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState()
                .with(FACING, context.getPlacementHorizontalFacing());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.PLAYER_STATUE_TILE_ENTITY.create();
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            ItemStack itemStack = new ItemStack(getBlock());

            if (tileEntity instanceof PlayerStatueTileEntity) {
                PlayerStatueTileEntity statueTileEntity = (PlayerStatueTileEntity) tileEntity;

                CompoundNBT statueNBT = statueTileEntity.serializeNBT();
                CompoundNBT stackNBT = new CompoundNBT();
                stackNBT.put("BlockEntityTag", statueNBT);

                itemStack.setTag(stackNBT);
            }

            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
            itemEntity.setDefaultPickupDelay();
            world.addEntity(itemEntity);
        }

        super.onBlockHarvested(world, pos, state, player);
    }

}
