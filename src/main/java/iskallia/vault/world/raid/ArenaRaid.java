package iskallia.vault.world.raid;

import iskallia.vault.Vault;
import iskallia.vault.block.VaultCrateBlock;
import iskallia.vault.block.item.PlayerStatueBlockItem;
import iskallia.vault.entity.ArenaBossEntity;
import iskallia.vault.entity.ArenaFighterEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.data.StreamData;
import iskallia.vault.world.gen.structure.ArenaStructure;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ArenaRaid implements INBTSerializable<CompoundNBT> {

    public static final int REGION_SIZE = 1 << 11;

    private UUID playerId;
    public MutableBoundingBox box;
    private boolean isComplete;

    public BlockPos start;
    public ArenaSpawner spawner = new ArenaSpawner(this, ModConfigs.ARENA_GENERAL.BOSS_COUNT);
    public ArenaScoreboard scoreboard = new ArenaScoreboard(this);
    public ReturnInfo returnInfo = new ReturnInfo();

    private int time = ModConfigs.ARENA_GENERAL.TICK_COUNTER;
    private int endDelay = 20 * 15;

    protected ArenaRaid() {

    }

    public ArenaRaid(UUID playerId, MutableBoundingBox box) {
        this.playerId = playerId;
        this.box = box;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public boolean isComplete() {
        return this.isComplete && this.endDelay < 0;
    }

    public BlockPos getCenter() {
        return this.start;
    }

    public void tick(ServerWorld world) {
        this.time++;

        if(this.isComplete) {
            this.endDelay--;
            return;
        }

        if(this.spawner.hasStarted()) {
            boolean bossLeft = this.spawner.bosses.stream().map(world::getEntityByUuid).anyMatch(entity -> entity instanceof ArenaBossEntity);
            boolean fighterLeft = this.spawner.fighters.stream().map(world::getEntityByUuid).anyMatch(entity -> entity instanceof ArenaFighterEntity);

            if(!bossLeft) {
                this.onFighterWin(world);
                this.isComplete = true;
            } else if(!fighterLeft) {
                this.onBossWin(world);
                this.isComplete = true;
            }
        } else if(this.time > 20 * 5) {
            this.spawner.start(world);
        }
    }

    private void onBossWin(ServerWorld world) {
        //TODO: losing screen
    }

    private void onFighterWin(ServerWorld world) {
        //TODO: win screen
        for(int i = 0; i < 128; i++) {
            FireworkRocketEntity firework = new FireworkRocketEntity(world,
                    this.getCenter().getX() + world.getRandom().nextInt(81) - 40,
                    this.getCenter().getY() - 15,
                    this.getCenter().getZ() + world.getRandom().nextInt(81) - 40,
                    new ItemStack(Items.FIREWORK_ROCKET));

            world.summonEntity(firework);
        }

        this.giveLoot(world);
    }

    private void giveLoot(ServerWorld world) {
        NonNullList<ItemStack> stacks = NonNullList.create();

        stacks.add(PlayerStatueBlockItem.forArenaChampion(this.scoreboard.get().entrySet().stream()
                .sorted((o1, o2) -> Float.compare(o2.getValue(), o1.getValue()))
                .map(Map.Entry::getKey).findFirst().orElse("")));

        ItemStack crate = VaultCrateBlock.getCrateWithLoot(ModBlocks.VAULT_CRATE_ARENA, stacks);

        this.runIfPresent(world, playerEntity -> {
            LootContext.Builder builder = (new LootContext.Builder(world)).withRandom(world.rand)
                    .withNullableParameter(LootParameters.THIS_ENTITY, playerEntity)
                    .withNullableParameter(LootParameters.DAMAGE_SOURCE, DamageSource.GENERIC)
                    .withNullableParameter(LootParameters.KILLER_ENTITY, playerEntity)
                    .withNullableParameter(LootParameters.DIRECT_KILLER_ENTITY, playerEntity)
                    .withParameter(LootParameters.LAST_DAMAGE_PLAYER, playerEntity).withLuck(playerEntity.getLuck());

            LootContext ctx = builder.build(LootParameterSets.ENTITY);
            stacks.addAll(world.getServer().getLootTableManager().getLootTableFromLocation(Vault.id("chest/arena")).generate(ctx));

            playerEntity.inventory.addItemStackToInventory(crate);
            world.playSound(null, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
        });
    }

    public void finish(ServerWorld world, ServerPlayerEntity player) {
        if(player != null) {
            this.returnInfo.apply(world.getServer(), player);
            StreamData.get(world).onArenaLeave(world.getServer(), this.playerId);
        }
    }

    public boolean runIfPresent(ServerWorld world, Consumer<ServerPlayerEntity> action) {
        if (world == null) return false;
        ServerPlayerEntity player = (ServerPlayerEntity) world.getPlayerByUuid(this.playerId);
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
        nbt.putInt("Time", this.time);
        nbt.putInt("EndDelay", this.endDelay);

        if (this.start != null) {
            CompoundNBT startNBT = new CompoundNBT();
            startNBT.putInt("x", this.start.getX());
            startNBT.putInt("y", this.start.getY());
            startNBT.putInt("z", this.start.getZ());
            nbt.put("Start", startNBT);
        }

        nbt.put("Scoreboard", this.scoreboard.serializeNBT());
        nbt.put("ReturnInfo", this.returnInfo.serializeNBT());
        nbt.put("Spawner", this.spawner.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.playerId = nbt.getUniqueId("PlayerId");
        this.box = new MutableBoundingBox(nbt.getIntArray("Box"));
        this.isComplete = nbt.getBoolean("Completed");
        this.time = nbt.getInt("Time");
        this.endDelay = nbt.getInt("EndDelay");

        if(nbt.contains("Start", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT startNBT = nbt.getCompound("Start");
            this.start = new BlockPos(startNBT.getInt("x"), startNBT.getInt("y"), startNBT.getInt("z"));
        }

        if(nbt.contains("Scoreboard", Constants.NBT.TAG_COMPOUND)) {
            this.scoreboard.deserializeNBT(nbt.getCompound("Scoreboard"));
        }

        if(nbt.contains("ReturnInfo", Constants.NBT.TAG_COMPOUND)) {
            this.returnInfo.deserializeNBT(nbt.getCompound("ReturnInfo"));
        }

        if(nbt.contains("Spawner", Constants.NBT.TAG_COMPOUND)) {
            this.spawner.deserializeNBT(nbt.getCompound("Spawner"));
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
        this.returnInfo = new ReturnInfo(player);

        loop:
        for (int x = -48; x < 48; x++) {
            for (int z = -48; z < 48; z++) {
                for (int y = 0; y < 48; y++) {
                    BlockPos pos = chunkPos.asBlockPos().add(x, ArenaStructure.START_Y + y, z);
                    if(world.getBlockState(pos).getBlock() != Blocks.CRIMSON_PRESSURE_PLATE)continue;
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    this.start = pos;
                    break loop;
                }
            }
        }

        if(this.start != null) {
            for(int x = -4; x <= 4; x++) {
                for(int z = -4; z <= 4; z++) {
                    for(int y = -4; y <= 4; y++) {
                        world.setBlockState(this.start.add(x, y, z), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }

        this.teleportToStart(world, player);
        player.func_242279_ag();

        player.setGameType(GameType.SPECTATOR);

        this.runIfPresent(world, playerEntity -> {
            StringTextComponent title = new StringTextComponent("The Arena");
            title.setStyle(Style.EMPTY.setColor(Color.fromInt(0xFF_91ee3e)));

            IFormattableTextComponent subtitle = new StringTextComponent("Let the fight begin!");
            subtitle.setStyle(Style.EMPTY.setColor(Color.fromInt(0xFF_91ee3e)));

            StringTextComponent actionBar = new StringTextComponent("You have " + spawner.getFighterCount() + " subscribers on your side.");
            actionBar.setStyle(Style.EMPTY.setColor(Color.fromInt(0xFF_91ee3e)));

            STitlePacket titlePacket = new STitlePacket(STitlePacket.Type.TITLE, title);
            STitlePacket subtitlePacket = new STitlePacket(STitlePacket.Type.SUBTITLE, subtitle);

            playerEntity.connection.sendPacket(titlePacket);
            playerEntity.connection.sendPacket(subtitlePacket);
            playerEntity.sendStatusMessage(actionBar, true);
        });
    }

}
