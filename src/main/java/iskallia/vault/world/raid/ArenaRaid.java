package iskallia.vault.world.raid;

import iskallia.vault.Vault;
import iskallia.vault.world.gen.structure.ArenaStructure;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;
import java.util.function.Consumer;

public class ArenaRaid implements INBTSerializable<CompoundNBT> {

    public static final int REGION_SIZE = 1 << 11;

    private UUID playerId;
    public MutableBoundingBox box;
    private boolean isComplete;

    public BlockPos start;
    private ArenaSpawner spawner = new ArenaSpawner(this, 3);

    protected ArenaRaid() {

    }

    public ArenaRaid(UUID playerId, MutableBoundingBox box) {
        this.playerId = playerId;
        this.box = box;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public void finish() {
        this.isComplete = true;
    }

    public boolean isComplete() {
        return this.isComplete;
    }

    public BlockPos getCenter() {
        return this.start;
    }

    public void tick(ServerWorld world) {
        if(this.isComplete()) {
            //TODO: stuffs
        }
    }

    public boolean runIfPresent(ServerWorld world, Consumer<ServerPlayerEntity> action) {
        if (world == null) return false;
        ServerPlayerEntity player = (ServerPlayerEntity)world.getPlayerByUuid(this.playerId);
        if (player == null) return false;
        action.accept(player);
        return true;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUniqueId("PlayerId", this.playerId);
        nbt.put("Box", this.box.toNBTTagIntArray());
        nbt.putBoolean("Completed", this.isComplete());

        if(this.start != null) {
            CompoundNBT startNBT = new CompoundNBT();
            startNBT.putInt("x", this.start.getX());
            startNBT.putInt("y", this.start.getY());
            startNBT.putInt("z", this.start.getZ());
            nbt.put("Start", startNBT);
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.playerId = nbt.getUniqueId("PlayerId");
        this.box = new MutableBoundingBox(nbt.getIntArray("Box"));
        this.isComplete = nbt.getBoolean("Completed");

        if(nbt.contains("Start", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT startNBT = nbt.getCompound("Start");
            this.start = new BlockPos(startNBT.getInt("x"), startNBT.getInt("y"), startNBT.getInt("z"));
        }
    }

    public static ArenaRaid fromNBT(CompoundNBT nbt) {
        ArenaRaid raid = new ArenaRaid();
        raid.deserializeNBT(nbt);
        return raid;
    }

    public void teleportToStart(ServerWorld world, ServerPlayerEntity player) {
        if (this.start == null) {
            Vault.LOGGER.warn("No arena start was found.");
            player.teleport(world, this.box.minX + this.box.getXSize() / 2.0F, 256,
                    this.box.minZ + this.box.getZSize() / 2.0F, player.rotationYaw, player.rotationPitch);
            return;
        }

        player.teleport(world, this.start.getX() + 0.5D, this.start.getY() + 0.2D, this.start.getZ() + 0.5D, world.getRandom().nextFloat() * 360.0F, 0.0F);
        player.setOnGround(true);
    }

    public void start(ServerWorld world, ServerPlayerEntity player, ChunkPos chunkPos) {
        loop:
        for(int x = -48; x < 48; x++) {
            for(int z = -48; z < 48; z++) {
                for(int y = 0; y < 48; y++) {
                    BlockPos pos = chunkPos.asBlockPos().add(x, ArenaStructure.START_Y + y, z);
                    if(world.getBlockState(pos).getBlock() != Blocks.CRIMSON_PRESSURE_PLATE)continue;
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    this.start = pos;
                    break loop;
                }
            }
        }

        this.teleportToStart(world, player);
        player.func_242279_ag();
        this.spawner.start(world);
    }

}
