package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.world.gen.decorator.RegionOreFeature;
import iskallia.vault.world.gen.ruletest.VaultRuleTest;
import iskallia.vault.world.gen.structure.ArenaStructure;
import iskallia.vault.world.gen.structure.VaultStructure;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;
import java.util.List;

public class ModFeatures {

    public static List<ConfiguredFeature<?, ?>> FEATURES = new ArrayList<>();

    public static StructureFeature<VaultStructure.Config, ? extends Structure<VaultStructure.Config>> VAULT_FEATURE;
    public static StructureFeature<ArenaStructure.Config, ? extends Structure<ArenaStructure.Config>> ARENA_FEATURE;
    public static ConfiguredFeature<?, ?> VAULT_ORE;

    public static void registerStructureFeatures() {
        VAULT_FEATURE = register("vault", ModStructures.VAULT.func_236391_a_(new VaultStructure.Config(() -> VaultStructure.Pools.START, 7)));
        ARENA_FEATURE = register("arena", ModStructures.ARENA.func_236391_a_(new ArenaStructure.Config(() -> ArenaStructure.Pools.START, 8)));
    }

    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        RegionOreFeature.register(event);
        VAULT_ORE = register("vault_ore", RegionOreFeature.INSTANCE.withConfiguration(new OreFeatureConfig(VaultRuleTest.INSTANCE, Blocks.DIORITE.getDefaultState(), 0)).func_242731_b(1));
    }

    private static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> register(String name, ConfiguredFeature<FC, F> feature) {
        FEATURES.add(feature);
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, Vault.id(name), feature);
    }

    private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, F> register(String name, StructureFeature<FC, F> feature) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, Vault.id(name), feature);
    }

}
