package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.entity.ArenaBossEntity;
import iskallia.vault.entity.ArenaFighterEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArenaMobsConfig extends Config {

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
			if(this.LEVEL_OVERRIDES.get(i).MIN_MONTHS > level) {
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
		return "arena_mobs";
	}

	@Override
	protected void reset() {
		this.LEVEL_OVERRIDES.add(new Level(1,
				new Mob<ArenaBossEntity>(1.0F)
						.add(LEATHER_ARMOR, WOODEN_WEAPONS, STONE_WEAPONS)
						.attribute(Attributes.MAX_HEALTH, 200.0D)
						.enchant(1, 0),
				new Mob<ArenaFighterEntity>(0.5F)
						.add(LEATHER_ARMOR, WOODEN_WEAPONS, STONE_WEAPONS)
						.enchant(1, 0)));

		this.LEVEL_OVERRIDES.add(new Level(3,
				new Mob<ArenaBossEntity>(1.5F)
						.add(LEATHER_ARMOR, CHAINMAIL_ARMOR, STONE_WEAPONS, GOLDEN_WEAPONS)
						.attribute(Attributes.MAX_HEALTH, 200.0D)
						.enchant(1, 1),
				new Mob<ArenaFighterEntity>(0.75F)
						.add(LEATHER_ARMOR, CHAINMAIL_ARMOR, STONE_WEAPONS, GOLDEN_WEAPONS)
						.enchant(1, 1)));

		this.LEVEL_OVERRIDES.add(new Level(5,
				new Mob<ArenaBossEntity>(2.0F)
						.add(GOLDEN_ARMOR, CHAINMAIL_ARMOR, GOLDEN_WEAPONS, IRON_WEAPONS)
						.attribute(Attributes.MAX_HEALTH, 200.0D)
						.enchant(1, 2),
				new Mob<ArenaFighterEntity>(1.0F)
						.add(GOLDEN_ARMOR, CHAINMAIL_ARMOR, GOLDEN_WEAPONS, IRON_WEAPONS)
						.enchant(1, 1)));

		this.LEVEL_OVERRIDES.add(new Level(10,
				new Mob<ArenaBossEntity>(2.5F)
						.add(IRON_ARMOR, GOLDEN_ARMOR, IRON_WEAPONS, DIAMOND_WEAPONS)
						.attribute(Attributes.MAX_HEALTH, 200.0D)
						.enchant(2, 1),
				new Mob<ArenaFighterEntity>(1.25F)
						.add(IRON_ARMOR, GOLDEN_ARMOR, IRON_WEAPONS, DIAMOND_WEAPONS)
						.enchant(2, 1)));

		this.LEVEL_OVERRIDES.add(new Level(15,
				new Mob<ArenaBossEntity>(3.0F)
						.add(IRON_ARMOR, DIAMOND_ARMOR, DIAMOND_WEAPONS)
						.attribute(Attributes.MAX_HEALTH, 200.0D)
						.enchant(2, 2),
				new Mob<ArenaFighterEntity>(1.5F)
						.add(IRON_ARMOR, DIAMOND_ARMOR, DIAMOND_WEAPONS)
						.enchant(2, 1)));

		this.LEVEL_OVERRIDES.add(new Level(15,
				new Mob<ArenaBossEntity>(3.5F)
						.add(DIAMOND_ARMOR, NETHERITE_ARMOR, DIAMOND_WEAPONS, NETHERITE_WEAPONS)
						.attribute(Attributes.MAX_HEALTH, 200.0D)
						.enchant(2, 2),
				new Mob<ArenaFighterEntity>(1.75F)
						.add(DIAMOND_ARMOR, NETHERITE_ARMOR, DIAMOND_WEAPONS, NETHERITE_WEAPONS)
						.enchant(2, 1)));

		this.LEVEL_OVERRIDES.add(new Level(24,
				new Mob<ArenaBossEntity>(4.0F)
						.add(NETHERITE_ARMOR, NETHERITE_WEAPONS)
						.attribute(Attributes.MAX_HEALTH, 200.0D)
						.enchant(3, 1),
				new Mob<ArenaFighterEntity>(2.0F)
						.add(NETHERITE_ARMOR, NETHERITE_WEAPONS)
						.enchant(3, 1)));
	}

	public static class Level {
		public static final Level EMPTY = new Level(0, new Mob<>(0.0F), new Mob<>(0.0F));

		@Expose public int MIN_MONTHS;
		@Expose public Mob<ArenaBossEntity> BOSS_CONFIG;
		@Expose public Mob<ArenaFighterEntity> FIGHTER_CONFIG;

		public Level(int minMonths, Mob<ArenaBossEntity> boss, Mob<ArenaFighterEntity> fighter) {
			this.MIN_MONTHS = minMonths;
			this.BOSS_CONFIG = boss;
			this.FIGHTER_CONFIG = fighter;
		}
	}

	public static class Mob<T extends LivingEntity> {
		@Expose private Map<String, List<String>> LOOT;
		@Expose private List<AttributeOverride> ATTRIBUTES;
		@Expose public int ENCH_LEVEL;
		@Expose public int ENCH_TRIALS;
		@Expose public float MOB_SIZE;

		public Mob(float mobSize) {
			this.LOOT = new HashMap<>();
			this.ATTRIBUTES = new ArrayList<>();
			this.MOB_SIZE = mobSize;
		}

		public Mob<T> add(Item... items) {
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

		public Mob<T> add(Item[]... items) {
			for(Item[] item: items) {
				this.add(item);
			}

			return this;
		}

		public Mob<T> enchant(int level, int trials) {
			this.ENCH_LEVEL = level;
			this.ENCH_TRIALS = trials;
			return this;
		}

		public List<Item> getFor(EquipmentSlotType slot) {
			return this.LOOT.getOrDefault(slot.getName(), new ArrayList<>()).stream().map(ResourceLocation::new)
					.map(s -> Registry.ITEM.getOptional(s).orElse(Items.AIR)).collect(Collectors.toList());
		}

		public Mob<T> attribute(Attribute attribute, double defaultValue) {
			this.ATTRIBUTES.add(new AttributeOverride(attribute, defaultValue));
			return this;
		}

		public T create(World world, EntityType<T> type) {
			T entity = type.create(world);

			if(entity != null) {
				for(AttributeOverride override: ATTRIBUTES) {
					Attribute attribute = Registry.ATTRIBUTE.getOptional(new ResourceLocation(override.NAME)).orElse(null);
					if(attribute == null)continue;
					ModifiableAttributeInstance instance = entity.getAttribute(attribute);
					if(instance == null)continue;
					instance.setBaseValue(override.DEFAULT_VALUE);
				}

				entity.heal(1000000.0F);
			}

			return entity;
		}

		public static class AttributeOverride {
			@Expose private String NAME;
			@Expose private double DEFAULT_VALUE;

			public AttributeOverride(Attribute attribute, double defaultValue) {
				this.NAME = attribute.getRegistryName().toString();
				this.DEFAULT_VALUE = defaultValue;
			}
		}
	}

}
