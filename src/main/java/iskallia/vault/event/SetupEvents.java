package iskallia.vault.event;

import iskallia.vault.Vault;
import iskallia.vault.init.*;
import iskallia.vault.network.ModNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEvents {

    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
        Vault.LOGGER.info("setupClient()");
        ModScreens.register(event);
        ModKeybinds.register(event);
        MinecraftForge.EVENT_BUS.register(KeyboardEvents.class);
    }

    @SubscribeEvent
    public static void setupCommon(final FMLCommonSetupEvent event) {
        Vault.LOGGER.info("setupCommon()");
        ModNetwork.initialize();
        ModConfigs.register();
    }

    @SubscribeEvent
    public static void setupDedicatedServer(final FMLDedicatedServerSetupEvent event) {
        Vault.LOGGER.info("setupDedicatedServer()");
    }

}
