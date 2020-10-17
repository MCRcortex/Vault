package iskallia.vault.client.gui.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;

public class UIHelper {

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

}
