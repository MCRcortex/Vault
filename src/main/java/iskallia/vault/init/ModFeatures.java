package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.world.gen.decorator.RegionOreFeature;
import iskallia.vault.world.gen.ruletest.VaultRuleTest;
import iskallia.vault.world.gen.structure.VaultConfig;
import iskallia.vault.world.gen.structure.VaultPools;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.ArrayList;
import java.util.List;

public class ModFeatures {

	public static List<ConfiguredFeature<?, ?>> FEATURES = new ArrayList<>();

	public static StructureFeature<VaultConfig, ? extends Structure<VaultConfig>> VAULT_FEATURE;
	public static ConfiguredFeature<?, ?> VAULT_ORE;

	public static void register() {
		VAULT_FEATURE = register("vault", ModStructures.VAULT.func_236391_a_(new VaultConfig(() -> VaultPools.START, 5)));
		VAULT_ORE = registerOre("vault_ore", 128);
	}

	public static ConfiguredFeature<?, ?> registerOre(String name, int tries) {
		return register(name, RegionOreFeature.INSTANCE.withConfiguration(new OreFeatureConfig(VaultRuleTest.INSTANCE,
				Blocks.DIORITE.getDefaultState(), 0)).func_242733_d(256).func_242728_a().func_242731_b(tries));
	}

	private static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> register(String name, ConfiguredFeature<FC, F> feature) {
		FEATURES.add(feature);
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, Vault.id(name), feature);
	}

	private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, F> register(String name, StructureFeature<FC, F> feature) {
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, Vault.id(name), feature);
	}

}
