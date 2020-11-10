package iskallia.vault.altar;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class PedestalItem {

	private ItemStack item;
	private int currentAmount;
	private int amountRequired;

	public PedestalItem(ItemStack stack, int currentAmount, int amountRequired) {
		this.item = stack;
		this.currentAmount = currentAmount;
		this.amountRequired = amountRequired;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public int getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(int currentAmount) {
		this.currentAmount = currentAmount;
	}

	public void addAmount(int amount) {
		currentAmount += amount;
	}

	public int getAmountRequired() {
		return amountRequired;
	}

	public void setAmountRequired(int amountRequired) {
		this.amountRequired = amountRequired;
	}

	public static CompoundNBT serializeNBT(PedestalItem pedestalItem) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.put("item", pedestalItem.getItem().serializeNBT());
		nbt.putInt("currentAmount", pedestalItem.getCurrentAmount());
		nbt.putInt("amountRequired", pedestalItem.getAmountRequired());
		return nbt;
	}

	public static PedestalItem deserializeNBT(CompoundNBT nbt) {
		if (!nbt.contains("item"))
			return null;
		return new PedestalItem(ItemStack.read(nbt.getCompound("item")), nbt.getInt("currentAmount"), nbt.getInt("amountRequired"));
	}

}
