package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.helper.Rectangle;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.widget.RaffleEntry;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.StringTextComponent;

import java.util.LinkedList;
import java.util.List;

public class RaffleScreen extends Screen {

    protected boolean spinning;
    protected List<String> occupants;
    protected String winner;

    protected List<RaffleEntry> raffleWidgets;

    protected int spinTicks, distance;
    protected double elapsedTicks;
    private double C, N;

    private Button button;

    public RaffleScreen() {
        super(new StringTextComponent("Raffle Screen"));
        this.spinTicks = 20 * 20; // ticks
        this.distance = 1000; // pixels

        this.C = (Math.pow(spinTicks, 3) / 6) - spinTicks;
        this.N = distance / ((-Math.pow(spinTicks, 4) / 24)
                + (Math.pow(spinTicks, 2) / 2)
                + C * spinTicks);

        this.raffleWidgets = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            RaffleEntry entry = new RaffleEntry("Entry#" + (i + 1));
            Rectangle bounds = new Rectangle();
            bounds.x0 = 5;
            bounds.y0 = i * 21;
            bounds.setWidth(50);
            bounds.setHeight(20);
            entry.setBounds(bounds);
            raffleWidgets.add(entry);
        }

        this.button = new Button(
                10, 10,
                100, 20,
                new StringTextComponent("Spin Thingy"),
                button -> {
                    if (spinning) {
                        spinning = false;
                        elapsedTicks = 0;
                    } else {
                        spinning = true;
                    }
                }
        );
    }

    public Rectangle getViewportBounds() {
        Rectangle bounds = new Rectangle();
        bounds.x0 = 20;
        bounds.y0 = 50;
        bounds.setWidth(300);
        bounds.setHeight(150);
        return bounds;
    }

    public double calculateYOffset(double tick) {
        /*
         * Mega Wutax-mind:
         *  C = R^3 / 6 - R
         *  N = D / (-R^4 / 24 + R^2 / 2 + C * R)
         *  d(t) = (-t^4 / 24 + t^2 / 2 + C * t) * N
         */
        return N * (-Math.pow(tick, 4) / 24 + Math.pow(tick, 2) / 2 + C * tick);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        this.button.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.button.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return this.button.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return this.button.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack, 0x00_000000);

        this.button.render(matrixStack, mouseX, mouseY, partialTicks);

        UIHelper.renderOverflowHidden(matrixStack,
                this::renderWheelBackground,
                this::renderWheel);

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
                0xFF_000000
        );
    }

    protected void renderWheel(MatrixStack matrixStack) {
        Rectangle bounds = getViewportBounds();
        Vector2f midpoint = bounds.midpoint();
        double yOffset = calculateYOffset(elapsedTicks);

        matrixStack.push();
        matrixStack.translate(bounds.x0, bounds.y0, 0);
        matrixStack.translate(0, -yOffset, 0);

//        font.drawString(matrixStack, yOffset + "",
//                10, 10,
//                0xFF_FFFFFF);
//
//        font.drawString(matrixStack, spinning + "",
//                10, 21,
//                0xFF_FFFFFF);
//
//        font.drawString(matrixStack, elapsedTicks + "",
//                10, 32,
//                0xFF_FFFFFF);

        for (RaffleEntry raffleWidget : this.raffleWidgets) {
            Rectangle entryBounds = raffleWidget.getBounds();
            AbstractGui.fill(matrixStack,
                    entryBounds.x0,
                    entryBounds.y0,
                    entryBounds.x1,
                    entryBounds.y1,
                    0xFF_FFFFFF
            );
            boolean isTargetted = entryBounds.contains(
                    entryBounds.x0 + 2,
                    (int) (midpoint.y + yOffset)
            );

            font.drawString(matrixStack, raffleWidget.getOccupantName(),
                    entryBounds.x0 + 2,
                    entryBounds.y0 + 2,
                    isTargetted ? 0xFF_FF0000 : 0xFF_000000);
            RenderSystem.enableDepthTest();

            if (entryBounds.y1 - yOffset < 0) {
                // TODO: Play tick sound fx!
                entryBounds.y0 += 10 * (20 + 1);
                entryBounds.y1 += 10 * (20 + 1);
            }
        }

        matrixStack.pop();

        if (elapsedTicks >= spinTicks) {
            spinning = false;
        }
    }

}
