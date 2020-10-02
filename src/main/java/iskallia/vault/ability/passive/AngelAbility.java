package iskallia.vault.ability.passive;

import iskallia.vault.ability.PlayerAbility;
import net.minecraft.entity.player.PlayerEntity;

public class AngelAbility extends PlayerAbility {

	public AngelAbility(int cost) {
		super(cost);
	}

	@Override
	public void tick(PlayerEntity player) {
		if(!player.abilities.allowFlying) {
			player.abilities.allowFlying = true;
			player.sendPlayerAbilities();
		}
	}

	@Override
	public void onRemoved(PlayerEntity player) {
		player.abilities.allowFlying = false;
		player.sendPlayerAbilities();
	}

}
