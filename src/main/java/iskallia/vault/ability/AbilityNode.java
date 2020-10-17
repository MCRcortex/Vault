package iskallia.vault.ability;

import iskallia.vault.init.ModConfigs;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;

public class AbilityNode<T extends PlayerAbility> implements INBTSerializable<CompoundNBT> {

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

    public boolean isLearned() {
        return this.level != 0;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Name", this.getGroup().getParentName());
        nbt.putInt("Level", this.getLevel());
        return nbt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deserializeNBT(CompoundNBT nbt) {
        String groupName = nbt.getString("Name");
        this.group = (AbilityGroup<T>) ModConfigs.ABILITIES.getByName(groupName);
        this.level = nbt.getInt("Level");
    }

    public static <T extends PlayerAbility> AbilityNode<T> fromNBT(CompoundNBT nbt, Class<T> clazz) {
        AbilityGroup<T> group = (AbilityGroup<T>) ModConfigs.ABILITIES.getByName(nbt.getString("Name"));
        int level = nbt.getInt("Level");
        return new AbilityNode<>(group, level);
    }

}
