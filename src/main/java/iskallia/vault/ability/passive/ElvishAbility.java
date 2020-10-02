package iskallia.vault.ability.passive;

import iskallia.vault.ability.PlayerAbility;
import net.minecraft.entity.player.PlayerEntity;

public class ElvishAbility extends PlayerAbility {

	public ElvishAbility(int cost) {
		super(cost);
	}

	@Override
	public void tick(PlayerEntity player) {
		player.fallDistance = 0.0F;
	}

}
