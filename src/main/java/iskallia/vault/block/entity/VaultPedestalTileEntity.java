package iskallia.vault.block.entity;

import java.util.List;

import javax.annotation.Nullable;

import iskallia.vault.altar.PedestalItem;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.VectorHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class VaultPedestalTileEntity extends TileEntity implements ITickableTileEntity {

	private PedestalItem required;

	private int tick = 0;

	public VaultPedestalTileEntity() {
		super(ModBlocks.VAULT_PEDESTAL_TILE_ENTITY);
	}

	public PedestalItem getRequiredItem() {
		return required;
	}

	public void setRequiredItem(PedestalItem item) {
		this.required = item;
		update();
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

			if (required.getItem() == null)
				break;

			if (required.getItem().isItemEqualIgnoreDurability(itemEntity.getItem())) {
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
			required.addAmount(itemEntity.getItem().getCount());
			itemEntity.remove();
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.put("pedestalItem", PedestalItem.serializeNBT(required));
		return super.write(compound);
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		if (!nbt.contains("pedestalItem"))
			return;
		CompoundNBT compound = nbt.getCompound("pedestalItem");
		required = PedestalItem.deserializeNBT(compound);
		super.read(state, nbt);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = super.getUpdateTag();
		if (required != null)
			tag.put("pedestalItem", PedestalItem.serializeNBT(required));
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

	public void print() {
		System.out.println(this.getRequiredItem().getItem().getDisplayName() + ": Current = " + this.getRequiredItem().getCurrentAmount() + "- Required = " + this.getRequiredItem().getAmountRequired());
	}

}
