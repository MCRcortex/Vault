package iskallia.vault;

import iskallia.vault.init.ModScreens;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModContainers;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Vault.MOD_ID)
public class Vault {

    public static final String MOD_ID = "the_vault";
    public static final Logger LOGGER = LogManager.getLogger();

    public static RegistryKey<World> WORLD_KEY = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(MOD_ID, "vault"));

    public Vault() {
        ModConfigs.register();
    }

    /* ------------------------------------------------- */

    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
        ModScreens.register(event);
    }

    @SubscribeEvent
    public static void setupCommon(final FMLCommonSetupEvent event) { }

    @SubscribeEvent
    public static void setupDedicatedServer(final FMLDedicatedServerSetupEvent event) { }

    /* ------------------------------------------------- */

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        ModContainers.register(event.getRegistry());
    }

}
