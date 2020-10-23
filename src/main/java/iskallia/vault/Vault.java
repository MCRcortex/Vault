package iskallia.vault;

import iskallia.vault.init.ModCommands;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModFeatures;
import iskallia.vault.init.ModStructures;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
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
	    MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::onCommandRegister);
	    MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::onBiomeLoad);
	    MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::onPlayerLoggedIn);
    }

	public void onCommandRegister(RegisterCommandsEvent event) {
		ModCommands.registerCommands(event.getDispatcher(), event.getEnvironment());
	}

	public void onBiomeLoad(BiomeLoadingEvent event) {
    	if(event.getName().getNamespace().equals(MOD_ID)) {
    		ModFeatures.FEATURES.forEach(feature -> event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, feature));
	    }
	}

	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		ServerWorld serverWorld = player.getServerWorld();
		MinecraftServer server = player.getServer();
		PlayerAbilitiesData.get(serverWorld).getAbilities(player).syncLevelInfo(server);
	}

	public static String sId(String name) {
		return MOD_ID + ":" + name;
	}

	public static ResourceLocation id(String name) {
		return new ResourceLocation(MOD_ID, name);
	}
	
}
