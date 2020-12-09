package iskallia.vault.entity;

import iskallia.vault.entity.ai.AOEGoal;
import iskallia.vault.entity.ai.SnowStormGoal;
import iskallia.vault.entity.ai.TeleportGoal;
import iskallia.vault.util.EntityHelper;
import iskallia.vault.world.raid.VaultRaid;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class MonsterEyeEntity extends SlimeEntity implements VaultBoss {

    public boolean shouldBlockSlimeSplit;
    public final ServerBossInfo bossInfo;

    public MonsterEyeEntity(EntityType<? extends SlimeEntity> type, World worldIn) {
        super(type, worldIn);
        EntityHelper.changeSize(this, 2f);
        setSlimeSize(4, false);
        bossInfo = new ServerBossInfo(getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS);
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean attackedRecently) { }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, ArenaFighterEntity.class, false));

        this.goalSelector.addGoal(1, TeleportGoal.builder(this).start(entity -> {
            return entity.getAttackTarget() != null && entity.ticksExisted % 60 == 0;
        }).to(entity -> {
            return entity.getAttackTarget().getPositionVec().add((entity.rand.nextDouble() - 0.5D) * 8.0D, entity.rand.nextInt(16) - 8, (entity.rand.nextDouble() - 0.5D) * 8.0D);
        }).then(entity -> {
            entity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }).build());

        this.goalSelector.addGoal(1, new SnowStormGoal<>(this, 96, 10));
        this.goalSelector.addGoal(1, new AOEGoal<>(this, e -> !(e instanceof ArenaBossEntity)));

        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(100.0D);
    }

    @Override
    public void spawnInTheWorld(VaultRaid raid, ServerWorld world, BlockPos pos) {
        this.setSlimeSize(4, false);
        this.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 0.2D, pos.getZ() + 0.5D, 0.0F, 0.0F);
        world.summonEntity(this);

        this.getTags().add("VaultBoss");
        this.bossInfo.setVisible(true);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0F);
        this.setHealth(100.0F);

        if (raid != null) {
            EntityScaler.scaleVault(this, raid.level + 5, new Random());

            if (raid.playerBossName != null) {
                this.setCustomName(new StringTextComponent(raid.playerBossName));
            }
        }

        world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }

    @Override
    public void remove(boolean keepData) {
        shouldBlockSlimeSplit = true;
        super.remove(keepData);
    }

    @Override
    public int getSlimeSize() {
        return shouldBlockSlimeSplit ? 0 : super.getSlimeSize();
    }

    @Override
    public ServerBossInfo getServerBossInfo() {
        return bossInfo;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isRemote) {
            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        }
    }

    @Override
    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

}
