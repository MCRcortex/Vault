package iskallia.vault;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModFeatures;
import iskallia.vault.init.ModStructures;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Vault.MOD_ID)
public class Vault {

    public static final String MOD_ID = "the_vault";
    public static final Logger LOGGER = LogManager.getLogger();

    public static RegistryKey<World> WORLD_KEY = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(MOD_ID, "vault"));

    public Vault() {
        ModConfigs.register();
	    ModStructures.register();
	    ModFeatures.register();

	    MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);
	    MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::biomeModification);
    }

	//todo: remove
	public void biomeModification(final BiomeLoadingEvent event) {
		event.getGeneration().withStructure(ModFeatures.VAULT_FEATURE);
	}

    //TODO: remove
	public void addDimensionalSpacing(final WorldEvent.Load event) {
		if(event.getWorld() instanceof ServerWorld) {
			ServerWorld serverWorld = (ServerWorld)event.getWorld();

			if(serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator) {
				return;
			}

			serverWorld.getChunkProvider().getChunkGenerator().func_235957_b_().func_236195_a_()
					.put(ModStructures.VAULT, new StructureSeparationSettings(12, 0, 1234));
		}
	}

	public static String sId(String name) {
		return MOD_ID + ":" + name;
	}

	public static ResourceLocation id(String name) {
		return new ResourceLocation(MOD_ID, name);
	}

}
