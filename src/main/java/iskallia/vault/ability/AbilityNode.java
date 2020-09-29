package iskallia.vault.ability;

import iskallia.vault.init.ModConfigs;
import net.minecraft.nbt.CompoundNBT;

public class AbilityNode<T extends PlayerAbility> {

	private AbilityGroup<T> group;
	private int level;

	public AbilityNode(AbilityGroup<T> group, int level) {
		this.group = group;
		this.level = level;
	}

	public AbilityGroup<T> getGroup() {
		return this.group;
	}

	public int getLevel() {
		return this.level;
	}

	public T getAbility() {
		return this.getGroup().getAbility(this.getLevel());
	}

	public String getName() {
		return this.getGroup().getName(this.getLevel());
	}

	public CompoundNBT toNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("Name", this.getGroup().getParentName());
		nbt.putInt("Level", this.getLevel());
		return nbt;
	}

	public static <T extends PlayerAbility> AbilityNode<T> fromNBT(CompoundNBT nbt, Class<T> clazz) {
		AbilityGroup<T> group = (AbilityGroup<T>)ModConfigs.ABILITIES.getByName(nbt.getString("Name"));
		int level = nbt.getInt("Level");
		return new AbilityNode<>(group, level);
	}

}
