package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import iskallia.vault.client.gui.helper.ConfettiParticles;
import iskallia.vault.init.ModSounds;
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
    public static int maxHype = 1; // Will be synced with Server Config

    @SubscribeEvent
    public static void
    onPostRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR)
            return; // Render only on HOTBAR

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.world == null || minecraft.world.getDimensionKey() == Vault.ARENA_KEY) {
            return;
        }

        MatrixStack matrixStack = event.getMatrixStack();

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

        float percentage = Math.min(1.0f, (float) currentHype / maxHype);
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
                    ModSounds.CONFETTI_SFX,
                    1.0F
            ));
            currentHype -= maxHype;
        }
    }

}
