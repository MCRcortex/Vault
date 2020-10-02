package iskallia.vault.init;

import iskallia.vault.gui.screen.AbilityTreeScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModScreens {

    public static void register(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainers.abilityTree, AbilityTreeScreen::new);
    }

}
