package iskallia.vault.block;

import java.util.Random;

import iskallia.vault.block.entity.VaultPedestalTileEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

		Random rand = new Random();
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && !(te instanceof VaultPedestalTileEntity))
			return;
		VaultPedestalTileEntity pedestal = (VaultPedestalTileEntity) te;
		switch (rand.nextInt(3)) {
		case 0:
			pedestal.setItem(new ItemStack(Items.IRON_INGOT));
			pedestal.setItemCount(rand.nextInt(32000));
			break;
		case 1:
			pedestal.setItem(new ItemStack(Items.GOLD_INGOT));
			pedestal.setItemCount(rand.nextInt(32000));
			break;
		case 2:
			pedestal.setItem(new ItemStack(Items.DIAMOND));
			pedestal.setItemCount(rand.nextInt(32000));
			break;
		case 3:
			pedestal.setItem(new ItemStack(Items.NETHERITE_INGOT));
			pedestal.setItemCount(rand.nextInt(32000));
			break;
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

}
