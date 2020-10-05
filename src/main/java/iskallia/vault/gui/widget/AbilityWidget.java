package iskallia.vault.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

// TODO: Replace fill() calls with necessary blit() calls
public class AbilityWidget extends Widget {

    private static final int PIP_SIZE = 8; //px
    private static final int GAP_SIZE = 5; //px
    private static final int ICON_SIZE = 32; // px
    private static final int MAX_PIP_INLINE = 5;

    int maxLevel, level;

    public AbilityWidget(int x, int y, int level, int maxLevel) {
        super(x, y,
                5 * PIP_SIZE + 4 * GAP_SIZE,
                pipRowCount(level) * (PIP_SIZE + GAP_SIZE) - GAP_SIZE,
                new StringTextComponent("the_vault.widgets.talent"));
        this.level = level;
        this.maxLevel = maxLevel;
    }

    @Override
    public void
    render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderIcon(matrixStack, mouseX, mouseY, partialTicks);
        renderPips(matrixStack, mouseX, mouseY, partialTicks);
    }

    public void
    renderIcon(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        matrixStack.push();
        matrixStack.translate(-ICON_SIZE / 2f, -ICON_SIZE / 2f, 0);
        fill(matrixStack, x, y, x + ICON_SIZE, y + ICON_SIZE, 0xFF_FFFFFF);
        matrixStack.pop();
    }

    public void
    renderPips(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int rowCount = pipRowCount(this.maxLevel);
        int remainingPips = this.maxLevel;
        int remainingFilledPips = this.level;
        for (int r = 0; r < rowCount; r++) {
            renderPipLine(matrixStack,
                    x,
                    y + (ICON_SIZE / 2) + 9 + r * (GAP_SIZE + PIP_SIZE),
                    Math.min(MAX_PIP_INLINE, remainingPips),
                    Math.min(MAX_PIP_INLINE, remainingFilledPips)
            );
            remainingPips -= MAX_PIP_INLINE;
            remainingFilledPips -= MAX_PIP_INLINE;
        }
    }

    public void
    renderPipLine(MatrixStack matrixStack, int x, int y, int count, int filledCount) {
        int lineWidth = count * PIP_SIZE + (count - 1) * GAP_SIZE;
        int remainingFilled = filledCount;

        matrixStack.push();
        matrixStack.translate(x, y, 0);
        matrixStack.translate(-lineWidth / 2f, -PIP_SIZE / 2f, 0);

        for (int i = 0; i < count; i++) {
            if (remainingFilled > 0) {
                fill(matrixStack, 0, 0, PIP_SIZE, PIP_SIZE, 0xFF_FFFF00);
                remainingFilled--;

            } else {
                fill(matrixStack, 0, 0, PIP_SIZE, PIP_SIZE, 0xFF_FF0000);
            }
            matrixStack.translate(PIP_SIZE + GAP_SIZE, 0, 0);
        }

        matrixStack.pop();
    }

    public static int
    pipRowCount(int level) {
        return (int) Math.ceil((float) level / MAX_PIP_INLINE);
    }

}
