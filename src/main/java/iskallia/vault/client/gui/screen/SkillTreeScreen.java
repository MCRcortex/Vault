package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import iskallia.vault.ability.AbilityTree;
import iskallia.vault.client.gui.component.AbilityDialog;
import iskallia.vault.client.gui.component.ResearchDialog;
import iskallia.vault.client.gui.helper.FontHelper;
import iskallia.vault.client.gui.helper.Rectangle;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.client.gui.tab.ResearchesTab;
import iskallia.vault.client.gui.tab.SkillTab;
import iskallia.vault.client.gui.tab.TalentsTab;
import iskallia.vault.container.SkillTreeContainer;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.research.node.Research;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkillTreeScreen extends ContainerScreen<SkillTreeContainer> {

    public static final ResourceLocation UI_RESOURCE = new ResourceLocation(Vault.MOD_ID, "textures/gui/ability-tree.png");
    public static final ResourceLocation BACKGROUNDS_RESOURCE = new ResourceLocation(Vault.MOD_ID, "textures/gui/ability-tree-bgs.png");

    public static final int TAB_WIDTH = 28;
    public static final int GAP = 3;

    protected SkillTab activeTab;
    protected AbilityDialog abilityDialog;
    protected ResearchDialog researchDialog;

    public SkillTreeScreen(SkillTreeContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, new StringTextComponent("Ability Tree Screen!"));

        this.activeTab = new ResearchesTab(this);
        AbilityTree abilityTree = getContainer().getAbilityTree();
        ResearchTree researchTree = getContainer().getResearchTree();
        this.abilityDialog = new AbilityDialog(abilityTree);
        this.researchDialog = new ResearchDialog(researchTree, abilityTree);
        refreshWidgets();
    }

    public void refreshWidgets() {
        this.activeTab.refresh();
        if (this.abilityDialog != null) {
            this.abilityDialog.refreshWidgets();
        }
        if (this.researchDialog != null) {
            this.researchDialog.refreshWidgets();
        }
    }

    public Rectangle getContainerBounds() {
        Rectangle bounds = new Rectangle();
        bounds.x0 = 30; //px
        bounds.y0 = 60; //px
        bounds.x1 = (int) (width * 0.55); // Responsiveness ayyyyy
        bounds.y1 = height - 30;
        return bounds;
    }

    public Rectangle getTabBounds(int index, boolean active) {
        Rectangle containerBounds = getContainerBounds();
        Rectangle bounds = new Rectangle();
        bounds.x0 = containerBounds.x0 + 5 + index * (TAB_WIDTH + GAP);
        bounds.y0 = containerBounds.y0 - 25 - (active ? 21 : 17);
        bounds.setWidth(TAB_WIDTH);
        bounds.setHeight(active ? 32 : 25);
        return bounds;
    }

    public AbilityDialog getAbilityDialog() {
        return abilityDialog;
    }

    public ResearchDialog getResearchDialog() {
        return researchDialog;
    }

    /* --------------------------------------------------- */

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Rectangle containerBounds = getContainerBounds();

        if (containerBounds.contains((int) mouseX, (int) mouseY)) {
            this.activeTab.mouseClicked(mouseX, mouseY, button);

        } else {
//            Rectangle abilitiesTabBounds = getTabBounds(0, false);
            Rectangle talentsTabBounds = getTabBounds(1, activeTab instanceof TalentsTab);
            Rectangle researchesTabBounds = getTabBounds(2, activeTab instanceof ResearchesTab);

            if (talentsTabBounds.contains(((int) mouseX), ((int) mouseY))) {
                this.activeTab = new TalentsTab(this);
                this.refreshWidgets();

            } else if (researchesTabBounds.contains(((int) mouseX), ((int) mouseY))) {
                this.activeTab = new ResearchesTab(this);
                this.refreshWidgets();

            } else if (activeTab instanceof ResearchesTab) {
                this.researchDialog.mouseClicked((int) mouseX, (int) mouseY, button);

            } else {
                this.abilityDialog.mouseClicked((int) mouseX, (int) mouseY, button);
            }
        }


        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.activeTab.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        this.activeTab.mouseMoved(mouseX, mouseY);

        if (activeTab instanceof ResearchesTab) {
            this.researchDialog.mouseMoved((int) mouseX, (int) mouseY);
        } else {
            this.abilityDialog.mouseMoved((int) mouseX, (int) mouseY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.activeTab.mouseScrolled(mouseX, mouseY, delta);

        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    /* --------------------------------------------------- */

    @Override
    protected void
    drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        renderBackground(matrixStack);
    }

    @Override
    protected void
    drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        // For some reason, without this it won't render :V
        this.font.func_243248_b(matrixStack,
                new StringTextComponent(""),
                (float) this.titleX, (float) this.titleY,
                4210752);
    }

    @Override
    public void
    render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        UIHelper.renderOverflowHidden(matrixStack,
                this::renderContainerBackground,
                ms -> activeTab.render(ms, mouseX, mouseY, partialTicks));

        Rectangle containerBounds = getContainerBounds();

        if (VaultBarOverlay.unspentSkillPoints > 0) {
            renderLabel(matrixStack,
                    VaultBarOverlay.unspentSkillPoints + " unspent skill point(s)",
                    containerBounds.getHeight() - 28);
        }
        renderContainerBorders(matrixStack);
        renderContainerTabs(matrixStack);
        Rectangle dialogBounds = new Rectangle();
        dialogBounds.x0 = containerBounds.x1 + 15;
        dialogBounds.y0 = containerBounds.y0 - 18;
        dialogBounds.x1 = width - 21;
        dialogBounds.y1 = height - 21;

        researchDialog.setBounds(dialogBounds);
        abilityDialog.setBounds(dialogBounds);

        if (activeTab instanceof ResearchesTab) {
            researchDialog.render(matrixStack, mouseX, mouseY, partialTicks);

        } else {
            abilityDialog.render(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    private void
    renderContainerTabs(MatrixStack matrixStack) {
        Rectangle containerBounds = getContainerBounds();

        // Abilities
        Rectangle abilitiesTabBounds = getTabBounds(0, false);
        blit(matrixStack,
                abilitiesTabBounds.x0,
                abilitiesTabBounds.y0,
                63, 0,
                abilitiesTabBounds.getWidth(), abilitiesTabBounds.getHeight());
        blit(matrixStack,
                abilitiesTabBounds.x0 + 6,
                containerBounds.y0 - 25 - 11,
                32, 60, 16, 16);

        // Talents
        Rectangle talentsTabBounds = getTabBounds(1, activeTab instanceof TalentsTab);
        blit(matrixStack,
                talentsTabBounds.x0,
                talentsTabBounds.y0,
                63, (activeTab instanceof TalentsTab) ? 28 : 0,
                talentsTabBounds.getWidth(), talentsTabBounds.getHeight());
        blit(matrixStack,
                talentsTabBounds.x0 + 6,
                containerBounds.y0 - 25 - 11,
                16, 60, 16, 16);

        // Research
        Rectangle researchesTabBounds = getTabBounds(2, activeTab instanceof ResearchesTab);
        blit(matrixStack,
                researchesTabBounds.x0,
                researchesTabBounds.y0,
                63, (activeTab instanceof ResearchesTab) ? 28 : 0,
                researchesTabBounds.getWidth(), researchesTabBounds.getHeight());
        blit(matrixStack,
                researchesTabBounds.x0 + 6,
                containerBounds.y0 - 25 - 11,
                0, 60, 16, 16);

        Minecraft minecraft = getMinecraft();

        if (activeTab instanceof TalentsTab) {
            minecraft.fontRenderer.drawString(matrixStack, "Talents",
                    containerBounds.x0, containerBounds.y0 - 12, 0xFF_3f3f3f);

        } else if (activeTab instanceof ResearchesTab) {
            minecraft.fontRenderer.drawString(matrixStack, "Researches",
                    containerBounds.x0, containerBounds.y0 - 12, 0xFF_3f3f3f);
        }

        minecraft.textureManager.bindTexture(VaultBarOverlay.RESOURCE);

        String text = String.valueOf(VaultBarOverlay.vaultLevel);
        int textWidth = minecraft.fontRenderer.getStringWidth(text);
        int barWidth = 85;
        float expPercentage = (float) VaultBarOverlay.vaultExp / VaultBarOverlay.tnl;

        int barX = containerBounds.x1 - barWidth - 5;
        int barY = containerBounds.y0 - 10;

        minecraft.getProfiler().startSection("vaultBar");
        minecraft.ingameGUI.blit(matrixStack,
                barX, barY,
                1, 1, barWidth, 5);
        minecraft.ingameGUI.blit(matrixStack,
                barX, barY,
                1, 7, (int) (barWidth * expPercentage), 5);
        FontHelper.drawStringWithBorder(matrixStack,
                text,
                barX - textWidth - 1, barY - 1,
                0xFF_ffe637, 0xFF_3e3e3e);

        minecraft.getProfiler().endSection();

    }

    private void
    renderContainerBorders(MatrixStack matrixStack) {
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bindTexture(UI_RESOURCE);

        Rectangle containerBounds = getContainerBounds();

        RenderSystem.enableBlend();

        blit(matrixStack, containerBounds.x0 - 9, containerBounds.y0 - 18,
                0, 0, 15, 24);
        blit(matrixStack, containerBounds.x1 - 7, containerBounds.y0 - 18,
                18, 0, 15, 24);
        blit(matrixStack, containerBounds.x0 - 9, containerBounds.y1 - 7,
                0, 27, 15, 16);
        blit(matrixStack, containerBounds.x1 - 7, containerBounds.y1 - 7,
                18, 27, 15, 16);

        matrixStack.push();
        matrixStack.translate(containerBounds.x0 + 6, containerBounds.y0 - 18, 0);
        matrixStack.scale(containerBounds.x1 - containerBounds.x0 - 13, 1, 1);
        blit(matrixStack, 0, 0,
                16, 0, 1, 24);
        matrixStack.translate(0, containerBounds.y1 - containerBounds.y0 + 11, 0);
        blit(matrixStack, 0, 0,
                16, 27, 1, 16);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.translate(containerBounds.x0 - 9, containerBounds.y0 + 6, 0);
        matrixStack.scale(1, containerBounds.y1 - containerBounds.y0 - 13, 1);
        blit(matrixStack, 0, 0,
                0, 25, 15, 1);
        matrixStack.translate(containerBounds.x1 - containerBounds.x0 + 2, 0, 0);
        blit(matrixStack, 0, 0,
                18, 25, 15, 1);
        matrixStack.pop();
    }

    private void
    renderLabel(MatrixStack matrixStack, String text, int yLevel) {
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bindTexture(UI_RESOURCE);

        FontRenderer fontRenderer = minecraft.fontRenderer;

        Rectangle containerBounds = getContainerBounds();
        int textWidth = fontRenderer.getStringWidth(text);

        matrixStack.push();
        matrixStack.translate(containerBounds.x1, containerBounds.y0 + yLevel, 0);

        float scale = 0.75f;
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-9, 0, 0);
        blit(matrixStack, 0, 0, 143, 36, 9, 24);

        int gap = 5;
        int remainingWidth = textWidth + 2 * gap;
        matrixStack.translate(-remainingWidth, 0, 0);
        while (remainingWidth > 0) {
            blit(matrixStack, 0, 0, 136, 36, 6, 24);
            remainingWidth -= 6;
            matrixStack.translate(Math.min(6, remainingWidth), 0, 0);
        }

        matrixStack.translate(-textWidth - 2 * gap - 6, 0, 0);
        blit(matrixStack, 0, 0, 121, 36, 14, 24);

        fontRenderer.drawString(matrixStack, text,
                14 + gap, 9, 0xFF_443a1b);

        matrixStack.pop();
    }

    private void
    renderContainerBackground(MatrixStack matrixStack) {
        assert this.minecraft != null;

        this.minecraft.getTextureManager().bindTexture(BACKGROUNDS_RESOURCE);

        Rectangle containerBounds = getContainerBounds();

        // TODO: Include scale param
        int textureSize = 16;
        int currentX = containerBounds.x0;
        int currentY = containerBounds.y0;
        int uncoveredWidth = containerBounds.getWidth();
        int uncoveredHeight = containerBounds.getHeight();
        while (uncoveredWidth > 0) {
            while (uncoveredHeight > 0) {
                blit(matrixStack, currentX, currentY,
                        16 * 5, 0, // TODO: <-- depends on tab
                        Math.min(textureSize, uncoveredWidth),
                        Math.min(textureSize, uncoveredHeight)
                );
                uncoveredHeight -= textureSize;
                currentY += textureSize;
            }

            // Decrement
            uncoveredWidth -= textureSize;
            currentX += textureSize;

            // Reset
            uncoveredHeight = containerBounds.getHeight();
            currentY = containerBounds.y0;
        }
    }

}
