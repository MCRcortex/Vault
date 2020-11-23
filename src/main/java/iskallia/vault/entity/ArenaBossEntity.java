package iskallia.vault.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

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

}
