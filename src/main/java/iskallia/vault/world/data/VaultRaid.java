package iskallia.vault.world.data;

import iskallia.vault.Vault;
import iskallia.vault.block.VaultPortalBlock;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.network.ModNetwork;
import iskallia.vault.network.message.VaultRaidTickMessage;
import iskallia.vault.util.NetcodeUtils;
import iskallia.vault.world.gen.structure.VaultStructure;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.UUID;
import java.util.function.Consumer;

public class VaultRaid implements INBTSerializable<CompoundNBT> {

    public static final int REGION_SIZE = 1 << 11;

    private UUID playerId;
    public MutableBoundingBox box;
    public int level;
    public int ticksLeft = 20 * 60 * 5;

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

    public void tick(ServerWorld world) {
        this.ticksLeft--;

        this.syncTicksLeft(world.getServer());

        if (this.ticksLeft <= 0) {
            this.runIfPresent(world, playerEntity -> {
                playerEntity.sendMessage(new StringTextComponent("Time has run out!").mergeStyle(TextFormatting.GREEN), this.playerId);
                this.teleportToStart(world, playerEntity);
            });
        } else if (this.ticksLeft <= 100 && this.ticksLeft % 20 == 0) {
            this.runIfPresent(world, playerEntity -> {
                int s = this.ticksLeft / 20;
                playerEntity.sendMessage(new StringTextComponent("Teleporting in " + s + (s == 1 ? " second..." : " seconds...")).mergeStyle(TextFormatting.GREEN), this.playerId);
            });
        }
    }

    public boolean runIfPresent(ServerWorld world, Consumer<ServerPlayerEntity> action) {
        if (world == null) return false;
        ServerPlayerEntity player = (ServerPlayerEntity) world.getPlayerByUuid(this.playerId);
        if (player == null) return false;
        action.accept(player);
        return true;
    }

    public void syncTicksLeft(MinecraftServer server) {
        NetcodeUtils.runIfPresent(server, this.playerId, player -> {
            ModNetwork.channel.sendTo(
                    new VaultRaidTickMessage(this.ticksLeft),
                    player.connection.netManager,
                    NetworkDirection.PLAY_TO_CLIENT
            );
        });
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
        if (this.start == null) {
            Vault.LOGGER.warn("No vault start was found.");
            player.teleport(world, this.box.minX + this.box.getXSize() / 2.0F, 256,
                    this.box.minZ + this.box.getZSize() / 2.0F, player.rotationYaw, player.rotationPitch);
            return;
        }

        player.teleport(world, this.start.getX() + 0.5D, this.start.getY() + 0.2D, this.start.getZ() + 0.5D,
                this.facing == null ? world.getRandom().nextFloat() * 360.0F : this.facing.rotateY().getHorizontalAngle(), 0.0F);

        player.setOnGround(true);
    }

    public void start(ServerWorld world, ServerPlayerEntity player, ChunkPos chunkPos) {
        loop:
        for (int x = -48; x < 48; x++) {
            for (int z = -48; z < 48; z++) {
                for (int y = 0; y < 48; y++) {
                    BlockPos pos = chunkPos.asBlockPos().add(x, VaultStructure.START_Y + y, z);
                    if (world.getBlockState(pos).getBlock() != Blocks.CRIMSON_PRESSURE_PLATE) continue;
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());

                    this.start = pos;

                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        int count = 1;

                        while (world.getBlockState(pos.offset(direction, count)).getBlock() == Blocks.WARPED_PRESSURE_PLATE) {
                            world.setBlockState(pos.offset(direction, count), Blocks.AIR.getDefaultState());
                            count++;
                        }

                        if (count != 1) {
                            makePortal(world, pos, this.facing = direction, count, count + 1);
                            break loop;
                        }
                    }
                }
            }
        }

        this.teleportToStart(world, player);
        player.func_242279_ag();

        this.runIfPresent(world, playerEntity -> {
            float min = this.ticksLeft / (20.0F * 60.0F);
            playerEntity.sendMessage(new StringTextComponent("Welcome to the Vault!").mergeStyle(TextFormatting.GREEN), this.playerId);
            playerEntity.sendMessage(new StringTextComponent("You have " + min + " minutes to complete the raid.").mergeStyle(TextFormatting.GREEN), this.playerId);
            playerEntity.sendMessage(new StringTextComponent("Good luck ").append(player.getName()).append(new StringTextComponent("!")).mergeStyle(TextFormatting.GREEN), this.playerId);
        });
    }

    public static void makePortal(IWorld world, BlockPos pos, Direction facing, int width, int height) {
        Block[] blocks = {
                Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
        };

        pos = pos.offset(Direction.DOWN).offset(facing.getOpposite());

        for (int y = 0; y < height + 2; y++) {
            world.setBlockState(pos.up(y), blocks[world.getRandom().nextInt(blocks.length)].getDefaultState(), 1);
            world.setBlockState(pos.offset(facing, width + 1).up(y), blocks[world.getRandom().nextInt(blocks.length)].getDefaultState(), 1);

            BlockState state = y == 0 || y == height + 1
                    ? blocks[world.getRandom().nextInt(blocks.length)].getDefaultState()
                    : ModBlocks.VAULT_PORTAL.getDefaultState().with(VaultPortalBlock.AXIS, facing.getAxis());

            for (int x = 1; x < width + 1; x++) {
                world.setBlockState(pos.offset(facing, x).up(y), state, 1);
            }
        }
    }

}
