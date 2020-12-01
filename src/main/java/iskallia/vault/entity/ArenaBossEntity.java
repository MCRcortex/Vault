package iskallia.vault.entity;

import iskallia.vault.entity.ai.SnowStormGoal;
import iskallia.vault.entity.ai.TeleportGoal;
import iskallia.vault.world.data.ArenaRaidData;
import iskallia.vault.world.raid.ArenaRaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArenaBossEntity extends FighterEntity {

	public ArenaBossEntity(EntityType<? extends ZombieEntity> type, World world) {
		super(type, world);
		this.setCustomName(new StringTextComponent("Boss"));
	}

	@Override
	protected void applyEntityAI() {
		super.applyEntityAI();
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, ArenaFighterEntity.class, false));

		this.goalSelector.addGoal(1, TeleportGoal.builder(this).start(entity -> {
			return entity.getAttackTarget() != null && entity.ticksExisted % 60 == 0;
		}).to(entity -> {
			return entity.getAttackTarget().getPositionVec().add((entity.rand.nextDouble() - 0.5D) * 8.0D, entity.rand.nextInt(16) - 8, (entity.rand.nextDouble() - 0.5D) * 8.0D);
		}).then(entity -> {
			entity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
		}).build());

		this.goalSelector.addGoal(1, new SnowStormGoal<>(this, 96, 10));

		this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(100.0D);
	}

	private float knockbackAttack(Entity entity) {
		for(int i = 0; i < 20; ++i) {
			double d0 = this.world.rand.nextGaussian() * 0.02D;
			double d1 = this.world.rand.nextGaussian() * 0.02D;
			double d2 = this.world.rand.nextGaussian() * 0.02D;

			((ServerWorld)this.world).spawnParticle(ParticleTypes.POOF,
					entity.getPosX() + this.world.rand.nextDouble() - d0,
					entity.getPosY() + this.world.rand.nextDouble() - d1,
					entity.getPosZ() + this.world.rand.nextDouble() - d2, 10, d0, d1, d2, 1.0D);
		}

		this.world.playSound(null, entity.getPosition(), SoundEvents.ENTITY_IRON_GOLEM_HURT, SoundCategory.HOSTILE, 1.0F, 1.0F);
		return 15.0F;
	}

	public boolean attackEntityAsMob(Entity entity) {
		if(this.rand.nextInt(12) == 0) {
			double old = this.getAttribute(Attributes.ATTACK_KNOCKBACK).getBaseValue();
			this.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(this.knockbackAttack(entity));
			boolean result = super.attackEntityAsMob(entity);
			this.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(old);
			return result;
		} else if(this.rand.nextInt(6) == 0) {
			this.world.setEntityState(this, (byte)4);
			float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
			float f1 = (int)f > 0 ? f / 2.0F + (float)this.rand.nextInt((int)f) : f;
			boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), f1);

			if(flag) {
				entity.setMotion(entity.getMotion().add(0.0D, 0.6F, 0.0D));
				this.applyEnchantments(this, entity);
			}

			this.world.playSound(null, entity.getPosition(), SoundEvents.ENTITY_IRON_GOLEM_HURT, SoundCategory.HOSTILE, 1.0F, 1.0F);
			return flag;
		}

		return super.attackEntityAsMob(entity);
	}

	@SubscribeEvent
	public static void onDamage(LivingDamageEvent event) {
		if(event.getEntity().world.isRemote)return;
		ServerWorld world = (ServerWorld)event.getEntity().world;

		if(!(event.getEntity() instanceof ArenaBossEntity))return;
		ArenaBossEntity boss = (ArenaBossEntity)event.getEntity();

		if(!(event.getSource().getTrueSource() instanceof ArenaFighterEntity))return;
		ArenaFighterEntity fighter = (ArenaFighterEntity)event.getSource().getTrueSource();

		ArenaRaid raid = ArenaRaidData.get(world).getAt(boss.getPosition());
		if(raid == null)return;

		raid.scoreboard.onDamage(fighter, event.getAmount());
	}

}
