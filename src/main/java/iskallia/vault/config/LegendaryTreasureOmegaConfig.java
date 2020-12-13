package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.ItemEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LegendaryTreasureOmegaConfig extends Config {

    @Expose
    public List<ItemEntry> ITEMS = new ArrayList<>();

    @Override
    public String getName() {
        return "legendary_treasure_omega";
    }

    @Override
    protected void reset() {
        ITEMS.add(new ItemEntry("minecraft:enchanted_golden_apple", "{display:{Name:'{\"text\":\"Fanciest Apple\"}'}}"));
        ITEMS.add(new ItemEntry("minecraft:diamond_sword", "{Enchantments:[{id:\"minecraft:sharpness\",lvl:10s}]}"));
    }

    public ItemStack getRandom() {
        Random rand = new Random();
        ItemStack stack = ItemStack.EMPTY;

        ItemEntry legendaryItem = ITEMS.get(rand.nextInt(ITEMS.size()));

        try {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(legendaryItem.ITEM));
            stack = new ItemStack(item);
            CompoundNBT nbt = JsonToNBT.getTagFromJson(legendaryItem.NBT);
            stack.setTag(nbt);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return stack;
    }


}
