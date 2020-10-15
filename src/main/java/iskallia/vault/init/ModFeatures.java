package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.world.gen.ruletest.VaultRuleTest;
import iskallia.vault.world.gen.structure.VaultConfig;
import iskallia.vault.world.gen.structure.VaultPools;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.ArrayList;
import java.util.List;

public class ModFeatures {

	public static List<ConfiguredFeature<?, ?>> FEATURES = new ArrayList<>();

	public static StructureFeature<VaultConfig, ? extends Structure<VaultConfig>> VAULT_FEATURE;
	public static ConfiguredFeature<?, ?> COAL, IRON, GOLD, REDSTONE, LAPIS, DIAMOND;

	public static void register() {
		VAULT_FEATURE = register("vault", ModStructures.VAULT.func_236391_a_(new VaultConfig(() -> VaultPools.START, 8)));
		COAL = registerOre("vault_coal_ore", Blocks.COAL_ORE.getDefaultState(), 8, 20);
		IRON = registerOre("vault_iron_ore", Blocks.IRON_ORE.getDefaultState(), 8, 20);
		GOLD = registerOre("vault_gold_ore", Blocks.GOLD_ORE.getDefaultState(), 8, 20);
		REDSTONE = registerOre("vault_redstone_ore", Blocks.REDSTONE_ORE.getDefaultState(), 8, 20);
		LAPIS = registerOre("vault_lapis_ore", Blocks.LAPIS_ORE.getDefaultState(), 8, 20);
		DIAMOND = registerOre("vault_diamond_ore", Blocks.DIAMOND_ORE.getDefaultState(), 8, 20);
	}

	public static ConfiguredFeature<?, ?> registerOre(String name, BlockState ore, int size, int tries) {
		return register(name, Feature.ORE.withConfiguration(new OreFeatureConfig(VaultRuleTest.INSTANCE, ore, size))
				.func_242733_d(256).func_242728_a().func_242731_b(tries));
	}
	private static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> register(String name, ConfiguredFeature<FC, F> feature) {
		FEATURES.add(feature);
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, Vault.id(name), feature);
	}

	private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, F> register(String name, StructureFeature<FC, F> feature) {
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, Vault.id(name), feature);
	}

}
