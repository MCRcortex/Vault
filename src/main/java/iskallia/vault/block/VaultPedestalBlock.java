package iskallia.vault.block;

import iskallia.vault.block.entity.VaultPedestalTileEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class VaultPedestalBlock extends Block {

	public VaultPedestalBlock() {
		super(Properties.create(Material.ROCK, MaterialColor.DIAMOND).setRequiresTool().hardnessAndResistance(3f, 3f));
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

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if (worldIn.isRemote)
			return;

		VaultPedestalTileEntity pedestal = getVaultPedestalTileEntity(worldIn, pos);
		if (pedestal == null)
			return;

		if (!(entityIn instanceof ItemEntity)) {
			return;
		}

		ItemStack itemEntity = ((ItemEntity) entityIn).getItem();

		if (pedestal.getItem() == null) {
			System.out.println(itemEntity.getDisplayName());
			pedestal.setItem(itemEntity);
			pedestal.setItemCount(itemEntity.getCount());
			entityIn.remove();
		} else if (pedestal.getItem().isItemEqualIgnoreDurability(itemEntity)) {
			System.out.println(itemEntity.getDisplayName());
			int count = itemEntity.getCount();
			pedestal.setItemCount(pedestal.getItemCount() + count);
			entityIn.remove();
		}

	}

	public static VaultPedestalTileEntity getVaultPedestalTileEntity(World worldIn, BlockPos pos) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && !(te instanceof VaultPedestalTileEntity))
			return null;
		VaultPedestalTileEntity pedestal = (VaultPedestalTileEntity) te;
		return pedestal;
	}

}
