package iskallia.vault.block;

import iskallia.vault.altar.RequiredItem;
import iskallia.vault.block.entity.VaultAltarTileEntity;
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
            if (altar.containsVaultRock()) {
                altar.setContainsVaultRock(false);
                if (!player.inventory.addItemStackToInventory(new ItemStack(ModItems.VAULT_ROCK))) {
                    ItemEntity e = new ItemEntity(worldIn, pos.getX() + 0.5d, pos.getY() + 1.5d, pos.getZ() + 0.5d);
                    e.setItem(new ItemStack(ModItems.VAULT_ROCK));
                    worldIn.addEntity(e);
                }
            }
            altar.updateForClient();
            return ActionResultType.SUCCESS;
        }

        ItemStack heldItem = player.getHeldItemMainhand();
        if (heldItem.getItem() != ModItems.VAULT_ROCK) {
            altar.updateForClient();

            return ActionResultType.SUCCESS;
        }

        PlayerVaultAltarData data = PlayerVaultAltarData.get((ServerWorld) worldIn);
        RequiredItem[] items = ModConfigs.VAULT_PEDESTAL.getRequiredItemsFromConfig();

        if (!data.playerExists(player.getUniqueID()))
            data.addPlayer(player.getUniqueID(), items);

        altar.setContainsVaultRock(true);

        heldItem.setCount(heldItem.getCount() - 1);
        altar.updateForClient();
        return ActionResultType.SUCCESS;
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

}
