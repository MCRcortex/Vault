package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.SingleItemEntry;
import iskallia.vault.util.StatueType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class StatueLootConfig extends Config {

    @Expose private List<SingleItemEntry> GIFT_NORMAL_STATUE_LOOT;
    @Expose private int GIFT_NORMAL_STATUE_INTERVAL;
    @Expose private List<SingleItemEntry> GIFT_MEGA_STATUE_LOOT;
    @Expose private int GIFT_MEGA_STATUE_INTERVAL;
    @Expose private List<SingleItemEntry> PLAYER_STATUE_LOOT;
    @Expose private int PLAYER_STATUE_INTERVAL;

    @Override
    public String getName() {
        return "statue_loot";
    }

    @Override
    protected void reset() {
        this.GIFT_NORMAL_STATUE_LOOT = new LinkedList<>();
        this.GIFT_NORMAL_STATUE_LOOT.add(new SingleItemEntry("minecraft:apple", "{display:{Name:'{\"text\":\"Fancy Apple\"}'}}"));
        this.GIFT_NORMAL_STATUE_LOOT.add(new SingleItemEntry("minecraft:wooden_sword", "{Enchantments:[{id:\"minecraft:sharpness\",lvl:10s}]}"));
        this.GIFT_NORMAL_STATUE_INTERVAL = 500;

        this.GIFT_MEGA_STATUE_LOOT = new LinkedList<>();
        this.GIFT_MEGA_STATUE_LOOT.add(new SingleItemEntry("minecraft:golden_apple", "{display:{Name:'{\"text\":\"Fancier Apple\"}'}}"));
        this.GIFT_MEGA_STATUE_LOOT.add(new SingleItemEntry("minecraft:diamond_sword", "{Enchantments:[{id:\"minecraft:sharpness\",lvl:10s}]}"));
        this.GIFT_MEGA_STATUE_INTERVAL = 1000;
        this.PLAYER_STATUE_LOOT = new LinkedList<>();
        this.PLAYER_STATUE_INTERVAL = 500;
    }

    public ItemStack randomLoot(StatueType type) {
        switch (type) {
            case GIFT_NORMAL:
                return getRandom(GIFT_NORMAL_STATUE_LOOT);
            case GIFT_MEGA:
                return getRandom(GIFT_MEGA_STATUE_LOOT);
            case PLAYER:
                return getRandom(PLAYER_STATUE_LOOT);
        }

        throw new InternalError("Unknown Statue variant: " + type);
    }

    public int getInterval(StatueType type) {
        switch (type) {
            case GIFT_NORMAL:
                return GIFT_NORMAL_STATUE_INTERVAL;
            case GIFT_MEGA:
                return GIFT_MEGA_STATUE_INTERVAL;
            case PLAYER:
                return PLAYER_STATUE_INTERVAL;
        }

        throw new InternalError("Unknown Statue variant: " + type);
    }

    private ItemStack getRandom(List<SingleItemEntry> loottable) {
        Random rand = new Random();
        ItemStack stack = ItemStack.EMPTY;

        if (loottable == null || loottable.isEmpty())
            return stack;

        SingleItemEntry randomEntry = loottable.get(rand.nextInt(loottable.size()));

        try {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(randomEntry.ITEM));
            stack = new ItemStack(item);
            CompoundNBT nbt = JsonToNBT.getTagFromJson(randomEntry.NBT);
            stack.setTag(nbt);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stack;
    }

}
