package iskallia.vault.block;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.item.ItemVaultCrystal;
import iskallia.vault.world.gen.PortalPlacer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class VaultGateBlock extends Block {

    public VaultGateBlock() {
        super(Properties.create(Material.IRON, MaterialColor.DIAMOND)
                .setRequiresTool()
                .hardnessAndResistance(3f, 3f)
        );
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) return ActionResultType.SUCCESS;

        Item item = player.getHeldItemMainhand().getItem();
        if (!(item instanceof ItemVaultCrystal)) return ActionResultType.SUCCESS;

        Direction facing = player.getHorizontalFacing();

        if (validatePortalLocation(worldIn, pos, facing)) {
            ItemVaultCrystal crystal = (ItemVaultCrystal) item;
            BlockPos start = getBottomLeft(pos, facing).offset(facing.rotateY(), 1).up();
            getPortalPlacer(crystal.getRarity()).place(worldIn, start, facing.rotateY(), 3, 5);
        }

        return ActionResultType.SUCCESS;
    }

    private PortalPlacer getPortalPlacer(ItemVaultCrystal.Rarity rarity) {
        return new PortalPlacer((pos, random, facing) -> {
            return ModBlocks.VAULT_PORTAL.getDefaultState().with(VaultPortalBlock.AXIS, facing.getAxis()).with(VaultPortalBlock.RARITY, rarity.ordinal());
        }, (pos, random, facing) -> {
            Block[] blocks = {
                    Blocks.BLACKSTONE, Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE,
                    Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
            };

            return blocks[random.nextInt(blocks.length)].getDefaultState();
        });
    }

    private boolean validatePortalLocation(World worldIn, BlockPos pos, Direction facing) {
        boolean valid = true;
        Iterable<BlockPos> positions = BlockPos.getAllInBoxMutable(getBottomLeft(pos, facing), getTopRight(pos, facing));
        for (BlockPos blockPos : positions) {
            BlockState state = worldIn.getBlockState(blockPos);
            if (!(state.getBlock().getBlock() instanceof VaultGateBlock) && !worldIn.isAirBlock(blockPos)) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    private BlockPos getBottomLeft(BlockPos pos, Direction facing) {
        return pos.offset(facing.rotateY(), -2);
    }

    private BlockPos getTopRight(BlockPos pos, Direction facing) {
        return pos.offset(facing.rotateY(), 2).add(0, 6, 0);
    }
}
