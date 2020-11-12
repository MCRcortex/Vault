package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import iskallia.vault.client.gui.helper.ConfettiParticles;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class HyperBarOverlay {

    public static final ResourceLocation HUD_RESOURCE = new ResourceLocation(Vault.MOD_ID, "textures/gui/vault-hud.png");

    public static ConfettiParticles confettiParticles = new ConfettiParticles()
            .angleRange(200, 265)
            .quantityRange(60, 80)
            .delayRange(0, 10)
            .lifespanRange(20, 20 * 5)
            .sizeRange(2, 5)
            .speedRange(2, 10);

    public static int currentHype;
    public static int maxHype = 100; // TODO: Fetch from the config?

    @SubscribeEvent
    public static void
    onPostRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR)
            return; // Render only on HOTBAR

        MatrixStack matrixStack = event.getMatrixStack();

        Minecraft minecraft = Minecraft.getInstance();

        int right = minecraft.getMainWindow().getScaledWidth();
        int bottom = minecraft.getMainWindow().getScaledHeight();

        int outerWidth = 33;
        int outerHeight = 96;

        RenderSystem.enableBlend();

        minecraft.getTextureManager().bindTexture(HUD_RESOURCE);

        minecraft.ingameGUI.blit(matrixStack,
                right - outerWidth - 2, bottom - outerHeight - 2,
                1, 68, outerWidth, outerHeight);

        int innerWidth = 21;
        int innerHeight = 72;

        float percentage = (float) currentHype / maxHype;
        int filledHypeHeight = (int) (innerHeight * percentage);

        minecraft.ingameGUI.blit(matrixStack,
                right - innerWidth - 2,
                bottom - filledHypeHeight - 14,
                35, 80,
                innerWidth, filledHypeHeight);

        confettiParticles.spawnedPosition(
                right - 10,
                bottom - 30
        );

        confettiParticles.tick();
        confettiParticles.render(matrixStack);

        if (currentHype >= maxHype) {
            confettiParticles.pop();
            minecraft.getSoundHandler().play(SimpleSound.master(
                    SoundEvents.ENTITY_FIREWORK_ROCKET_TWINKLE,
                    1.0F
            ));
            currentHype -= maxHype;
        }
    }

    @SubscribeEvent
    public static void onMouse(InputEvent.MouseInputEvent event) {
        if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_MIDDLE
                && event.getAction() == GLFW.GLFW_RELEASE) {
            currentHype += 10;
        }
    }

}
