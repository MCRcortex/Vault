package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.altar.RequiredItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VaultAltarConfig extends Config {

    @Expose
    public List<AltarConfigItem> ITEMS = new ArrayList<>();
    @Expose
    public float PULL_SPEED;
    @Expose
    public double PLAYER_RANGE_CHECK;
    @Expose
    public double ITEM_RANGE_CHECK;
    @Expose
    public int INFUSION_TIME;


    private Random rand = new Random();

    @Override
    public String getName() {
        return "vault_altar";
    }

    @Override
    protected void reset() {

        ITEMS.add(new AltarConfigItem("minecraft:stone", 1000, 32000));
        ITEMS.add(new AltarConfigItem("minecraft:cobblestone", 1000, 32000));
        ITEMS.add(new AltarConfigItem("minecraft:diamond", 1000, 32000));
        ITEMS.add(new AltarConfigItem("minecraft:gold_nugget", 1000, 32000));

        PULL_SPEED = 1f;
        PLAYER_RANGE_CHECK = 32d;
        ITEM_RANGE_CHECK = 8d;
        INFUSION_TIME = 5;

    }

    public List<RequiredItem> getRequiredItemsFromConfig() {
        List<RequiredItem> requiredItems = new ArrayList<>();

        List<AltarConfigItem> configItems = new ArrayList<>(ITEMS);

        for (int i = 0; i < 4; i++) {
            AltarConfigItem configItem = configItems.remove(rand.nextInt(configItems.size()));
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(configItem.ITEM_ID));

            requiredItems.add(new RequiredItem(new ItemStack(item), 0, getRandomInt(configItem.MIN, configItem.MAX)));
        }

        return requiredItems;
    }

    private int getRandomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public class AltarConfigItem {

        @Expose
        public String ITEM_ID;
        @Expose
        public int MIN;
        @Expose
        public int MAX;

        public AltarConfigItem(String item, int min, int max) {
            ITEM_ID = item;
            MIN = min;
            MAX = max;
        }

    }

}
