package iskallia.vault.item;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemVaultCrystal extends Item {

    private Rarity rarity;

    public ItemVaultCrystal(ItemGroup group, ResourceLocation id, Rarity rarity) {
        super(new Properties()
                .group(group)
                .maxStackSize(1));

        this.setRegistryName(id);
        this.rarity = rarity;
    }

    public static ItemStack getRandomCrystal() {
        List<Integer> weights = new ArrayList<>();
        for (int normal = 0; normal < ModConfigs.VAULT_CRYSTAL.NORMAL_WEIGHT; normal++) {
            weights.add(0);
        }
        for (int rare = 0; rare < ModConfigs.VAULT_CRYSTAL.RARE_WEIGHT; rare++) {
            weights.add(1);
        }
        for (int epic = 0; epic < ModConfigs.VAULT_CRYSTAL.EPIC_WEIGHT; epic++) {
            weights.add(2);
        }
        for (int omega = 0; omega < ModConfigs.VAULT_CRYSTAL.OMEGA_WEIGHT; omega++) {
            weights.add(3);
        }
        Random rand = new Random();
        int randomIndex = weights.get(rand.nextInt(weights.size()));
        Rarity rarity = Rarity.values()[randomIndex];
        switch (rarity) {
            case NORMAL:
                return new ItemStack(ModItems.VAULT_CRYSTAL_NORMAL);
            case RARE:
                return new ItemStack(ModItems.VAULT_CRYSTAL_RARE);
            case EPIC:
                return new ItemStack(ModItems.VAULT_CRYSTAL_EPIC);
            case OMEGA:
                return new ItemStack(ModItems.VAULT_CRYSTAL_OMEGA);
        }
        return new ItemStack(ModItems.VAULT_CRYSTAL_NORMAL);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (stack.getItem() instanceof ItemVaultCrystal) {
            ItemVaultCrystal item = (ItemVaultCrystal) stack.getItem();
            switch (item.getRarity()) {
                case NORMAL:
                    return new StringTextComponent(item.getRarity().color + "Vault Crystal (normal)");
                case RARE:
                    return new StringTextComponent(item.getRarity().color + "Vault Crystal (rare)");
                case EPIC:
                    return new StringTextComponent(item.getRarity().color + "Vault Crystal (epic)");
                case OMEGA:
                    return new StringTextComponent(item.getRarity().color + "Vault Crystal (omega)");
            }
        }

        return super.getDisplayName(stack);
    }

    public Rarity getRarity() {
        return rarity;
    }

    public enum Rarity {
        NORMAL(TextFormatting.WHITE),
        RARE(TextFormatting.YELLOW),
        EPIC(TextFormatting.LIGHT_PURPLE),
        OMEGA(TextFormatting.GREEN);

        public final TextFormatting color;

        Rarity(TextFormatting color) {
            this.color = color;
        }
    }
}
