package iskallia.vault.block;

import iskallia.vault.block.entity.GiftStatueTileEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
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

public class GiftStatueBlock extends Block {

    public static final VoxelShape SHAPE_0 = Block.makeCuboidShape(1, 0, 1, 15, 13, 15);
    public static final VoxelShape SHAPE_1 = Block.makeCuboidShape(1, 0, 1, 15, 5, 15);
    public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 0, 15);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public GiftStatueBlock() {
        super(Properties.create(Material.ROCK, MaterialColor.STONE)
                .hardnessAndResistance(1, 1)
                .notSolid()
                .doesNotBlockMovement());
        this.setDefaultState(this.getStateContainer().getBaseState().with(VARIANT, 0).with(FACING, Direction.SOUTH));

    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof GiftStatueTileEntity) {
            GiftStatueTileEntity statueTileEntity = (GiftStatueTileEntity) tileEntity;
            statueTileEntity.getSkin().updateSkin("iskall85"); //TODO get the appropriate player name
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.GIFT_STATUE_TILE_ENTITY.create();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos pos = context.getPos();
        World world = context.getWorld();
        if (pos.getY() < 255 && world.getBlockState(pos.up()).isReplaceable(context)) {
            return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing()).with(VARIANT, 1); //TODO: get variant from BlockItem
        } else {
            return null;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        int variant = state.get(VARIANT);

        if (variant == 0)
            return SHAPE_0;
        else if (variant == 1)
            return SHAPE_1;

        return Block.makeCuboidShape(0, 0, 0, 16, 16, 16);
    }

}
