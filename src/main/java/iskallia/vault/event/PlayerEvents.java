package iskallia.vault.event;

import iskallia.vault.Vault;
import iskallia.vault.entity.FighterEntity;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.FighterSizeMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {

	public static boolean NATURAL_REGEN_OLD_VALUE = false; //TODO: No static field pls
	public static boolean SHOULD_TICK_END = false;


	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		Entity target = event.getTarget();

		if(!(target instanceof FighterEntity) || target.world.isRemote)return;

		FighterEntity fighter = (FighterEntity)target;
		ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();

		ModNetwork.CHANNEL.sendTo(new FighterSizeMessage(fighter), player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if(event.side == LogicalSide.CLIENT)return;

		if(event.player.world.getDimensionKey() != Vault.VAULT_KEY && !(SHOULD_TICK_END && event.phase != TickEvent.Phase.END))return;

		if(event.phase == TickEvent.Phase.START) {
			NATURAL_REGEN_OLD_VALUE = event.player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
			event.player.world.getGameRules().get(GameRules.NATURAL_REGENERATION).set(false, event.player.getServer());
			SHOULD_TICK_END = true;
		} else if(event.phase == TickEvent.Phase.END) {
			event.player.world.getGameRules().get(GameRules.NATURAL_REGENERATION).set(NATURAL_REGEN_OLD_VALUE, event.player.getServer());
			SHOULD_TICK_END = false;
		}
	}

}
