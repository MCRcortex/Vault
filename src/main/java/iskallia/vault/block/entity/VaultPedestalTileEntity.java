package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class VaultPedestalTileEntity extends TileEntity implements ITickableTileEntity {

	private ItemStack item;
	private int itemCount;

	public VaultPedestalTileEntity() {
		super(ModBlocks.VAULT_PEDESTAL_TILE_ENTITY);
	}

	@Override
	public void tick() {
		World world = this.getWorld();
		if (world.isRemote)
			return;
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		if (item != null)
			compound.put("item", item.serializeNBT());
		if (itemCount > 0)
			compound.putInt("itemCount", itemCount);
		return super.write(compound);
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		itemCount = nbt.getInt("itemCount");
		CompoundNBT itemNBT = (CompoundNBT) nbt.get("item");
		if (itemNBT != null)
			item = ItemStack.read(itemNBT);
		super.read(state, nbt);
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

}
