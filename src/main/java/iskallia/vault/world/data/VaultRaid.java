package iskallia.vault.world.data;

import iskallia.vault.Vault;
import iskallia.vault.block.VaultPortalBlock;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.world.gen.structure.VaultStructure;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public class VaultRaid implements INBTSerializable<CompoundNBT> {

	public static final int REGION_SIZE = 1 << 11;

	private UUID playerId;
	public MutableBoundingBox box;
	public int level;
	public int ticksLeft = 20 * 60 * 20;

	public BlockPos start;
	public Direction facing;

	protected VaultRaid() {

	}

	public VaultRaid(UUID playerId, MutableBoundingBox box, int level) {
		this.playerId = playerId;
		this.box = box;
		this.level = level;
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
		nbt.putInt("Level", this.level);
		nbt.putInt("TicksLeft", this.ticksLeft);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.playerId = nbt.getUniqueId("PlayerId");
		this.box = new MutableBoundingBox(nbt.getIntArray("Box"));
		this.level = nbt.getInt("Level");
		this.ticksLeft = nbt.getInt("TicksLeft");
	}

	public static VaultRaid fromNBT(CompoundNBT nbt) {
		VaultRaid raid = new VaultRaid();
		raid.deserializeNBT(nbt);
		return raid;
	}

	public void teleportToStart(ServerWorld world, ServerPlayerEntity player) {
		if(this.start == null) {
			Vault.LOGGER.warn("No vault start was found.");
			player.teleport(world, this.box.minX + this.box.getXSize() / 2.0F, 256,
					this.box.minZ + this.box.getZSize() / 2.0F, player.rotationYaw, player.rotationPitch);
			return;
		}

		player.teleport(world, this.start.getX(), this.start.getY(), this.start.getZ(),
				this.facing == null ? world.getRandom().nextFloat() * 360.0F : this.facing.rotateY().getHorizontalAngle(), 0.0F);
	}

	public void searchForStart(ServerWorld world, ChunkPos chunkPos) {
		for(int x = -48; x < 48; x++) {
			for(int z = -48; z < 48; z++) {
				for(int y = 0; y < 48; y++) {
					BlockPos pos = chunkPos.asBlockPos().add(x, VaultStructure.START_Y + y, z);
					if(world.getBlockState(pos).getBlock() != Blocks.CRIMSON_PRESSURE_PLATE)continue;
					world.setBlockState(pos, Blocks.AIR.getDefaultState());

					this.start = pos;

					for(Direction direction : Direction.Plane.HORIZONTAL) {
						int count = 1;

						while(world.getBlockState(pos.offset(direction, count)).getBlock() == Blocks.WARPED_PRESSURE_PLATE) {
							world.setBlockState(pos.offset(direction, count), Blocks.AIR.getDefaultState());
							count++;
						}

						if(count != 1) {
							makePortal(world, pos, this.facing = direction, 2 + count - 1, 3 + count - 1);
							return;
						}
					}
				}
			}
		}
	}

	public static void makePortal(IWorld world, BlockPos pos, Direction facing, int width, int height) {
		pos = pos.offset(Direction.DOWN).offset(facing.getOpposite());

		for(int y = 0; y < height + 2; y++) {
			world.setBlockState(pos.up(y), Blocks.OBSIDIAN.getDefaultState(), 1);
			world.setBlockState(pos.offset(facing, width + 1).up(y), Blocks.OBSIDIAN.getDefaultState(), 1);

			BlockState state = y == 0 || y == height + 1
					? Blocks.OBSIDIAN.getDefaultState()
					: ModBlocks.VAULT_PORTAL.getDefaultState().with(VaultPortalBlock.AXIS, facing.getAxis());

			for(int x = 1; x < width + 1; x++) {
				world.setBlockState(pos.offset(facing, x).up(y), state, 1);
			}
		}
	}

}
