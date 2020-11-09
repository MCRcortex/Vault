package iskallia.vault.block.entity;

import java.util.List;

import javax.annotation.Nullable;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.VectorHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class VaultPedestalTileEntity extends TileEntity implements ITickableTileEntity {

	private ItemStack item = null;
	private int itemCount = 0;

	private int tick = 0;

	public VaultPedestalTileEntity() {
		super(ModBlocks.VAULT_PEDESTAL_TILE_ENTITY);
	}

	public void update() {
		this.markDirty();
		this.world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
	}

	@Override
	public void tick() {
		World world = this.getWorld();
		if (world.isRemote)
			return;

		double x = this.getPos().getX() + 0.5D;
		double y = this.getPos().getY() + 0.5D;
		double z = this.getPos().getZ() + 0.5D;

		int range = 50;
		float speed = 3f; // blocks per second

		speed = speed / 20f;

		List<ItemEntity> entities = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
		for (ItemEntity itemEntity : entities) {

			if (this.getItem() == null)
				break;

			if (this.getItem().isItemEqualIgnoreDurability(itemEntity.getItem())) {
				moveItemTowardPedestal(itemEntity, speed);
				attemptCollectItem(itemEntity);
			}

		}

	}

	private void moveItemTowardPedestal(ItemEntity itemEntity, float speed) {
		Vector3d target = VectorHelper.getVectorFromPos(this.getPos());
		Vector3d current = VectorHelper.getVectorFromPos(itemEntity.getPosition());

		Vector3d velocity = VectorHelper.getMovementVelocity(current, target, speed);

		itemEntity.addVelocity(velocity.x, velocity.y, velocity.z);
	}

	private void attemptCollectItem(ItemEntity itemEntity) {
		BlockPos itemPos = itemEntity.getPosition();

		if (itemPos.distanceSq(getPos()) <= (2 * 2)) {
			this.addCount(itemEntity.getItem().getCount());
			itemEntity.remove();
		}
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

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = super.getUpdateTag();
		if (item != null)
			tag.put("item", item.serializeNBT());
		tag.putInt("itemCount", itemCount);
		return tag;
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		read(state, tag);
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		CompoundNBT tag = pkt.getNbtCompound();
		handleUpdateTag(getBlockState(), tag);
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

	public void addCount(int count) {
		this.itemCount += count;
	}

}
