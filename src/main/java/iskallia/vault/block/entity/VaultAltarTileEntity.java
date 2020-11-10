package iskallia.vault.block.entity;

import javax.annotation.Nullable;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class VaultAltarTileEntity extends TileEntity implements ITickableTileEntity {

	private int tick = 0;

	public VaultAltarTileEntity() {
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

	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {

		return super.write(compound);
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {

		super.read(state, nbt);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = super.getUpdateTag();

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

}
