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
		System.out.println("BlockPlaced");
		TileEntity te = worldIn.getTileEntity(pos);
		if (te == null || !(te instanceof VaultPedestalTileEntity))
			return;

		VaultPedestalTileEntity pedestal = (VaultPedestalTileEntity) te;
		pedestal.setPlayerName("jmilthedude");

		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
}
