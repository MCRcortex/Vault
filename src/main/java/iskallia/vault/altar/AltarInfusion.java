package iskallia.vault.altar;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

public class AltarInfusion {

	private PedestalItem[] requiredItems;

	public AltarInfusion() {
		requiredItems = new PedestalItem[4];
	}

	public AltarInfusion(PedestalItem[] items) {
		this.requiredItems = items;
	}

	public PedestalItem[] getRequiredItems() {
		return requiredItems;
	}

	public static CompoundNBT serialize(PedestalItem[] requiredItems) {
		CompoundNBT nbt = new CompoundNBT();
		ListNBT list = new ListNBT();
		for (int i = 0; i < requiredItems.length; i++) {
			list.add(PedestalItem.serializeNBT(requiredItems[i]));
		}
		nbt.put("requiredItems", list);
		return nbt;
	}

	public static PedestalItem[] deserialize(CompoundNBT nbt) {
		ListNBT list = nbt.getList("requiredItems", Constants.NBT.TAG_COMPOUND);
		PedestalItem[] requiredItems = new PedestalItem[4];
		for (int i = 0; i < list.size(); i++) {
			CompoundNBT compound = (CompoundNBT) list.get(i);
			requiredItems[i] = PedestalItem.deserializeNBT(compound);
		}
		return requiredItems;
	}

}
