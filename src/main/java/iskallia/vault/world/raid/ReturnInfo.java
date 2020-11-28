package iskallia.vault.world.raid;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

public class ReturnInfo implements INBTSerializable<CompoundNBT> {

	private Vector3d position;
	private float yaw;
	private float pitch;
	private GameType gamemode;

	public ReturnInfo() {
		this(Vector3d.ZERO, 0.0F, 0.0F, GameType.NOT_SET);
	}

	public ReturnInfo(ServerPlayerEntity player) {
		this(player.getPositionVec(), player.rotationYaw, player.rotationPitch, player.interactionManager.getGameType());
	}

	public ReturnInfo(Vector3d position, float yaw, float pitch, GameType gamemode) {
		this.position = position;
		this.yaw = yaw;
		this.pitch = pitch;
		this.gamemode = gamemode;
	}

	public void apply(ServerWorld world, ServerPlayerEntity player) {
		player.teleport(world, this.position.x, this.position.y, this.position.z, this.yaw, this.pitch);
		player.setGameType(this.gamemode);
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putDouble("PosX", this.position.x);
		nbt.putDouble("PosY", this.position.y);
		nbt.putDouble("PosZ", this.position.z);
		nbt.putFloat("Yaw", this.yaw);
		nbt.putFloat("Pitch", this.pitch);
		nbt.putInt("Gamemode", this.gamemode.ordinal());
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.position = new Vector3d(nbt.getDouble("PosX"), nbt.getDouble("PosY"), nbt.getDouble("PosZ"));
		this.yaw = nbt.getFloat("Yaw");
		this.pitch = nbt.getFloat("Pitch");
		this.gamemode = GameType.getByID(nbt.getInt("Gamemode"));
	}

}
