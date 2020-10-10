package iskallia.vault.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import iskallia.vault.util.ResourceBoundary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

// TODO: Replace fill() calls with necessary blit() calls
public class AbilityWidget extends Widget {

    private static final int PIP_SIZE = 8; //px
    private static final int GAP_SIZE = 2; //px
    private static final int ICON_SIZE = 30; // px
    private static final int MAX_PIP_INLINE = 5;

    private static final ResourceLocation RESOURCE = new ResourceLocation(Vault.MOD_ID, "textures/gui/ability-widget.png");
    private static final ResourceLocation ABILITIES_RESOURCE = new ResourceLocation(Vault.MOD_ID, "textures/gui/abilities.png");

    int maxLevel, level;
    AbilityFrame frame;

    public AbilityWidget(int x, int y, int level, int maxLevel, AbilityFrame frame) {
        super(x, y,
                5 * PIP_SIZE + 4 * GAP_SIZE,
                pipRowCount(level) * (PIP_SIZE + GAP_SIZE) - GAP_SIZE,
                new StringTextComponent("the_vault.widgets.talent"));
        this.level = level;
        this.maxLevel = maxLevel;
        this.frame = frame;
    }

    @Override
    public void
    render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderIcon(matrixStack, mouseX, mouseY, partialTicks);
        renderPips(matrixStack, mouseX, mouseY, partialTicks);
    }

    public void
    renderIcon(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        ResourceBoundary resourceBoundary = frame.resourceBoundary;

        matrixStack.push();
        matrixStack.translate(-ICON_SIZE / 2f, -ICON_SIZE / 2f, 0);
        Minecraft.getInstance().textureManager.bindTexture(resourceBoundary.getResource());
        blit(matrixStack, this.x, this.y,
                resourceBoundary.getU(), resourceBoundary.getV(),
                resourceBoundary.getW(), resourceBoundary.getH());
        matrixStack.pop();

        matrixStack.push();
        matrixStack.translate(-16 / 2f, -16 / 2f, 0);
        Minecraft.getInstance().textureManager.bindTexture(ABILITIES_RESOURCE);
        blit(matrixStack, this.x, this.y,
                16 * 4, 0,
                16, 16);
        matrixStack.pop();
    }

    public void
    renderPips(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().textureManager.bindTexture(RESOURCE);

        int rowCount = pipRowCount(this.maxLevel);
        int remainingPips = this.maxLevel;
        int remainingFilledPips = this.level;
        for (int r = 0; r < rowCount; r++) {
            renderPipLine(matrixStack,
                    x,
                    y + (ICON_SIZE / 2) + 4 + r * (GAP_SIZE + PIP_SIZE),
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
                blit(matrixStack, 0, 0,
                        1, 102, 8, 8);
                remainingFilled--;

            } else {
                blit(matrixStack, 0, 0,
                        1, 93, 8, 8);
            }
            matrixStack.translate(PIP_SIZE + GAP_SIZE, 0, 0);
        }

        matrixStack.pop();
    }

    public static int
    pipRowCount(int level) {
        return (int) Math.ceil((float) level / MAX_PIP_INLINE);
    }

    public enum AbilityFrame {
        STAR(new ResourceBoundary(RESOURCE, 0, 31, ICON_SIZE, ICON_SIZE)),
        RECTANGULAR(new ResourceBoundary(RESOURCE, 30, 31, ICON_SIZE, ICON_SIZE)),
        ;

        ResourceBoundary resourceBoundary;

        AbilityFrame(ResourceBoundary resourceBoundary) {
            this.resourceBoundary = resourceBoundary;
        }

        public ResourceBoundary getResourceBoundary() {
            return resourceBoundary;
        }
    }

}
