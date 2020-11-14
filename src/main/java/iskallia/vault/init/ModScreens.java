package iskallia.vault.init;

import iskallia.vault.client.gui.overlay.AbilitiesOverlay;
import iskallia.vault.client.gui.overlay.HyperBarOverlay;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.client.gui.overlay.VaultRaidOverlay;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModScreens {

    public static void register(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(VaultBarOverlay.class);
        MinecraftForge.EVENT_BUS.register(AbilitiesOverlay.class);
        MinecraftForge.EVENT_BUS.register(VaultRaidOverlay.class);
        MinecraftForge.EVENT_BUS.register(HyperBarOverlay.class);
        ScreenManager.registerFactory(ModContainers.SKILL_TREE_CONTAINER, SkillTreeScreen::new);
    }

}
