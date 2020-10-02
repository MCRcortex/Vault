package iskallia.vault.event;

import iskallia.vault.Vault;
import iskallia.vault.init.ModKeybinds;
import iskallia.vault.init.ModScreens;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class InitSetupEvent {

    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
        Vault.LOGGER.info("setupClient()");
        ModScreens.register(event);
        ModKeybinds.register(event);
    }

    @SubscribeEvent
    public static void setupCommon(final FMLCommonSetupEvent event) {
        Vault.LOGGER.info("setupCommon()");
    }

    @SubscribeEvent
    public static void setupDedicatedServer(final FMLDedicatedServerSetupEvent event) {
        Vault.LOGGER.info("setupDedicatedServer()");
    }

}
