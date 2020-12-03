package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import iskallia.vault.client.gui.helper.ConfettiParticles;
import iskallia.vault.client.gui.helper.Rectangle;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.widget.RaffleEntry;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.init.ModSounds;
import iskallia.vault.network.message.RaffleMessage;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.StringTextComponent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RaffleScreen extends Screen {

    public static final ResourceLocation UI_RESOURCE = new ResourceLocation(Vault.MOD_ID, "textures/gui/raffle.png");
    public static final int containerWidth = 62, containerHeight = 20;

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
    protected Button raffleButton;

    protected int spinTicks, distance;
    protected double elapsedTicks;
    private double C;

    public RaffleScreen(List<String> occupants, String winner) {
        super(new StringTextComponent("Raffle Screen"));

        this.raffleWidgets = new LinkedList<>();
        this.occupants = new LinkedList<>();
        this.occupants.addAll(occupants);
        Collections.shuffle(occupants);

        this.winner = winner;
        int winnerIndex = occupants.indexOf(winner);

        this.spinTicks = 10 * 20; // ticks
        int freeSpinCount = 5;
        this.distance = freeSpinCount * (occupants.size() * containerHeight + (occupants.size() - 1) + 1)
                + (winnerIndex - 2) * (containerHeight + 1); // pixels

        for (int i = 0; i < this.occupants.size(); i++) {
            RaffleEntry entry = new RaffleEntry(occupants.get(i), i % 5);
            Rectangle bounds = new Rectangle();
            bounds.x0 = 0;
            bounds.y0 = i * (containerHeight + 1);
            bounds.setWidth(containerWidth);
            bounds.setHeight(containerHeight);
            entry.setBounds(bounds);
            raffleWidgets.add(entry);
        }

        this.C = 0.5 * spinTicks * spinTicks;
    }

    @Override
    protected void init() {
        super.init();

        int midX = width / 2;
        int midY = height / 2;

        this.raffleButton = new Button(midX - 50, midY + 40, 100, 20,
                new StringTextComponent("Raffle!"),
                this::onRaffleButtonClick);
    }

    public Rectangle getViewportBounds() {
        Rectangle bounds = new Rectangle();
        int midX = width / 2;
        int midY = height / 2;
        bounds.x0 = midX - 32;
        bounds.y0 = midY - 60;
        bounds.setWidth(62);
        bounds.setHeight(90);
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
    public void mouseMoved(double mouseX, double mouseY) {
        this.raffleButton.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.raffleButton.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return this.raffleButton.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return this.raffleButton.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    public void onRaffleButtonClick(Button button) {
        spinning = true;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack, 0x00_000000);

        int midX = width / 2;
        int midY = height / 2;

        int crystalWidth = 114;
        int crystalHeight = 130;

        matrixStack.push();
        matrixStack.translate(midX, midY, 0);
        matrixStack.scale(1.5f, 1.5f, 1.5f);
        getMinecraft().textureManager.bindTexture(UI_RESOURCE);
        blit(matrixStack, -crystalWidth / 2, -crystalHeight / 2,
                0, 0, crystalWidth, crystalHeight);
        matrixStack.pop();

        UIHelper.renderOverflowHidden(matrixStack,
                this::renderWheelBackground,
                this::renderWheel);

        Rectangle viewportBounds = getViewportBounds();
        getMinecraft().textureManager.bindTexture(UI_RESOURCE);
        RenderSystem.enableBlend();
        UIHelper.renderContainerBorder(this, matrixStack, viewportBounds,
                115, 0,
                7, 7, 7, 7,
                0x00_000000);

        int indicatorWidth = 17;
        int indicatorHeight = 15;

        Vector2f viewpointMidpoint = viewportBounds.midpoint();
        blit(matrixStack, midX - 42,
                (int) (viewpointMidpoint.y - indicatorHeight / 2),
                115, 18, indicatorWidth, indicatorHeight);

        this.raffleButton.render(matrixStack, mouseX, mouseY, partialTicks);
        this.raffleButton.active = !spinning && !popped;

        if (popped) {
            String winnerText = String.format("Winner is %s!", winner);
            int textWidth = font.getStringWidth(winnerText);
            font.drawStringWithShadow(matrixStack, winnerText,
                    midX - textWidth / 2f,
                    midY + crystalHeight / 2f + 33,
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
                    190, 3 + raffleWidget.getTypeIndex() * containerHeight,
                    containerWidth, containerHeight
            );
            boolean isTargeted = entryBounds.contains(
                    entryBounds.x0 + 2,
                    (int) (bounds.y1 - midpoint.y + yOffset)
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
                getMinecraft().getSoundHandler().play(SimpleSound.master(ModSounds.RAFFLE_SFX, 1.2F, 1F));
                entryBounds.y0 += this.raffleWidgets.size() * (containerHeight + 1);
                entryBounds.y1 += this.raffleWidgets.size() * (containerHeight + 1);
            }
        }

        matrixStack.pop();

        if (elapsedTicks >= spinTicks) {
            spinning = false;
            if (!popped) {
                ModNetwork.CHANNEL.sendToServer(RaffleMessage.animationDone(this.winner));
                getMinecraft().getSoundHandler().play(SimpleSound.master(ModSounds.CONFETTI_SFX, 1.0F));
                leftConfettiPopper.pop();
                rightConfettiPopper.pop();
                popped = true;
            }
        }
    }

}
