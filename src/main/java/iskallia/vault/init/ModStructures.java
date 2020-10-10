package iskallia.vault.init;

import iskallia.vault.world.structure.VaultConfig;
import iskallia.vault.world.structure.VaultStructure;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.Locale;

public class ModStructures {

	public static Structure<VaultConfig> VAULT;

	public static void register() {
		VAULT = register("Vault", new VaultStructure(VaultConfig.CODEC), GenerationStage.Decoration.UNDERGROUND_STRUCTURES);
	}

	private static <T extends Structure<?>> T register(String name, T structure, GenerationStage.Decoration stage) {
		Structure.field_236365_a_.put(name.toLowerCase(Locale.ROOT), structure);
		return Registry.register(Registry.STRUCTURE_FEATURE, name.toLowerCase(Locale.ROOT), structure);
	}

}
