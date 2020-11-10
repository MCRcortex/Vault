package iskallia.vault.event;

import iskallia.vault.client.gui.overlay.AbilitiesOverlay;
import iskallia.vault.init.ModKeybinds;
import iskallia.vault.network.ModNetwork;
import iskallia.vault.network.message.AbilityKeyMessage;
import iskallia.vault.network.message.OpenSkillTreeMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class InputEvents {

    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.currentScreen == null && ModKeybinds.openAbilityTree.isPressed()) {
            ModNetwork.channel.sendToServer(new OpenSkillTreeMessage());

        } else if (AbilitiesOverlay.cooldownTicks == 0 && ModKeybinds.abilityKey.getKey().getKeyCode() == event.getKey()) {
            System.out.println(event.getKey());

            if (event.getAction() == GLFW.GLFW_RELEASE) {
                ModNetwork.channel.sendToServer(new AbilityKeyMessage(true, false, false, false));

            } else if (event.getAction() == GLFW.GLFW_PRESS) {
                ModNetwork.channel.sendToServer(new AbilityKeyMessage(false, true, false, false));
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollEvent event) {
        double scrollDelta = event.getScrollDelta();

        if (ModKeybinds.abilityKey.isKeyDown()) {
            if (AbilitiesOverlay.cooldownTicks == 0) {
                if (scrollDelta < 0) {
                    ModNetwork.channel.sendToServer(new AbilityKeyMessage(false, false, false, true));

                } else {
                    ModNetwork.channel.sendToServer(new AbilityKeyMessage(false, false, true, false));
                }
            }
            event.setCanceled(true);
        }
    }

}
