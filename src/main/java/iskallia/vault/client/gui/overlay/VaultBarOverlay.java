package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import iskallia.vault.client.gui.helper.FontHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class VaultBarOverlay {

    public static final ResourceLocation RESOURCE = new ResourceLocation(Vault.MOD_ID, "textures/gui/vault-hud.png");

    @SubscribeEvent
    public static void
    onPreRender(RenderGameOverlayEvent.Pre event) { }

    @SubscribeEvent
    public static void
    onPostRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR)
            return; // Render only on HOTBAR

        MatrixStack matrixStack = event.getMatrixStack();
        Minecraft minecraft = Minecraft.getInstance();
        int midX = minecraft.getMainWindow().getScaledWidth() / 2;
        int bottom = minecraft.getMainWindow().getScaledHeight();

        String text = "123"; // TODO: <-- Fetch from actual AbilityTree
        int textX = midX + 50 - (minecraft.fontRenderer.getStringWidth(text) / 2);
        int textY = bottom - 54;
        int barWidth = 85;
        float expPercentage = 0.4f;// TODO: <-- Fetch from actual AbilityTree

        minecraft.getProfiler().startSection("vaultBar");
        minecraft.getTextureManager().bindTexture(RESOURCE);
        minecraft.ingameGUI.blit(matrixStack,
                midX + 9, bottom - 48,
                1, 1, barWidth, 5);
        minecraft.ingameGUI.blit(matrixStack,
                midX + 9, bottom - 48,
                1, 7, (int) (barWidth * expPercentage), 5);
        FontHelper.drawStringWithBorder(matrixStack,
                text,
                textX, textY,
                0xFF_ffe637, 0x3c3400);

        minecraft.getProfiler().endSection();
    }

}
