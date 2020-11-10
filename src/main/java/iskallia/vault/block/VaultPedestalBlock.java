package iskallia.vault.block;

import iskallia.vault.block.entity.VaultPedestalTileEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class VaultPedestalBlock extends Block {

	public VaultPedestalBlock() {
		super(Properties.create(Material.ROCK, MaterialColor.DIAMOND).setRequiresTool().hardnessAndResistance(3f, 3f).setLightLevel(light -> {
			return 0;
		}));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModBlocks.VAULT_PEDESTAL_TILE_ENTITY.create();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (worldIn.isRemote)
			return;

		VaultPedestalTileEntity pedestal = getVaultPedestalTileEntity(worldIn, pos);
		if (pedestal == null)
			return;

		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	// @Override
	// public ActionResultType onBlockActivated(BlockState state, World worldIn,
	// BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
	// if (worldIn.isRemote)
	// return ActionResultType.SUCCESS;
	//
	// if (handIn != Hand.MAIN_HAND)
	// return ActionResultType.SUCCESS;
	//
	// if (player.getHeldItemMainhand() == ItemStack.EMPTY)
	// return ActionResultType.SUCCESS;
	//
	// VaultPedestalTileEntity pedestal = getVaultPedestalTileEntity(worldIn, pos);
	//
	// if (pedestal == null)
	// return ActionResultType.SUCCESS;
	//
	// if (pedestal.getRequiredItem() != null)
	// return ActionResultType.SUCCESS;
	//
	// ItemStack heldItem = player.getHeldItem(handIn);
	// pedestal.getRequiredItem().setItem(heldItem);
	// pedestal.getRequiredItem().addAmount(heldItem.getCount());
	// pedestal.update();
	//
	// player.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
	//
	// return ActionResultType.SUCCESS;
	// }

	public static VaultPedestalTileEntity getVaultPedestalTileEntity(World worldIn, BlockPos pos) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && !(te instanceof VaultPedestalTileEntity))
			return null;
		VaultPedestalTileEntity pedestal = (VaultPedestalTileEntity) te;
		return pedestal;
	}

}
