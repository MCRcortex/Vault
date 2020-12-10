package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.LegendaryItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LegendaryTreasureEpicConfig extends Config {

    @Expose
    public List<LegendaryItem> ITEMS = new ArrayList<>();

    @Override
    public String getName() {
        return "legendary_treasure_epic";
    }

    @Override
    protected void reset() {
        ITEMS.add(new LegendaryItem("minecraft:apple", "{display:{Name:'{\"text\":\"Fancy Apple\"}'}}"));
        ITEMS.add(new LegendaryItem("minecraft:iron_sword", "{Enchantments:[{id:\"minecraft:sharpness\",lvl:10s}]}"));
    }

    public ItemStack getRandom() {
        Random rand = new Random();
        ItemStack stack = ItemStack.EMPTY;

        LegendaryItem legendaryItem = ITEMS.get(rand.nextInt(ITEMS.size()));

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
