package iskallia.vault.world.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public class VaultRaid implements INBTSerializable<CompoundNBT> {

	private UUID playerId;
	public MutableBoundingBox box;
	public int ticksLeft = 20 * 60 * 20;

	private VaultRaid() {

	}

	public VaultRaid(UUID playerId, MutableBoundingBox box) {
		this.playerId = playerId;
		this.box = box;
	}

	public UUID getPlayerId() {
		return this.playerId;
	}

	public boolean isComplete() {
		return this.ticksLeft <= 0;
	}

	public void tick() {
		this.ticksLeft--;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putUniqueId("PlayerId", this.playerId);
		nbt.put("Box", this.box.toNBTTagIntArray());
		nbt.putInt("TicksLeft", this.ticksLeft);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.playerId = nbt.getUniqueId("PlayerId");
		this.box = new MutableBoundingBox(nbt.getIntArray("Box"));
		this.ticksLeft = nbt.getInt("TicksLeft");
	}

	public static VaultRaid fromNBT(CompoundNBT nbt) {
		VaultRaid raid = new VaultRaid();
		raid.deserializeNBT(nbt);
		return raid;
	}

}
