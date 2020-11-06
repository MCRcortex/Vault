package iskallia.vault.block;

import iskallia.vault.block.entity.VaultPedestalTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class VaultAltarBlock extends Block {

	public VaultAltarBlock() {
		super(Properties.create(Material.ROCK, MaterialColor.DIAMOND).setRequiresTool().hardnessAndResistance(3f, 3f));
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isRemote)
			return ActionResultType.SUCCESS;

		VaultPedestalTileEntity[] pedestals = getNearbyPedestals(worldIn, pos);

		if (pedestals != null) {
			// found 4 pedestals.. validate the infusion recipe.
		}

		return ActionResultType.SUCCESS;
	}

	/**
	 * Gets an array of 4 pedestals within a bounding box specified by [range]
	 * 
	 * @param world
	 *            The world to check positions.
	 * @param altar
	 *            The position of the Vault Altar to search near.
	 * @return
	 */
	private VaultPedestalTileEntity[] getNearbyPedestals(World world, BlockPos altar) {
		VaultPedestalTileEntity[] pedestals = new VaultPedestalTileEntity[4];
		int index = 0;
		int altarX = altar.getX();
		int altarY = altar.getY();
		int altarZ = altar.getZ();
		for (int x = -5; x < 5; x++) {
			for (int y = -5; y < 5; y++) {
				for (int z = -5; z < 5; z++) {
					BlockPos pos = new BlockPos(altarX + x, altarY + y, altarZ + z);
					TileEntity te = world.getTileEntity(pos);
					if (te == null || !(te instanceof VaultPedestalTileEntity))
						continue;
					VaultPedestalTileEntity pedestal = (VaultPedestalTileEntity) te;
					pedestals[index++] = pedestal;
					if (index == 4)
						return pedestals;
				}
			}
		}
		return null;
	}
}
