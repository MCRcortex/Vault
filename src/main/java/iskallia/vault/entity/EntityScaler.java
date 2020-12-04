package iskallia.vault.entity;

import iskallia.vault.config.ArenaMobsConfig;
import iskallia.vault.config.VaultMobsConfig;
import iskallia.vault.init.ModConfigs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Random;

public class EntityScaler {

	public static void scaleVault(MonsterEntity entity, int level, Random random) {
		VaultMobsConfig.Level overrides = ModConfigs.VAULT_MOBS.getForLevel(level);

		for(EquipmentSlotType slot: EquipmentSlotType.values()) {
			if(slot.getSlotType() == EquipmentSlotType.Group.HAND) {
				if(!entity.getItemStackFromSlot(slot).isEmpty())continue;
			}

			List<Item> pool = overrides.getFor(slot);
			if(pool.isEmpty())return;

			ItemStack loot = new ItemStack(pool.get(random.nextInt(pool.size())));

			for(int i = 0; i < overrides.ENCH_TRIALS; i++) {
				EnchantmentHelper.addRandomEnchantment(random, loot,
						EnchantmentHelper.calcItemStackEnchantability(random, overrides.ENCH_LEVEL, 15, loot), true);
			}

			entity.setItemStackToSlot(slot, loot);
		}
	}

	public static void scaleArena(FighterEntity entity, int months, Random random) {
		ArenaMobsConfig.Level overrides = ModConfigs.ARENA_MOBS.getForLevel(months);
		ArenaMobsConfig.Mob<?> config = entity instanceof ArenaBossEntity ? overrides.BOSS_CONFIG : overrides.FIGHTER_CONFIG;

		for(EquipmentSlotType slot: EquipmentSlotType.values()) {
			List<Item> pool = config.getFor(slot);
			if(pool.isEmpty())return;

			ItemStack loot = new ItemStack(pool.get(random.nextInt(pool.size())));

			for(int i = 0; i < config.ENCH_TRIALS; i++) {
				EnchantmentHelper.addRandomEnchantment(random, loot,
						EnchantmentHelper.calcItemStackEnchantability(random, config.ENCH_LEVEL, 15, loot), true);
			}

			entity.setItemStackToSlot(slot, loot);
		}

		entity.changeSize(entity instanceof ArenaBossEntity ? ModConfigs.ARENA_GENERAL.BOSS_SIZE : config.MOB_SIZE);
	}

}
