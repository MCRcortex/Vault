package iskallia.vault.event;

import iskallia.vault.entity.FighterEntity;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.FighterSizeMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		Entity target = event.getTarget();

		if(!(target instanceof FighterEntity) || target.world.isRemote)return;

		FighterEntity fighter = (FighterEntity)target;
		ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();

		ModNetwork.CHANNEL.sendTo(new FighterSizeMessage(fighter), player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
	}

}
