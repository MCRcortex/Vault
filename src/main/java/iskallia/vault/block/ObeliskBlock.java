package iskallia.vault.block;

import iskallia.vault.client.gui.overlay.VaultRaidOverlay;
import iskallia.vault.config.VaultMobsConfig;
import iskallia.vault.entity.ArenaBossEntity;
import iskallia.vault.entity.EntityScaler;
import iskallia.vault.entity.FighterEntity;
import iskallia.vault.entity.VaultBoss;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModEntities;
import iskallia.vault.item.ObeliskInscriptionItem;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.raid.VaultRaid;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ObeliskBlock extends Block {

    public static final IntegerProperty COMPLETION = IntegerProperty.create("completion", 0, 4);

    public ObeliskBlock() {
        super(Properties.create(Material.ROCK).sound(SoundType.METAL).hardnessAndResistance(-1.0F, 3600000.0F).noDrops());
        this.setDefaultState(this.stateContainer.getBaseState().with(COMPLETION, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.makeCuboidShape(4f, 0f, 4f, 12f, 32f, 12f);
//        return super.getShape(state, worldIn, pos, context);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack heldStack = player.getHeldItem(hand);

        if (heldStack.getItem() instanceof ObeliskInscriptionItem) {
            if (!player.isCreative()) {
                heldStack.shrink(1);
            }
        } else {
            return ActionResultType.PASS;
        }

        BlockState newState = state.with(COMPLETION, MathHelper.clamp(state.get(COMPLETION) + 1, 0, 4));
        world.setBlockState(pos, newState);

        if (world.isRemote) {
            if (newState.get(COMPLETION) == 4)
                startBossLoop();

            return ActionResultType.SUCCESS;
        }

        this.spawnParticles(world, pos);

        if (newState.get(COMPLETION) == 4) {
            VaultRaid raid = VaultRaidData.get((ServerWorld) world).getAt(pos);

            if (raid != null && raid.playerBossName != null && !raid.playerBossName.isEmpty()) {
                spawnSubscriberBoss(raid, (ServerWorld) world, pos);
            } else {
                spawnRandomBoss(raid, (ServerWorld) world, pos);
                world.setBlockState(pos, Blocks.AIR.getDefaultState());

            }
        }

        return ActionResultType.SUCCESS;
    }

    public void spawnSubscriberBoss(VaultRaid raid, ServerWorld world, BlockPos pos) {
        ArenaBossEntity boss = ModEntities.ARENA_BOSS.create(world);
        boss.changeSize(2.0F);
        boss.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 0.2D, pos.getZ() + 0.5D, 0.0F, 0.0F);
        world.summonEntity(boss);

        boss.getTags().add("VaultBoss");
        boss.bossInfo.setVisible(true);
        boss.setCustomName(new StringTextComponent("Boss"));

        if (raid != null) {
            EntityScaler.scaleVault(boss, raid.level, new Random());
            VaultMobsConfig.Level override = ModConfigs.VAULT_MOBS.getForLevel(raid.level);

            boss.getAttribute(Attributes.MAX_HEALTH).setBaseValue(override.BOSS_HEALTH);
            boss.setHealth((float)override.BOSS_HEALTH);

            boss.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(override.BOSS_SPEED);
            boss.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(override.BOSS_DAMAGE);
            boss.getAttribute(Attributes.ARMOR).setBaseValue(override.BOSS_ARMOR);

            if (raid.playerBossName != null) {
                boss.setCustomName(new StringTextComponent(raid.playerBossName));
            }
        }
    }

    public void spawnRandomBoss(VaultRaid raid, ServerWorld world, BlockPos pos) {
        EntityType<? extends VaultBoss>[] bossPool = new EntityType[] {
                ModEntities.BOOGIEMAN,
                ModEntities.BLUE_BLAZE,
                ModEntities.ROBOT,
                ModEntities.MONSTER_EYE,
        };

        VaultBoss boss = bossPool[world.rand.nextInt(bossPool.length)].create(world);

        if (boss == null) {
            // TODO: Wut? How da hell?
            return;
        }

        boss.spawnInTheWorld(raid, world, pos);
        boss.getServerBossInfo().setVisible(true);
    }

    @OnlyIn(Dist.CLIENT)
    private void startBossLoop() {
        VaultRaidOverlay.bossSummoned = true;
    }

    private void spawnParticles(World world, BlockPos pos) {
        for (int i = 0; i < 20; ++i) {
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;

            ((ServerWorld) world).spawnParticle(ParticleTypes.POOF,
                    pos.getX() + world.rand.nextDouble() - d0,
                    pos.getY() + world.rand.nextDouble() - d1,
                    pos.getZ() + world.rand.nextDouble() - d2, 10, d0, d1, d2, 1.0D);
        }

        world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(COMPLETION);
    }

}
