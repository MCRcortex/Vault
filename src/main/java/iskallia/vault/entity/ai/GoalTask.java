package iskallia.vault.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;

public abstract class GoalTask<T extends LivingEntity> extends Goal {

	private final T entity;

	public GoalTask(T entity) {
		this.entity = entity;
	}

	public T getEntity() {
		return this.entity;
	}

	public World getWorld() {
		return this.getEntity().world;
	}

}