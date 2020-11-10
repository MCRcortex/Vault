package iskallia.vault.block;

import iskallia.vault.altar.PedestalItem;
import iskallia.vault.block.entity.VaultPedestalTileEntity;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.data.PlayerVaultAltarData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

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
			for (VaultPedestalTileEntity pedestal : pedestals) {
				pedestal.print();
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (worldIn.isRemote)
			return;
		PlayerVaultAltarData data = PlayerVaultAltarData.get((ServerWorld) worldIn);
		if (!data.getMap().containsKey(placer.getUniqueID()))
			data.getMap().put(placer.getUniqueID(), ModConfigs.VAULT_PEDESTAL.getRequiredItemsFromConfig());

		VaultPedestalTileEntity[] pedestals = getNearbyPedestals(worldIn, pos);
		int i = 0;
		for (PedestalItem item : data.getMap().get(placer.getUniqueID())) {
			pedestals[i++].setRequiredItem(item);
		}

		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
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
					// System.out.println(pedestal.getRequiredItem().getCurrentAmount());
					if (index == 4)
						return pedestals;
				}
			}
		}
		return null;
	}
}
