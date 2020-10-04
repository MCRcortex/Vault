package iskallia.vault.event;

import iskallia.vault.init.ModKeybinds;
import iskallia.vault.network.ModNetwork;
import iskallia.vault.network.message.OpenAbilityTreeMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class KeyboardEvents {

    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.currentScreen == null && ModKeybinds.openAbilityTree.isPressed()) {
            ModNetwork.channel.sendToServer(new OpenAbilityTreeMessage());
        }
    }

}
