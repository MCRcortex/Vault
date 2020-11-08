package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class VampirismTalent extends PlayerTalent {

	@Expose private final float leechRatio;

	public VampirismTalent(int cost, float leechRatio) {
		super(cost);
		this.leechRatio = leechRatio;
	}

	public float getLeechRatio() {
		return this.leechRatio;
	}

	public void onDamagedEntity(PlayerEntity player, LivingHurtEvent event) {
		player.heal(event.getAmount() * this.getLeechRatio());
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		if(!(event.getSource().getTrueSource() instanceof ServerPlayerEntity))return;
		ServerPlayerEntity player = (ServerPlayerEntity)event.getSource().getTrueSource();
		TalentTree abilities = PlayerAbilitiesData.get(player.getServerWorld()).getAbilities(player);

		for(TalentNode<?> node: abilities.getNodes()) {
			if(!(node.getAbility() instanceof VampirismTalent))continue;
			VampirismTalent vampirism = (VampirismTalent)node.getAbility();
			vampirism.onDamagedEntity(player, event);
		}
	}

}