package iskallia.vault.client.gui.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

public class UIHelper {

    public static final ResourceLocation UI_RESOURCE = new ResourceLocation(Vault.MOD_ID, "textures/gui/ability-tree.png");

    public static void
    renderOverflowHidden(MatrixStack matrixStack,
                         Consumer<MatrixStack> backgroundRenderer,
                         Consumer<MatrixStack> innerRenderer) {
        matrixStack.push();
        RenderSystem.enableDepthTest();
        matrixStack.translate(0, 0, 950);
        RenderSystem.colorMask(false, false, false, false);
        AbstractGui.fill(matrixStack, 4680, 2260, -4680, -2260, 0xff_000000);
        RenderSystem.colorMask(true, true, true, true);
        matrixStack.translate(0, 0, -950);
        RenderSystem.depthFunc(GL11.GL_GEQUAL);
        backgroundRenderer.accept(matrixStack);
        RenderSystem.depthFunc(GL11.GL_LEQUAL);

        innerRenderer.accept(matrixStack);

        RenderSystem.depthFunc(GL11.GL_GEQUAL);
        matrixStack.translate(0, 0, -950);
        RenderSystem.colorMask(false, false, false, false);
        AbstractGui.fill(matrixStack, 4680, 2260, -4680, -2260, 0xff_000000);
        RenderSystem.colorMask(true, true, true, true);
        matrixStack.translate(0, 0, 950);
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.disableDepthTest();
        matrixStack.pop();
    }

    public static void
    renderContainerBorder(AbstractGui gui, MatrixStack matrixStack,
                          Rectangle screenBounds,
                          int u, int v,
                          int lw, int rw, int th, int bh,
                          int contentColor) {
        int width = screenBounds.getWidth();
        int height = screenBounds.getHeight();
        renderContainerBorder(gui, matrixStack,
                screenBounds.x0,
                screenBounds.y0,
                width, height,
                u, v,
                lw, rw, th, bh,
                contentColor);
    }

    public static void
    renderContainerBorder(AbstractGui gui, MatrixStack matrixStack,
                          int x, int y,
                          int width, int height,
                          int u, int v,
                          int lw, int rw, int th, int bh,
                          int contentColor) {
        int horizontalGap = width - lw - rw;
        int verticalGap = height - th - bh;

        if (contentColor != 0x00_000000) { // <-- Representing absolute transparency
            AbstractGui.fill(matrixStack, x + lw, y + th,
                    x + lw + horizontalGap, y + th + verticalGap,
                    contentColor);
        }

        gui.blit(matrixStack,
                x, y,
                u, v,
                lw, th);

        gui.blit(matrixStack,
                x + lw + horizontalGap, y,
                u + lw + 3, v,
                rw, th);

        gui.blit(matrixStack,
                x, y + th + verticalGap,
                u, v + th + 3,
                lw, bh);

        gui.blit(matrixStack,
                x + lw + horizontalGap, y + th + verticalGap,
                u + lw + 3, v + th + 3,
                rw, bh);

        matrixStack.push();
        matrixStack.translate(x + lw, y, 0);
        matrixStack.scale(horizontalGap, 1, 1);
        gui.blit(matrixStack, 0, 0,
                u + lw + 1, v,
                1, th);

        matrixStack.translate(0, th + verticalGap, 0);
        gui.blit(matrixStack, 0, 0,
                u + lw + 1, v + th + 3,
                1, bh);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.translate(x, y + th, 0);
        matrixStack.scale(1, verticalGap, 1);
        gui.blit(matrixStack, 0, 0,
                u, v + th + 1,
                lw, 1);

        matrixStack.translate(lw + horizontalGap, 0, 0);
        gui.blit(matrixStack, 0, 0,
                u + lw + 3, v + th + 1,
                rw, 1);
        matrixStack.pop();
    }

    public static void
    renderLabelAtRight(AbstractGui gui, MatrixStack matrixStack, String text, int x, int y) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(UI_RESOURCE);

        FontRenderer fontRenderer = minecraft.fontRenderer;
        int textWidth = fontRenderer.getStringWidth(text);

        matrixStack.push();
        matrixStack.translate(x, y, 0);

        float scale = 0.75f;
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-9, 0, 0);
        gui.blit(matrixStack, 0, 0, 143, 36, 9, 24);

        int gap = 5;
        int remainingWidth = textWidth + 2 * gap;
        matrixStack.translate(-remainingWidth, 0, 0);
        while (remainingWidth > 0) {
            gui.blit(matrixStack, 0, 0, 136, 36, 6, 24);
            remainingWidth -= 6;
            matrixStack.translate(Math.min(6, remainingWidth), 0, 0);
        }

        matrixStack.translate(-textWidth - 2 * gap - 6, 0, 0);
        gui.blit(matrixStack, 0, 0, 121, 36, 14, 24);

        fontRenderer.drawString(matrixStack, text,
                14 + gap, 9, 0xFF_443a1b);

        matrixStack.pop();
    }

}
