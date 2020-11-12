package iskallia.vault.block.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import iskallia.vault.altar.AltarInfusion;
import iskallia.vault.altar.PedestalItem;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.world.data.PlayerVaultAltarData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

public class VaultAltarTileEntity extends TileEntity implements ITickableTileEntity {

	private Map<UUID, PedestalItem[]> playerMap = new HashMap<>();
	private boolean holdingVaultRock = false;
	private int tick = 0;

	public VaultAltarTileEntity() {
		super(ModBlocks.VAULT_ALTAR_TILE_ENTITY);
	}

	public void setHoldingVaultRock(boolean holdingVaultRock) {
		this.holdingVaultRock = holdingVaultRock;
	}

	public boolean isHoldingVaultRock() {
		return holdingVaultRock;
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

		if (!holdingVaultRock) {
			if(!playerMap.isEmpty()) playerMap.clear();
			return;
		}
		
		PlayerVaultAltarData data = PlayerVaultAltarData.get((ServerWorld) world);

		double x = this.getPos().getX();
		double y = this.getPos().getY();
		double z = this.getPos().getZ();

		int range = 32;

		List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
		for (PlayerEntity p : players) {
			if (data.playerExists(p.getUniqueID())) {
				PedestalItem[] items = data.getRequiredItems(p.getUniqueID());
				playerMap.put(p.getUniqueID(), items);
			}
		}

	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		ListNBT list = new ListNBT();
		for (UUID uuid : playerMap.keySet()) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.put(uuid.toString(), AltarInfusion.serialize(playerMap.get(uuid)));
			list.add(nbt);
		}
		compound.put("players", list);
		return super.write(compound);
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		ListNBT list = (ListNBT) nbt.get("players");
		for (INBT compound : list) {
			CompoundNBT c = (CompoundNBT) compound;
			for (String s : c.keySet()) {
				UUID id = UUID.fromString(s);
				PedestalItem[] items = AltarInfusion.deserialize((CompoundNBT) c.get(s));
				playerMap.put(id, items);
			}
		}
		super.read(state, nbt);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = super.getUpdateTag();
		ListNBT list = new ListNBT();
		for (UUID uuid : playerMap.keySet()) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.put(uuid.toString(), AltarInfusion.serialize(playerMap.get(uuid)));
			list.add(nbt);
		}
		tag.put("players", list);
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

	public Map<UUID, PedestalItem[]> getNearbyPlayers() {
		return playerMap;
	}

}
