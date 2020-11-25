package iskallia.vault.util;

import net.minecraft.util.text.TextFormatting;

public enum VaultRarity {
    NORMAL(TextFormatting.WHITE),
    RARE(TextFormatting.YELLOW),
    EPIC(TextFormatting.LIGHT_PURPLE),
    OMEGA(TextFormatting.GREEN);

    public final TextFormatting color;

    VaultRarity(TextFormatting color) {
        this.color = color;
    }
}
