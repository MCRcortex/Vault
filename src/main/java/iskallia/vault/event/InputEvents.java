package iskallia.vault.event;

import iskallia.vault.client.gui.overlay.AbilitiesOverlay;
import iskallia.vault.init.ModKeybinds;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.AbilityKeyMessage;
import iskallia.vault.network.message.GlobalTimerMessage;
import iskallia.vault.network.message.OpenSkillTreeMessage;
import iskallia.vault.network.message.RaffleServerMessage;
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
        if (minecraft.world == null) return;
        onInput(minecraft, event.getKey(), event.getAction());
    }

    @SubscribeEvent
    public static void onMouse(InputEvent.MouseInputEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.world == null) return;
        onInput(minecraft, event.getButton(), event.getAction());
    }

    private static void onInput(Minecraft minecraft, int key, int action) {
        if (minecraft.currentScreen == null && ModKeybinds.globalTimerKey.isPressed()) {
            ModNetwork.CHANNEL.sendToServer(GlobalTimerMessage.requestUnix());

        } else if (minecraft.currentScreen == null && ModKeybinds.openRaffleScreen.isPressed()) {
            ModNetwork.CHANNEL.sendToServer(RaffleServerMessage.requestRaffle());

        } else if (minecraft.currentScreen == null && ModKeybinds.openAbilityTree.isPressed()) {
            ModNetwork.CHANNEL.sendToServer(new OpenSkillTreeMessage());

        } else if (AbilitiesOverlay.cooldownTicks == 0 && ModKeybinds.abilityKey.getKey().getKeyCode() == key) {
            if (action == GLFW.GLFW_RELEASE) {
                ModNetwork.CHANNEL.sendToServer(new AbilityKeyMessage(true, false, false, false));

            } else if (action == GLFW.GLFW_PRESS) {
                ModNetwork.CHANNEL.sendToServer(new AbilityKeyMessage(false, true, false, false));
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollEvent event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.world == null) return;

        double scrollDelta = event.getScrollDelta();

        if (ModKeybinds.abilityKey.isKeyDown()) {
            if (AbilitiesOverlay.cooldownTicks == 0) {
                if (scrollDelta < 0) {
                    ModNetwork.CHANNEL.sendToServer(new AbilityKeyMessage(false, false, false, true));

                } else {
                    ModNetwork.CHANNEL.sendToServer(new AbilityKeyMessage(false, false, true, false));
                }
            }
            event.setCanceled(true);
        }
    }

}
