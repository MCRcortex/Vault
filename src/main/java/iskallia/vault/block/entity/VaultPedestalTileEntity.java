package iskallia.vault.block.entity;

import java.util.List;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.VectorHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class VaultPedestalTileEntity extends TileEntity implements ITickableTileEntity {

	private ItemStack item = null;
	private int itemCount = 0;

	private int tick = 0;

	public VaultPedestalTileEntity() {
		super(ModBlocks.VAULT_PEDESTAL_TILE_ENTITY);
	}

	@Override
	public void tick() {
		World world = this.getWorld();
		if (world.isRemote)
			return;

		double x = this.getPos().getX() + 0.5D;
		double y = this.getPos().getY() + 0.5D;
		double z = this.getPos().getZ() + 0.5D;

		int range = 8;
		float speed = 1f; // blocks per second

		speed = speed / 20f;

		List<ItemEntity> entities = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
		for (ItemEntity itemEntity : entities) {

			// pedestal has no item or is the same, move the item entity
			if (this.getItem() == null || this.getItem().isItemEqualIgnoreDurability(itemEntity.getItem()))
				moveItemTowardPedestal(itemEntity, speed);

			if (this.getItem() == null)
				collectItem(itemEntity, true);

			if (this.getItem().isItemEqualIgnoreDurability(itemEntity.getItem()))
				collectItem(itemEntity, false);

		}

	}

	private void moveItemTowardPedestal(ItemEntity itemEntity, float speed) {
		Vector3d target = VectorHelper.getVectorFromPos(this.getPos());
		Vector3d current = VectorHelper.getVectorFromPos(itemEntity.getPosition());

		Vector3d direction = VectorHelper.getDirectionNormalized(target, current);

		Vector3d velocity = VectorHelper.multiply(direction, speed);

		itemEntity.addVelocity(velocity.x, velocity.y, velocity.z);
	}

	private void collectItem(ItemEntity itemEntity, boolean isNew) {
		if (isInRange(itemEntity.getPosition())) {
			if (isNew)
				this.setItem(itemEntity.getItem());

			this.setItemCount(itemCount + itemEntity.getItem().getCount());
			itemEntity.remove();
		}
	}

	private boolean isInRange(BlockPos itemPos) {
		if (itemPos.distanceSq(this.getPos()) <= 1) {
			return true;
		}
		return false;
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
