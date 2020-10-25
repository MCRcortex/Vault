package iskallia.vault.entity;

import iskallia.vault.config.VaultScaleConfig;
import iskallia.vault.init.ModConfigs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Random;

public class EntityScaler {

	public static void scale(MonsterEntity entity, int level, Random random) {
		VaultScaleConfig.Overrides overrides = ModConfigs.VAULT_SCALE.getForLevel(level);

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

}
