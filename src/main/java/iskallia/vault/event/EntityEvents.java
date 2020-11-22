package iskallia.vault.event;

import iskallia.vault.Vault;
import iskallia.vault.block.VaultDoorBlock;
import iskallia.vault.entity.EntityScaler;
import iskallia.vault.entity.FighterEntity;
import iskallia.vault.init.ModEntities;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.raid.VaultRaid;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {

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
	public static void onEntityTick2(LivingEvent.LivingUpdateEvent event) {
		if(event.getEntity().world.isRemote
				|| !(event.getEntity() instanceof FighterEntity)
				|| event.getEntity().world.getDimensionKey() != Vault.ARENA_KEY)return;

		((FighterEntity)event.getEntity()).enablePersistence();
	}

	@SubscribeEvent
	public static void onEntityTick3(LivingEvent.LivingUpdateEvent event) {
		if(event.getEntity().world.isRemote
				|| !(event.getEntity() instanceof AreaEffectCloudEntity)
				|| event.getEntity().world.getDimensionKey() != Vault.VAULT_KEY
				|| event.getEntity().getTags().contains("vault_door"))return;

		for(int y = 0; y < 2; y++) {
			BlockPos pos = event.getEntityLiving().getPosition().up(y);
			BlockState state = event.getEntityLiving().world.getBlockState(pos);

			if(state.getBlock() == Blocks.IRON_DOOR) {
				BlockState newState = VaultDoorBlock.VAULT_DOORS.get(event.getEntityLiving().world.rand.nextInt(VaultDoorBlock.VAULT_DOORS.size())).getDefaultState()
						.with(DoorBlock.FACING, state.get(DoorBlock.FACING))
						.with(DoorBlock.OPEN, state.get(DoorBlock.OPEN))
						.with(DoorBlock.HINGE, state.get(DoorBlock.HINGE))
						.with(DoorBlock.POWERED, state.get(DoorBlock.POWERED))
						.with(DoorBlock.HALF, state.get(DoorBlock.HALF));

				event.getEntityLiving().world.setBlockState(pos, newState);
			}
		}

		event.getEntityLiving().remove();
	}

	@SubscribeEvent
	public static void onEntityTick4(LivingEvent.LivingUpdateEvent event) {
		if(event.getEntity().world.isRemote
				|| event.getEntity().world.getDimensionKey() != Vault.VAULT_KEY
				|| event.getEntity().getTags().contains("vault_boss"))return;

		Entity entity = event.getEntity();

		FighterEntity boss = ModEntities.FIGHTER.create(event.getEntity().world).changeSize(3.0F);
		boss.setLocationAndAngles(entity.getPosX(), entity.getPosY(), entity.getPosZ(), entity.rotationYaw, entity.rotationPitch);
		((ServerWorld)entity.world).summonEntity(boss);
		boss.getTags().add("VaultBoss");
		entity.remove();
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		if(event.getEntity().world.isRemote
				|| event.getEntity().world.getDimensionKey() != Vault.VAULT_KEY
				|| event.getEntity().getTags().contains("VaultBoss"))return;

		ServerWorld world = (ServerWorld)event.getEntityLiving().world;
		VaultRaid raid = VaultRaidData.get(world).getAt(event.getEntity().getPosition());

		raid.runIfPresent(world, player -> {
			LootContext.Builder builder = (new LootContext.Builder(world)).withRandom(world.rand)
					.withParameter(LootParameters.THIS_ENTITY, event.getEntity())
					.withParameter(LootParameters.field_237457_g_, event.getEntity().getPositionVec())
					.withParameter(LootParameters.DAMAGE_SOURCE, event.getSource())
					.withNullableParameter(LootParameters.KILLER_ENTITY, event.getSource().getTrueSource())
					.withNullableParameter(LootParameters.DIRECT_KILLER_ENTITY, event.getSource().getImmediateSource())
					.withParameter(LootParameters.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());

			LootContext ctx = builder.build(LootParameterSets.ENTITY);

			world.getServer().getLootTableManager().getLootTableFromLocation(Vault.id("chest/boss")).generate(ctx).forEach(stack -> {
				if(!player.addItemStackToInventory(stack)) {
					//TODO: drop the item at spawn
				}
			});

			raid.teleportToStart(world, player);
		});
	}

	@SubscribeEvent
	public static void onEntityDrops(LivingDropsEvent event) {
		if(event.getEntity().world.isRemote
				|| event.getEntity().world.getDimensionKey() != Vault.VAULT_KEY)return;
		event.setCanceled(true);
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
