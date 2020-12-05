package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import iskallia.vault.client.gui.helper.ArenaScoreboardContainer;
import iskallia.vault.client.gui.helper.FontHelper;
import iskallia.vault.init.ModSounds;
import iskallia.vault.util.RomanNumber;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ArenaScoreboardOverlay {

    public static final ResourceLocation HUD_RESOURCE = new ResourceLocation(Vault.MOD_ID, "textures/gui/arena-scoreboard.png");

    public static ArenaScoreboardContainer scoreboard = new ArenaScoreboardContainer();

    public static int hornsPlayedCount;
    private static SimpleSound arenaHorns;

    @SubscribeEvent
    public static void
    onPostRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.POTION_ICONS)
            return; // Render only on POTION_ICONS

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.world == null || minecraft.world.getDimensionKey() != Vault.ARENA_KEY) {
            hornsPlayedCount = 0;
            if (scoreboard.getSize() != 0)
                scoreboard.reset();
            return;
        }

        if (hornsPlayedCount < 1) {
            if (arenaHorns == null || !minecraft.getSoundHandler().isPlaying(arenaHorns)) {
                arenaHorns = SimpleSound.master(ModSounds.ARENA_HORNS_SFX, 1f, 1f);
                minecraft.getSoundHandler().play(arenaHorns);
                hornsPlayedCount++;
            }
        }

        MatrixStack matrixStack = event.getMatrixStack();

        int right = minecraft.getMainWindow().getScaledWidth();
        int midY = minecraft.getMainWindow().getScaledHeight() / 2;
        int scoreboardWidth = 81;
        int scoreboardHeight = 99;

        matrixStack.push();
        matrixStack.translate(right - 1 - scoreboardWidth, midY - scoreboardHeight / 2f, 0);

        minecraft.getTextureManager().bindTexture(HUD_RESOURCE);
        minecraft.ingameGUI.blit(matrixStack,
                0, 0,
                1, 1, scoreboardWidth, scoreboardHeight);

        String header = "Top Damage";
        int headerWidth = minecraft.fontRenderer.getStringWidth(header);

        FontHelper.drawStringWithBorder(matrixStack, header,
                (scoreboardWidth - headerWidth) / 2f, -4,
                0xFF_FFFFFF, 0xFF_000000);

        matrixStack.translate(5, 10, 0);
        float scale = 0.75f;
        matrixStack.scale(scale, scale, scale);
        List<ArenaScoreboardContainer.ScoreboardEntry> scoreboardTop = scoreboard.getTop(5);
        for (int i = 0; i < scoreboardTop.size(); i++) {
            ArenaScoreboardContainer.ScoreboardEntry entry = scoreboardTop.get(i);
            minecraft.fontRenderer.drawStringWithShadow(matrixStack, String.format("%s. %s",
                    RomanNumber.toRoman(i + 1), entry.nickname),
                    0, 0, 0xFF_FFFFFF);
            String damage = String.format("%.0f", entry.totalDamage * 100);
            int damageWidth = minecraft.fontRenderer.getStringWidth(damage);
            minecraft.fontRenderer.drawStringWithShadow(matrixStack, damage,
                    (scoreboardWidth - damageWidth - 5) / scale, 10, 0xFF_b72917);
            matrixStack.translate(0, 23, 0);
        }

        matrixStack.pop();
    }

}
