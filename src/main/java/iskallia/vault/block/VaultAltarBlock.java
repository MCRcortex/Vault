package iskallia.vault.block;

import iskallia.vault.altar.PedestalItem;
import iskallia.vault.block.entity.VaultAltarTileEntity;
import iskallia.vault.block.entity.VaultPedestalTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.world.data.PlayerVaultAltarData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class VaultAltarBlock extends Block {

	public VaultAltarBlock() {
		super(Properties.create(Material.ROCK, MaterialColor.DIAMOND).setRequiresTool().hardnessAndResistance(3f, 3f));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModBlocks.VAULT_ALTAR_TILE_ENTITY.create();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isRemote)
			return ActionResultType.SUCCESS;
		if (handIn != Hand.MAIN_HAND)
			return ActionResultType.SUCCESS;

		VaultAltarTileEntity altar = getAltarTileEntity(worldIn, pos);
		if (altar == null)
			return ActionResultType.SUCCESS;

		if (player.isSneaking()) {
			if (altar.isHoldingVaultRock()) {
				altar.setHoldingVaultRock(false);
				if (!player.inventory.addItemStackToInventory(new ItemStack(ModItems.VAULT_ROCK))) {
					ItemEntity e = new ItemEntity(worldIn, pos.getX() + 0.5d, pos.getY() + 1.5d, pos.getZ() + 0.5d);
					e.setItem(new ItemStack(ModItems.VAULT_ROCK));
					worldIn.addEntity(e);
				}
			}
		}

		ItemStack heldItem = player.getHeldItemMainhand();
		if (heldItem.getItem() != ModItems.VAULT_ROCK) {

			return ActionResultType.SUCCESS;
		}

		PlayerVaultAltarData data = PlayerVaultAltarData.get((ServerWorld) worldIn);
		PedestalItem[] items = ModConfigs.VAULT_PEDESTAL.getRequiredItemsFromConfig();

		if (!data.playerExists(player.getUniqueID()))
			data.getMap().put(player.getUniqueID(), items);

		altar.setHoldingVaultRock(true);
		System.out.println("Holding Rock: " + altar.isHoldingVaultRock());
		altar.update();
		return ActionResultType.SUCCESS;
	}

	@Override
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {

		VaultPedestalTileEntity[] pedestals = getNearbyPedestals((World) blockAccess, pos);
		if (pedestals != null) {
			for (int i = 0; i < pedestals.length; i++) {
				VaultPedestalTileEntity pedestal = pedestals[i];
				PedestalItem item = pedestal.getRequiredItem();
				if (item != null)
					System.out.println(item.getItem());
			}

		}
		return 0;
	}

	private VaultAltarTileEntity getAltarTileEntity(World worldIn, BlockPos pos) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te == null || !(te instanceof VaultAltarTileEntity))
			return null;
		VaultAltarTileEntity altar = (VaultAltarTileEntity) worldIn.getTileEntity(pos);
		return altar;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (worldIn.isRemote)
			return;

		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	/**
	 * Gets an array of 4 pedestals within a bounding box specified by [range]
	 * 
	 * @param world The world to check positions.
	 * @param altar The position of the Vault Altar to search near.
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
