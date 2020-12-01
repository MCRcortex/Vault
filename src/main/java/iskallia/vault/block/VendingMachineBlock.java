package iskallia.vault.block;

import iskallia.vault.block.entity.VendingMachineTileEntity;
import iskallia.vault.client.gui.screen.RaffleScreen;
import iskallia.vault.container.VendingMachineContainer;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModSounds;
import iskallia.vault.vending.Product;
import iskallia.vault.vending.Trade;
import iskallia.vault.vending.TraderCore;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.MerchantScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.LinkedList;

public class VendingMachineBlock extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public VendingMachineBlock() {
        super(Properties.create(Material.IRON, MaterialColor.IRON).hardnessAndResistance(2.0F).sound(SoundType.METAL).notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER)
            return true;

        return false;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER)
            return ModBlocks.VENDING_MACHINE_TILE_ENTITY.create();

        return null;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos pos = context.getPos();
        World world = context.getWorld();
        if (pos.getY() < 255 && world.getBlockState(pos.up()).isReplaceable(context)) {
            return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing()).with(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isRemote && player.isCreative()) {
            DoubleBlockHalf half = state.get(HALF);
            if (half == DoubleBlockHalf.UPPER) {
                BlockPos blockpos = pos.down();
                BlockState blockstate = worldIn.getBlockState(blockpos);
                if (blockstate.getBlock() == state.getBlock() && blockstate.get(HALF) == DoubleBlockHalf.LOWER) {
                    worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
                    worldIn.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
                }
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        DoubleBlockHalf half = stateIn.get(HALF);
        if (facing.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (facing == Direction.UP)) {
            return facingState.isIn(this) && facingState.get(HALF) != half ? stateIn.with(FACING, facingState.get(FACING)) : Blocks.AIR.getDefaultState();
        } else {
            return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        worldIn.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), 3);

        VendingMachineTileEntity machine = getVendingMachineTile(worldIn, pos, state);
        if (machine != null) {
            addTestCores(machine);
        }
    }

    private void addTestCores(VendingMachineTileEntity machine) {
        machine.addCore(new TraderCore("jmilthedude", new Trade(new Product(Items.APPLE, 8, null), null, new Product(Items.GOLDEN_APPLE, 1, null))));
        machine.addCore(new TraderCore("KaptainWutax", new Trade(new Product(Items.GOLDEN_APPLE, 8, null), null, new Product(Items.ENCHANTED_GOLDEN_APPLE, 1, null))));
        CompoundNBT nbt = new CompoundNBT();
        ListNBT enchantments = new ListNBT();
        CompoundNBT knockback = new CompoundNBT();
        knockback.putString("id", "minecraft:knockback");
        knockback.putInt("lvl", 10);
        enchantments.add(knockback);
        nbt.put("Enchantments", enchantments);
        machine.addCore(new TraderCore("iGoodie", new Trade(new Product(Items.ENCHANTED_GOLDEN_APPLE, 8, null), null, new Product(Items.STICK, 1, nbt))));
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (worldIn.isRemote) return;
        if (!newState.isAir()) return;

        VendingMachineTileEntity machine = getVendingMachineTile(worldIn, pos, state);
        if (machine != null) {
            machine.ejectCores();

        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isRemote) {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.getSoundHandler().play(SimpleSound.master(
                    ModSounds.VENDING_MACHINE_SFX, 1f, 1f
            ));
            return ActionResultType.SUCCESS;
        }

        VendingMachineTileEntity machine = getVendingMachineTile(world, pos, state);

        if (machine != null) {
//            machine.printCores(); // Nice way to debug, JML!
            NetworkHooks.openGui(
                    (ServerPlayerEntity) player,
                    new INamedContainerProvider() {
                        @Override
                        public ITextComponent getDisplayName() {
                            return new StringTextComponent("Vending Machine");
                        }

                        @Nullable
                        @Override
                        public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                            BlockPos vendingMachinePos = getVendingMachinePos(world, pos);
                            return new VendingMachineContainer(windowId, world, vendingMachinePos, playerInventory, playerEntity);
                        }
                    },
                    (buffer) -> {
                        buffer.writeBlockPos(getVendingMachinePos(world, pos));
                    }
            );
        }

        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HALF, FACING);
    }

    public static BlockPos getVendingMachinePos(World world, BlockPos pos) {
        return world.getBlockState(pos).get(HALF) == DoubleBlockHalf.UPPER
                ? pos.down() : pos;
    }

    public static VendingMachineTileEntity getVendingMachineTile(World world, BlockPos pos, BlockState state) {
        BlockPos vendingMachinePos = getVendingMachinePos(world, pos);

        TileEntity tileEntity = world.getTileEntity(vendingMachinePos);

        if ((!(tileEntity instanceof VendingMachineTileEntity)))
            return null;

        return (VendingMachineTileEntity) tileEntity;
    }

}
