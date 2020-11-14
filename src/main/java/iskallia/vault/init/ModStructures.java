package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.world.gen.structure.VaultConfig;
import iskallia.vault.world.gen.structure.VaultStructure;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Locale;

public class ModStructures {

    public static Structure<VaultConfig> VAULT;

    public static void register(RegistryEvent.Register<Structure<?>> event) {
        VAULT = register(event.getRegistry(), "Vault", new VaultStructure(VaultConfig.CODEC), GenerationStage.Decoration.UNDERGROUND_STRUCTURES);
    }

    private static <T extends Structure<?>> T register(IForgeRegistry<Structure<?>> registry, String name, T structure, GenerationStage.Decoration stage) {
        Structure.field_236365_a_.put(name.toLowerCase(Locale.ROOT), structure);
        structure.setRegistryName(Vault.id(name.toLowerCase(Locale.ROOT)));
        registry.register(structure);
        return structure;
    }

}
