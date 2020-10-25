package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VaultScaleConfig extends Config {

	public static final Item[] LEATHER_ARMOR = { Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE,
			Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS };
	public static final Item[] GOLDEN_ARMOR = { Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE,
			Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS };
	public static final Item[] CHAINMAIL_ARMOR = { Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE,
			Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS };
	public static final Item[] IRON_ARMOR = { Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS,
			Items.IRON_BOOTS };
	public static final Item[] DIAMOND_ARMOR = { Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE,
			Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS };
	public static final Item[] NETHERITE_ARMOR = { Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE,
			Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS };

	public static final Item[] WOODEN_WEAPONS = { Items.WOODEN_SWORD, Items.WOODEN_AXE, Items.WOODEN_PICKAXE,
			Items.WOODEN_SHOVEL, Items.WOODEN_HOE };
	public static final Item[] STONE_WEAPONS = { Items.STONE_SWORD, Items.STONE_AXE, Items.STONE_PICKAXE,
			Items.STONE_SHOVEL, Items.STONE_HOE };
	public static final Item[] GOLDEN_WEAPONS = { Items.GOLDEN_SWORD, Items.GOLDEN_AXE, Items.GOLDEN_PICKAXE,
			Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE };
	public static final Item[] IRON_WEAPONS = { Items.IRON_SWORD, Items.IRON_AXE, Items.IRON_PICKAXE, Items.IRON_SHOVEL,
			Items.IRON_HOE };
	public static final Item[] DIAMOND_WEAPONS = { Items.DIAMOND_SWORD, Items.DIAMOND_AXE, Items.DIAMOND_PICKAXE,
			Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE };
	public static final Item[] NETHERITE_WEAPONS = { Items.NETHERITE_SWORD, Items.NETHERITE_AXE, Items.NETHERITE_PICKAXE,
			Items.NETHERITE_SHOVEL, Items.NETHERITE_HOE };

	@Expose private List<Overrides> OVERRIDES = new ArrayList<>();

	public Overrides getForLevel(int level) {
		for(int i = 0; i < this.OVERRIDES.size(); i++) {
			if(this.OVERRIDES.get(i).MIN_LEVEL > level) {
				if(i == 0)break;
				return this.OVERRIDES.get(i - 1);
			} else if(i == this.OVERRIDES.size() - 1) {
				return this.OVERRIDES.get(i);
			}
		}

		return Overrides.EMPTY;
	}

	@Override
	public String getName() {
		return "vault_scale";
	}

	@Override
	protected void reset() {
		this.OVERRIDES.add(new Overrides(5).add(LEATHER_ARMOR).add(WOODEN_WEAPONS).add(STONE_WEAPONS).enchant(1, 1));
		this.OVERRIDES.add(new Overrides(10).add(LEATHER_ARMOR).add(GOLDEN_ARMOR).add(STONE_WEAPONS).add(GOLDEN_WEAPONS).enchant(1, 2));
		this.OVERRIDES.add(new Overrides(15).add(GOLDEN_ARMOR).add(IRON_ARMOR).add(GOLDEN_WEAPONS).add(IRON_WEAPONS).enchant(2, 1));
		this.OVERRIDES.add(new Overrides(20).add(IRON_ARMOR).add(DIAMOND_ARMOR).add(IRON_WEAPONS).add(DIAMOND_WEAPONS).enchant(2, 2));
		this.OVERRIDES.add(new Overrides(25).add(DIAMOND_ARMOR).add(NETHERITE_ARMOR).add(DIAMOND_WEAPONS).add(NETHERITE_WEAPONS).enchant(3, 1));
		this.OVERRIDES.add(new Overrides(30).add(NETHERITE_ARMOR).add(NETHERITE_WEAPONS).enchant(3, 2));
	}

	public static class Overrides {
		public static final Overrides EMPTY = new Overrides(0);

		@Expose public int MIN_LEVEL;
		@Expose public Map<String, List<String>> LOOT;
		@Expose public int ENCH_LEVEL;
		@Expose public int ENCH_TRIALS;

		public Overrides(int minLevel) {
			this.MIN_LEVEL = minLevel;
			this.LOOT = new LinkedHashMap<>();
		}

		public Overrides add(Item... items) {
			for(Item item: items) {
				if(item instanceof ArmorItem) {
					this.LOOT.computeIfAbsent(((ArmorItem)item).getEquipmentSlot().getName(), s -> new ArrayList<>()).add(item.getRegistryName().toString());
				} else {
					this.LOOT.computeIfAbsent(EquipmentSlotType.MAINHAND.getName(), s -> new ArrayList<>()).add(item.getRegistryName().toString());
					this.LOOT.computeIfAbsent(EquipmentSlotType.OFFHAND.getName(), s -> new ArrayList<>()).add(item.getRegistryName().toString());
				}
			}

			return this;
		}

		public Overrides enchant(int level, int trials) {
			this.ENCH_LEVEL = level;
			this.ENCH_TRIALS = trials;
			return this;
		}

		public List<Item> getFor(EquipmentSlotType slot) {
			return this.LOOT.getOrDefault(slot.getName(), new ArrayList<>()).stream().map(ResourceLocation::new)
					.map(s -> Registry.ITEM.getOptional(s).orElse(Items.AIR)).collect(Collectors.toList());
		}
	}

}
