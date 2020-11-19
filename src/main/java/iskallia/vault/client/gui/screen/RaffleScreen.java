package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import iskallia.vault.client.gui.helper.ConfettiParticles;
import iskallia.vault.client.gui.helper.Rectangle;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.widget.RaffleEntry;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.datafix.fixes.SwapHandsFix;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.StringTextComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RaffleScreen extends Screen {

    public static final ResourceLocation UI_RESOURCE = new ResourceLocation(Vault.MOD_ID, "textures/gui/raffle.png");
    public static final int containerWidth = 62, containerHeight = 20;
    public static final int leverWidth = 44, leverHeight = 61;

    protected ConfettiParticles leftConfettiPopper = new ConfettiParticles()
            .angleRange(200 + 90, 265 + 90)
            .quantityRange(60, 80)
            .delayRange(0, 10)
            .lifespanRange(20, 20 * 5)
            .sizeRange(2, 5)
            .speedRange(2, 10);

    protected ConfettiParticles rightConfettiPopper = new ConfettiParticles()
            .angleRange(200, 265)
            .quantityRange(60, 80)
            .delayRange(0, 10)
            .lifespanRange(20, 20 * 5)
            .sizeRange(2, 5)
            .speedRange(2, 10);

    protected boolean popped;

    protected boolean spinning;
    protected List<String> occupants;
    protected String winner;

    protected List<RaffleEntry> raffleWidgets;

    protected int spinTicks, distance;
    protected double elapsedTicks;
    private double C;

    public RaffleScreen() {
        super(new StringTextComponent("Raffle Screen"));

        this.raffleWidgets = new LinkedList<>();
        String[] names = {
                "TheBigBadWulf",
                "iGoodie",
                "Kumara22",
                "Kimandjax",
                "KaptainWutax",
                "Starmute",
                "Jmilthedude",
                "iskall85",
                "Monni_21",
                "OneHellOfALongNickname",
        };
        occupants = new LinkedList<>();
        occupants.addAll(Arrays.asList(names));
        Collections.shuffle(occupants);

        winner = "Starmute";
        int winnerIndex = occupants.indexOf(winner);

        this.spinTicks = 5 * 20; // ticks
        this.distance = 5 * (occupants.size() * containerHeight + (occupants.size() - 1) + 1)
                + (winnerIndex - 1) * (containerHeight + 1); // pixels

        for (int i = 0; i < 10; i++) {
            RaffleEntry entry = new RaffleEntry(occupants.get(i), i % 5);
            Rectangle bounds = new Rectangle();
            bounds.x0 = 3;
            bounds.y0 = i * (containerHeight + 1);
            bounds.setWidth(containerWidth);
            bounds.setHeight(containerHeight);
            entry.setBounds(bounds);
            raffleWidgets.add(entry);
        }

        this.C = 0.5 * spinTicks * spinTicks;
    }

    public Rectangle getLeverBounds() {
        Rectangle bounds = new Rectangle();
        int midX = width / 2;
        int midY = height / 2;
        bounds.x0 = midX + 68;
        bounds.y0 = midY - leverHeight / 2 - 12;
        bounds.setWidth(leverWidth);
        bounds.setHeight(leverHeight);
        return bounds;
    }

    public Rectangle getViewportBounds() {
        Rectangle bounds = new Rectangle();
        int midX = width / 2;
        int midY = height / 2;
        bounds.x0 = midX - 34;
        bounds.y0 = midY - 25;
        bounds.setWidth(68);
        bounds.setHeight(50);
        return bounds;
    }

    public double calculateYOffset(double tick) {
        /*
         * Mega Wutax-mind:
         *  C=0.5 * R^2
         *  d(t) = (-x^3/6 + C*x) * D / (-R^3/6 + C*R)
         */
        return (-(tick * tick * tick) / 6 + C * tick)
                * distance / (-(spinTicks * spinTicks * spinTicks) / 6.0 + C * spinTicks);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) { }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Rectangle leverBounds = getLeverBounds();

        if (!spinning && !popped && leverBounds.contains((int) mouseX, (int) mouseY)) {
            spinning = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack, 0x00_000000);

        int midX = width / 2;
        int midY = height / 2;

        int crystalWidth = 136;
        int crystalHeight = 174;

        getMinecraft().textureManager.bindTexture(UI_RESOURCE);
        blit(matrixStack, midX - crystalWidth / 2, midY - crystalHeight / 2,
                0, 0, crystalWidth, crystalHeight);

        UIHelper.renderOverflowHidden(matrixStack,
                this::renderWheelBackground,
                this::renderWheel);

        int plateWidth = 74;
        int plateHeight = 124;

        getMinecraft().textureManager.bindTexture(UI_RESOURCE);
        blit(matrixStack, midX - plateWidth / 2, midY - plateHeight / 2,
                136, 0, plateWidth, plateHeight);

        int indicatorWidth = 17;
        int indicatorHeight = 15;

        blit(matrixStack, midX - 42, midY - indicatorHeight / 2,
                0, 174, indicatorWidth, indicatorHeight);

        renderLever(matrixStack, mouseX, mouseY);

        if (popped) {
            String winnerText = String.format("Winner is %s!", winner);
            int textWidth = font.getStringWidth(winnerText);
            font.drawStringWithShadow(matrixStack, winnerText,
                    midX - textWidth / 2f,
                    midY + crystalHeight / 2f + 10,
                    0xFF_b5e8ff);
        }

        leftConfettiPopper.spawnedPosition(
                10,
                midY
        );
        rightConfettiPopper.spawnedPosition(
                width - 10,
                midY
        );

        leftConfettiPopper.tick();
        rightConfettiPopper.tick();
        leftConfettiPopper.render(matrixStack);
        rightConfettiPopper.render(matrixStack);

        if (spinning) {
            elapsedTicks += partialTicks;
        }
    }

    protected void renderLever(MatrixStack matrixStack, int mouseX, int mouseY) {
        Rectangle leverBounds = getLeverBounds();

        getMinecraft().textureManager.bindTexture(UI_RESOURCE);
        if (spinning || popped) {
            blit(matrixStack, leverBounds.x0, leverBounds.y0 + 21,
                    98, 192, leverWidth, leverHeight);

        } else if (leverBounds.contains(mouseX, mouseY)) {
            blit(matrixStack, leverBounds.x0 - 2, leverBounds.y0 - 2,
                    47, 190, leverWidth + 4, leverHeight + 4);

        } else {
            blit(matrixStack, leverBounds.x0, leverBounds.y0,
                    0, 192, leverWidth, leverHeight);
        }
    }

    protected void renderWheelBackground(MatrixStack matrixStack) {
        Rectangle viewportBounds = getViewportBounds();
        AbstractGui.fill(matrixStack,
                viewportBounds.x0,
                viewportBounds.y0,
                viewportBounds.x1,
                viewportBounds.y1,
                0xFF_36393f
        );
    }

    protected void renderWheel(MatrixStack matrixStack) {
        Rectangle bounds = getViewportBounds();
        Vector2f midpoint = bounds.midpoint();
        double yOffset = calculateYOffset(elapsedTicks);

        matrixStack.push();
        matrixStack.translate(bounds.x0, bounds.y0, 0);
        matrixStack.translate(0, -yOffset, 0);

        for (RaffleEntry raffleWidget : this.raffleWidgets) {
            Rectangle entryBounds = raffleWidget.getBounds();

            getMinecraft().textureManager.bindTexture(UI_RESOURCE);
            blit(matrixStack,
                    entryBounds.x0,
                    entryBounds.y0,
                    142, 124 + raffleWidget.getTypeIndex() * containerHeight,
                    containerWidth, containerHeight
            );
            boolean isTargeted = entryBounds.contains(
                    entryBounds.x0 + 2,
                    (int) (midpoint.y + yOffset)
            );

            matrixStack.push();
            int stringWidth = font.getStringWidth(raffleWidget.getOccupantName());
            float widthRatio = (float) stringWidth / containerWidth;

            matrixStack.translate(entryBounds.x0 + 1, entryBounds.y0 + 1, 0);
            matrixStack.translate(entryBounds.getWidth() / 2f, entryBounds.getHeight() / 2f, 0);
            if (widthRatio > 0.9) matrixStack.scale(0.9f / widthRatio, 0.9f / widthRatio, 1);
            matrixStack.translate(-stringWidth / 2f, -5, 0);

            if (isTargeted) {
                font.drawStringWithShadow(matrixStack, raffleWidget.getOccupantName(),
                        0, 0, 0xFF_FF0000);
            } else {
                font.drawString(matrixStack, raffleWidget.getOccupantName(),
                        0, 0, 0xFF_36393f);
            }

            RenderSystem.enableDepthTest();
            matrixStack.pop();

            if (entryBounds.y1 - yOffset < 0) {
                getMinecraft().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                entryBounds.y0 += this.raffleWidgets.size() * (containerHeight + 1);
                entryBounds.y1 += this.raffleWidgets.size() * (containerHeight + 1);
            }
        }

        matrixStack.pop();

        if (elapsedTicks >= spinTicks) {
            spinning = false;
            if (!popped) {
                getMinecraft().getSoundHandler().play(SimpleSound.master(SoundEvents.ENTITY_FIREWORK_ROCKET_TWINKLE, 1.0F));
                leftConfettiPopper.pop();
                rightConfettiPopper.pop();
                popped = true;
            }
        }
    }

}
