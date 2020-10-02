package iskallia.vault.init;

import iskallia.vault.Vault;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.event.KeyEvent;

@OnlyIn(Dist.CLIENT)
public class ModKeybinds {

    public static KeyBinding openAbilityTree;

    public static void register(final FMLClientSetupEvent event) {
        openAbilityTree = createKeyBinding("open_ability_tree", KeyEvent.VK_H, "key.categories.inventory");

        ClientRegistry.registerKeyBinding(openAbilityTree);
    }

    private static KeyBinding createKeyBinding(String name, int key, String category) {
        return new KeyBinding(
                "key." + Vault.MOD_ID + "." + name,
                key,
                category
        );
    }
}
