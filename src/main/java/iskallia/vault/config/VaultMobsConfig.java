package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.stream.Collectors;

public class VaultMobsConfig extends Config {

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

	@Expose private List<Level> LEVEL_OVERRIDES = new ArrayList<>();

	public Level getForLevel(int level) {
		for(int i = 0; i < this.LEVEL_OVERRIDES.size(); i++) {
			if(this.LEVEL_OVERRIDES.get(i).MIN_LEVEL > level) {
				if(i == 0)break;
				return this.LEVEL_OVERRIDES.get(i - 1);
			} else if(i == this.LEVEL_OVERRIDES.size() - 1) {
				return this.LEVEL_OVERRIDES.get(i);
			}
		}

		return Level.EMPTY;
	}

	@Override
	public String getName() {
		return "vault_mobs";
	}

	@Override
	protected void reset() {
		this.LEVEL_OVERRIDES.add(new Level(5, 3).add(LEATHER_ARMOR).add(WOODEN_WEAPONS).add(STONE_WEAPONS).enchant(1, 1).mob(EntityType.ZOMBIE, 1));
		this.LEVEL_OVERRIDES.add(new Level(10, 3).add(LEATHER_ARMOR).add(GOLDEN_ARMOR).add(STONE_WEAPONS).add(GOLDEN_WEAPONS).enchant(1, 2).mob(EntityType.ZOMBIE, 4).mob(EntityType.SKELETON, 1));
		this.LEVEL_OVERRIDES.add(new Level(15, 4).add(GOLDEN_ARMOR).add(IRON_ARMOR).add(GOLDEN_WEAPONS).add(IRON_WEAPONS).enchant(2, 1).mob(EntityType.ZOMBIE, 4).mob(EntityType.SKELETON, 2).mob(EntityType.CREEPER, 1));
		this.LEVEL_OVERRIDES.add(new Level(20, 4).add(IRON_ARMOR).add(DIAMOND_ARMOR).add(IRON_WEAPONS).add(DIAMOND_WEAPONS).enchant(2, 2).mob(EntityType.ZOMBIE, 4).mob(EntityType.SKELETON, 3).mob(EntityType.CREEPER, 2));
		this.LEVEL_OVERRIDES.add(new Level(25, 5).add(DIAMOND_ARMOR).add(NETHERITE_ARMOR).add(DIAMOND_WEAPONS).add(NETHERITE_WEAPONS).enchant(3, 1).mob(EntityType.ZOMBIE, 4).mob(EntityType.SKELETON, 3).mob(EntityType.CREEPER, 2).mob(EntityType.SPIDER, 2));
		this.LEVEL_OVERRIDES.add(new Level(30, 6).add(NETHERITE_ARMOR).add(NETHERITE_WEAPONS).enchant(3, 2).mob(EntityType.ZOMBIE, 4).mob(EntityType.ZOMBIE, 4).mob(EntityType.SKELETON, 3).mob(EntityType.CREEPER, 2).mob(EntityType.SPIDER, 2).mob(EntityType.VEX, 2));
	}

	public static class Level {
		public static final Level EMPTY = new Level(0, 0);

		@Expose public int MIN_LEVEL;
		@Expose public Map<String, List<String>> LOOT;
		@Expose public int ENCH_LEVEL;
		@Expose public int ENCH_TRIALS;
		@Expose public int MAX_MOBS;
		@Expose public List<Mob> MOB_POOL;

		public Level(int minLevel, int maxMobs) {
			this.MIN_LEVEL = minLevel;
			this.MAX_MOBS = maxMobs;
			this.LOOT = new LinkedHashMap<>();
			this.MOB_POOL = new ArrayList<>();
		}

		public Level add(Item... items) {
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

		public Level enchant(int level, int trials) {
			this.ENCH_LEVEL = level;
			this.ENCH_TRIALS = trials;
			return this;
		}

		public Level mob(EntityType<? extends LivingEntity> type, int weight) {
			this.MOB_POOL.add(new Mob(type, weight));
			return this;
		}

		public List<Item> getFor(EquipmentSlotType slot) {
			return this.LOOT.getOrDefault(slot.getName(), new ArrayList<>()).stream().map(ResourceLocation::new)
					.map(s -> Registry.ITEM.getOptional(s).orElse(Items.AIR)).collect(Collectors.toList());
		}

		public Mob getRandomMob(Random random) {
			int totalWeight = 0;

			for(Mob mob: this.MOB_POOL) {
				totalWeight += mob.WEIGHT;
			}

			if(totalWeight == 0)return null;
			int index = random.nextInt(totalWeight);
			Mob current = null;

			for(Mob mob: this.MOB_POOL) {
				current = mob;
				index -= mob.WEIGHT;
				if(index < 0)break;
			}

			return current;
		}

	}

	public static class Mob {
		@Expose private String NAME;
		@Expose private int WEIGHT;

		public Mob(EntityType<?> type, int weight) {
			this.NAME = type.getRegistryName().toString();
			this.WEIGHT = weight;
		}

		public EntityType<?> getType() {
			return Registry.ENTITY_TYPE.getOptional(new ResourceLocation(this.NAME)).orElse(EntityType.BAT);
		}
	}

}
