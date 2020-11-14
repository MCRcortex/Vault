package iskallia.vault.altar;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

public class AltarInfusion {

	private RequiredItem[] requiredItems;

	public AltarInfusion() {
		requiredItems = new RequiredItem[4];
	}

	public AltarInfusion(RequiredItem[] items) {
		this.requiredItems = items;
	}

	public RequiredItem[] getRequiredItems() {
		return requiredItems;
	}

	public static CompoundNBT serialize(RequiredItem[] requiredItems) {
		CompoundNBT nbt = new CompoundNBT();
		ListNBT list = new ListNBT();
		for (RequiredItem item : requiredItems) {
			list.add(RequiredItem.serializeNBT(item));
		}
		nbt.put("requiredItems", list);
		return nbt;
	}

	public static RequiredItem[] deserialize(CompoundNBT nbt) {
		ListNBT list = nbt.getList("requiredItems", Constants.NBT.TAG_COMPOUND);
		RequiredItem[] requiredItems = new RequiredItem[4];
		for (int i = 0; i < list.size(); i++) {
			CompoundNBT compound = (CompoundNBT) list.get(i);
			requiredItems[i] = RequiredItem.deserializeNBT(compound);
		}
		return requiredItems;
	}

}
