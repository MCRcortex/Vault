package iskallia.vault.config;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class VaultPedestalConfig extends Config {

	@Expose
	public List<PedestalItem> ITEMS = new ArrayList<PedestalItem>();

	@Override
	public String getName() {
		return "pedestal_items";
	}

	@Override
	protected void reset() {

		ITEMS.add(new PedestalItem("minecraft:stone", 1000, 32000));
		ITEMS.add(new PedestalItem("minecraft:cobblestone", 1000, 32000));

	}

	public class PedestalItem {

		@Expose
		public String ITEM;
		@Expose
		public int MIN;
		@Expose
		public int MAX;

		public PedestalItem(String item, int min, int max) {
			ITEM = item;
			MIN = min;
			MAX = max;
		}

	}

}
