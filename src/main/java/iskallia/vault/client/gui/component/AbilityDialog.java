package iskallia.vault.client.gui.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.ability.AbilityGroup;
import iskallia.vault.ability.AbilityNode;
import iskallia.vault.ability.AbilityTree;
import iskallia.vault.client.gui.helper.FontHelper;
import iskallia.vault.client.gui.helper.Rectangle;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.screen.AbilityTreeScreen;
import iskallia.vault.client.gui.widget.AbilityWidget;
import iskallia.vault.config.AbilitiesGUIConfig;
import iskallia.vault.init.ModConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;

public class AbilityDialog extends AbstractGui {

    private Rectangle bounds;
    private AbilityGroup<?> abilityGroup;
    private AbilityTree abilityTree;

    public AbilityDialog(AbilityGroup<?> abilityGroup, AbilityTree abilityTree) {
        this.abilityGroup = abilityGroup;
        this.abilityTree = abilityTree;
    }

    public AbilityDialog setBounds(Rectangle bounds) {
        this.bounds = bounds;
        return this;
    }

    public void
    render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        matrixStack.push();

        renderBackground(matrixStack, mouseX, mouseY, partialTicks);

        matrixStack.translate(bounds.x0 + 5, bounds.y0 + 5, 0);
        renderHeading(matrixStack, mouseX, mouseY, partialTicks);
        renderContent(matrixStack, mouseX, mouseY, partialTicks);
        renderFooter(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.push();
    }

    private void
    renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().getTextureManager().bindTexture(AbilityTreeScreen.UI_RESOURCE);
        fill(matrixStack,
                bounds.x0 + 5, bounds.y0 + 5,
                bounds.x1 - 5, bounds.y1 - 5,
                0xFF_C6C6C6);

        blit(matrixStack,
                bounds.x0, bounds.y0,
                0, 44, 5, 5);
        blit(matrixStack,
                bounds.x1 - 5, bounds.y0,
                8, 44, 5, 5);
        blit(matrixStack,
                bounds.x0, bounds.y1 - 5,
                0, 52, 5, 5);
        blit(matrixStack,
                bounds.x1 - 5, bounds.y1 - 5,
                8, 52, 5, 5);

        matrixStack.push();
        matrixStack.translate(bounds.x0 + 5, bounds.y0, 0);
        matrixStack.scale(bounds.getWidth() - 10, 1, 1);
        blit(matrixStack, 0, 0,
                6, 44, 1, 5);
        matrixStack.translate(0, bounds.getHeight() - 5, 0);
        blit(matrixStack, 0, 0,
                6, 52, 1, 5);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.translate(bounds.x0, bounds.y0 + 5, 0);
        matrixStack.scale(1, bounds.getHeight() - 10, 1);
        blit(matrixStack, 0, 0,
                0, 50, 5, 1);
        matrixStack.translate(bounds.getWidth() - 5, 0, 0);
        blit(matrixStack, 0, 0,
                8, 50, 5, 1);
        matrixStack.pop();
    }

    private void
    renderHeading(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        AbilitiesGUIConfig.AbilityStyle abilityStyle = ModConfigs.ABILITIES_GUI.getStyles()
                .get(abilityGroup.getParentName());

        AbilityNode<?> abilityNode = abilityTree.getNodeByName(abilityGroup.getParentName());

        AbilityWidget abilityWidget = new AbilityWidget(abilityNode.getLevel(),
                abilityGroup.getMaxLevel(),
                false,
                abilityStyle);

        Rectangle abilityBounds = abilityWidget.getClickableBounds();

        UIHelper.renderContainerBorder(this, matrixStack,
                5, 12,
                bounds.getWidth() - 20,
                abilityBounds.getHeight() + 18,
                14, 44,
                2, 2, 2, 2,
                0xFF_8B8B8B);

        String abilityName = abilityNode.getLevel() == 0
                ? abilityNode.getGroup().getName(1)
                : abilityNode.getName();

        String subText = abilityNode.getLevel() == 0
                ? "Not Learned Yet"
                : "Learned";

        int gap = 10;
        int contentWidth = abilityBounds.getWidth() + gap
                + Math.max(fontRenderer.getStringWidth(abilityName), fontRenderer.getStringWidth(subText));

        matrixStack.push();
        matrixStack.translate(20, 0, 0);
        FontHelper.drawStringWithBorder(matrixStack,
                abilityName,
                abilityBounds.getWidth() + gap, 24,
                abilityNode.getLevel() == 0 ? 0xFF_FFFFFF : 0xFF_c6b11e,
                abilityNode.getLevel() == 0 ? 0xFF_000000 : 0xFF_3b3300);

        FontHelper.drawStringWithBorder(matrixStack,
                subText,
                abilityBounds.getWidth() + gap, 34,
                abilityNode.getLevel() == 0 ? 0xFF_FFFFFF : 0xFF_c6b11e,
                abilityNode.getLevel() == 0 ? 0xFF_000000 : 0xFF_3b3300);

        FontHelper.drawStringWithBorder(matrixStack,
                abilityGroup.getMaxLevel() + " Max Level(s)",
                abilityBounds.getWidth() + gap, 54,
                abilityNode.getLevel() == 0 ? 0xFF_FFFFFF : 0xFF_c6b11e,
                abilityNode.getLevel() == 0 ? 0xFF_000000 : 0xFF_3b3300);

        matrixStack.translate(-abilityStyle.x, -abilityStyle.y, 0); // Nullify the viewport style
        matrixStack.translate(abilityBounds.getWidth() / 2f, abilityBounds.getHeight() / 2f, 0);
        matrixStack.translate(0, 16, 0);
        abilityWidget.render(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.pop();
    }

    private void
    renderContent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    }

    private void
    renderFooter(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    }

}
