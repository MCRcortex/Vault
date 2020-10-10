package iskallia.vault.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.container.AbilityTreeContainer;
import iskallia.vault.gui.widget.AbilityWidget;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class AbilityTreeScreen extends ContainerScreen<AbilityTreeContainer> {

    private static final ResourceLocation WINDOW = new ResourceLocation("textures/gui/advancements/window.png");
    private static final ResourceLocation TABS = new ResourceLocation("textures/gui/advancements/tabs.png");

    private Vector2f viewportTranslation;
    private float viewportZoomLevel;
    private boolean dragging;
    private Vector2f grabbedPos;

    private AbilityWidget testSkillBadge;
    private AbilityWidget testSkillBadge2;

    public AbilityTreeScreen(AbilityTreeContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, new StringTextComponent("Ability Tree Screen!"));
        this.viewportTranslation = new Vector2f(0, 0);
        this.viewportZoomLevel = 1f;
        this.dragging = false;
        this.grabbedPos = new Vector2f(0, 0);

        this.testSkillBadge = new AbilityWidget(100, 100, 7, 12);
        this.testSkillBadge2 = new AbilityWidget(-10, -50, 2, 3);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.dragging = true;
        this.grabbedPos = new Vector2f((float) mouseX, (float) mouseY);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (dragging) {
            float dx = (float) (mouseX - grabbedPos.x) / viewportZoomLevel;
            float dy = (float) (mouseY - grabbedPos.y) / viewportZoomLevel;
            this.viewportTranslation = new Vector2f(
                    viewportTranslation.x + dx,
                    viewportTranslation.y + dy);
            this.grabbedPos = new Vector2f((float) mouseX, (float) mouseY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int wheel = (int) delta;
        if (wheel < 0 && viewportZoomLevel > 0.1) {
            viewportZoomLevel -= 0.1;
        } else if (wheel > 0 && viewportZoomLevel < 2) {
            viewportZoomLevel += 0.1;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void
    render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        int x0 = 30; // px
        int y0 = 40; // px
        int x1 = (int) (width * 0.7);
        int y1 = height - 30;

        matrixStack.push();
        RenderSystem.enableDepthTest();
        matrixStack.translate(0, 0, 950);
        RenderSystem.colorMask(false, false, false, false);
        fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        matrixStack.translate(0, 0, -950);
        RenderSystem.depthFunc(GL11.GL_GEQUAL);
        fill(matrixStack, x1, y1, x0, y0, 0x91_709FB0);
        RenderSystem.depthFunc(GL11.GL_LEQUAL);

        matrixStack.push();
        matrixStack.translate(width / 2f, height / 2f, 0);
        matrixStack.scale(viewportZoomLevel, viewportZoomLevel, 1);
        matrixStack.translate(viewportTranslation.x, viewportTranslation.y, 0);
        testSkillBadge.render(matrixStack, mouseX, mouseY, partialTicks);
        testSkillBadge2.render(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.pop();

        RenderSystem.depthFunc(GL11.GL_GEQUAL);
        matrixStack.translate(0, 0, -950);
        RenderSystem.colorMask(false, false, false, false);
        fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        matrixStack.translate(0, 0, 950);
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.disableDepthTest();
        matrixStack.pop();
    }

    @Override
    protected void
    drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        renderBackground(matrixStack);
    }

    @Override
    protected void
    drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.func_243248_b(matrixStack,
                new StringTextComponent(""),
                (float) this.titleX, (float) this.titleY,
                4210752);
    }

}
