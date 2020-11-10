package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.client.gui.helper.FontHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class VaultRaidOverlay {

    public static int remainingTicks;

    @SubscribeEvent
    public static void
    onPostRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR)
            return; // Render only on HOTBAR

        if (remainingTicks == 0)
            return; // Timed out, stop here

        MatrixStack matrixStack = event.getMatrixStack();
        Minecraft minecraft = Minecraft.getInstance();
        int bottom = minecraft.getMainWindow().getScaledHeight();
        int barWidth = 62;
        int barHeight = 22;

        matrixStack.push();
        matrixStack.translate(10, bottom - barHeight, 0);
        FontHelper.drawStringWithBorder(matrixStack,
                formatTimeString(),
                18, -12,
                remainingTicks < 30 * 20
                        && remainingTicks % 10 < 5
                        ? 0xFF_FF0000
                        : 0xFF_FFFFFF,
                0xFF_000000);
        matrixStack.pop();
    }

    public static String formatTimeString() {
        long seconds = (remainingTicks / 20) % 60;
        long minutes = ((remainingTicks / 20) / 60) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

}
