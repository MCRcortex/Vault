package iskallia.vault.init;

import iskallia.vault.client.gui.overlay.AbilitiesOverlay;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModScreens {

    public static void register(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(VaultBarOverlay.class);
        MinecraftForge.EVENT_BUS.register(AbilitiesOverlay.class);
        ScreenManager.registerFactory(ModContainers.SKILL_TREE_CONTAINER, SkillTreeScreen::new);
    }

}
