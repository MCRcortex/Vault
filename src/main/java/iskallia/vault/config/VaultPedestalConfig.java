package iskallia.vault.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.annotations.Expose;

import iskallia.vault.altar.PedestalItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class VaultPedestalConfig extends Config {

	@Expose
	public List<PedestalConfigItem> ITEMS = new ArrayList<PedestalConfigItem>();

	private Random rand = new Random();

	@Override
	public String getName() {
		return "pedestal_items";
	}

	@Override
	protected void reset() {

		ITEMS.add(new PedestalConfigItem("minecraft:stone", 1000, 32000));
		ITEMS.add(new PedestalConfigItem("minecraft:cobblestone", 1000, 32000));
		ITEMS.add(new PedestalConfigItem("minecraft:diamond", 1000, 32000));
		ITEMS.add(new PedestalConfigItem("minecraft:gold_nugget", 1000, 32000));

	}

	public PedestalItem[] getRequiredItemsFromConfig() {
		PedestalItem[] pedestalItems = new PedestalItem[4];

		List<PedestalConfigItem> configItems = new ArrayList<PedestalConfigItem>();
		configItems.addAll(ITEMS);

		for (int i = 0; i < pedestalItems.length; i++) {
			PedestalConfigItem configItem = ITEMS.remove(rand.nextInt(ITEMS.size()));
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(configItem.ITEM_ID));

			pedestalItems[i] = new PedestalItem(new ItemStack(item), getRandomInt(configItem.MIN, configItem.MAX), 0);
		}

		return pedestalItems;
	}

	private int getRandomInt(int min, int max) {
		return (int) (Math.random() * (max - min) + min);
	}

	public class PedestalConfigItem {

		@Expose
		public String ITEM_ID;
		@Expose
		public int MIN;
		@Expose
		public int MAX;

		public PedestalConfigItem(String item, int min, int max) {
			ITEM_ID = item;
			MIN = min;
			MAX = max;
		}

	}

}
