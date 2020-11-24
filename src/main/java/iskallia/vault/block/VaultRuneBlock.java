package iskallia.vault.block;

import iskallia.vault.block.entity.VaultRuneTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class VaultRuneBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty RUNE_PLACED = BooleanProperty.create("rune_placed");

    public VaultRuneBlock() {
        super(Properties.create(Material.ROCK, MaterialColor.STONE)
                .hardnessAndResistance(Float.MAX_VALUE, Float.MAX_VALUE)
                .notSolid());

        this.setDefaultState(this.stateContainer.getBaseState()
                .with(FACING, Direction.SOUTH)
                .with(RUNE_PLACED, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState()
                .with(FACING, context.getPlacementHorizontalFacing())
                .with(RUNE_PLACED, false);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(RUNE_PLACED);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.VAULT_RUNE_TILE_ENTITY.create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof VaultRuneTileEntity) {
                VaultRuneTileEntity vaultRuneTE = (VaultRuneTileEntity) tileEntity;
                String playerNick = player.getDisplayName().getString();

                if (vaultRuneTE.getBelongsTo().equals(playerNick)) {
                    ItemStack heldStack = player.getHeldItem(hand);

                    if (heldStack.getItem() == ModItems.VAULT_RUNE) {
                        BlockState blockState = world.getBlockState(pos);
                        world.setBlockState(pos, blockState.with(RUNE_PLACED, true), 3);
                    }

                } else {
                    StringTextComponent text = new StringTextComponent(vaultRuneTE.getBelongsTo() + " is responsible with this block.");
                    text.setStyle(Style.EMPTY.setColor(Color.fromInt(0xFF_ff9966)));
                    player.sendStatusMessage(text, true);
                }
            }
        }

        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }

}
