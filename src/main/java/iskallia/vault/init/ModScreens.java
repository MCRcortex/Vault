package iskallia.vault.init;

import iskallia.vault.client.gui.overlay.*;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import iskallia.vault.client.gui.screen.VaultCrateScreen;
import iskallia.vault.client.gui.screen.VendingMachineScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModScreens {

    public static void register(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainers.SKILL_TREE_CONTAINER, SkillTreeScreen::new);
        ScreenManager.registerFactory(ModContainers.VAULT_CRATE_CONTAINER, VaultCrateScreen::new);
        ScreenManager.registerFactory(ModContainers.VENDING_MACHINE_CONTAINER, VendingMachineScreen::new);
    }

    public static void registerOverlays() {
        MinecraftForge.EVENT_BUS.register(VaultBarOverlay.class);
        MinecraftForge.EVENT_BUS.register(AbilitiesOverlay.class);
        MinecraftForge.EVENT_BUS.register(VaultRaidOverlay.class);
        MinecraftForge.EVENT_BUS.register(HyperBarOverlay.class);
        MinecraftForge.EVENT_BUS.register(ArenaScoreboardOverlay.class);
        MinecraftForge.EVENT_BUS.register(GiftBombOverlay.class);
    }

}
