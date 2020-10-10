package iskallia.vault.init;

import iskallia.vault.world.structure.VaultConfig;
import iskallia.vault.world.structure.VaultPools;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;

public class ModFeatures {

	public static StructureFeature<VaultConfig, ? extends Structure<VaultConfig>> VAULT_FEATURE;

	public static void register() {
		VAULT_FEATURE = register("vault", ModStructures.VAULT.func_236391_a_(new VaultConfig(() -> VaultPools.START, 8)));
	}

	private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, F> register(String name, StructureFeature<FC, F> feature) {
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, name, feature);
	}

}
