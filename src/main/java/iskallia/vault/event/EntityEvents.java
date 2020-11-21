package iskallia.vault.event;

import iskallia.vault.Vault;
import iskallia.vault.entity.EntityScaler;
import iskallia.vault.entity.FighterEntity;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.raid.VaultRaid;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {

	@SubscribeEvent
	public static void onEntityTick2(LivingEvent.LivingUpdateEvent event) {
		if(event.getEntity().world.isRemote
				|| !(event.getEntity() instanceof FighterEntity)
				|| event.getEntity().world.getDimensionKey() != Vault.ARENA_KEY)return;

		((FighterEntity)event.getEntity()).enablePersistence();;
	}

	@SubscribeEvent
	public static void onEntityTick(LivingEvent.LivingUpdateEvent event) {
		if(event.getEntity().world.isRemote
				|| !(event.getEntity() instanceof MonsterEntity)
				|| event.getEntity().world.getDimensionKey() != Vault.VAULT_KEY
				|| event.getEntity().getTags().contains("VaultScaled"))return;

		MonsterEntity entity = (MonsterEntity)event.getEntity();
		VaultRaid raid = VaultRaidData.get((ServerWorld) entity.world).getAt(entity.getPosition());
		if(raid == null)return;

		EntityScaler.scale(entity, raid.level, new Random());
		entity.getTags().add("VaultScaled");
		entity.enablePersistence();
	}

	@SubscribeEvent
	public static void onEntitySpawn(LivingSpawnEvent.CheckSpawn event) {
		if(event.getEntity().getEntityWorld().getDimensionKey() == Vault.VAULT_KEY && !event.isSpawner()) {
			event.setCanceled(true);
		} else if(event.getEntity().getEntityWorld().getDimensionKey() == Vault.ARENA_KEY) {
			event.setCanceled(true);
		}
	}

}
