package iskallia.vault.entity;

import iskallia.vault.world.data.ArenaRaidData;
import iskallia.vault.world.raid.ArenaRaid;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.DamageSource;
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
		this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(100.0D);
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
