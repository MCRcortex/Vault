package iskallia.vault.skill.ability;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.type.PlayerAbility;
import iskallia.vault.util.RomanNumber;

public class AbilityGroup<T extends PlayerAbility> {

    @Expose private final String name;
    @Expose private final T[] levels;

    private BiMap<String, T> registry;

    public AbilityGroup(String name, T... levels) {
        this.name = name;
        this.levels = levels;
    }

    public int getMaxLevel() {
        return this.levels.length;
    }

    public String getParentName() {
        return this.name;
    }

    public String getName(int level) {
        if (level == 0) return name + " " + RomanNumber.toRoman(0);
        return this.registry.inverse().get(this.getAbility(level));
    }

    public T getAbility(int level) {
        if (level < 0) return this.levels[0];
        if (level >= getMaxLevel()) return this.levels[getMaxLevel() - 1];
        return this.levels[level - 1];
    }

    public int learningCost() {
        return this.levels[0].getCost();
    }

    public int cost(int level) {
        if (level >= getMaxLevel()) return -1;
        return this.levels[level - 1].getCost();
    }

    public BiMap<String, T> getRegistry() {
        if (this.registry == null) {
            this.registry = HashBiMap.create(this.getMaxLevel());

            if (this.getMaxLevel() == 1) {
                this.registry.put(getParentName(), this.levels[0]);

            } else if (this.getMaxLevel() > 1) {
                for (int i = 0; i < getMaxLevel(); i++) {
                    this.registry.put(this.getParentName() + " " + RomanNumber.toRoman(i + 1),
                            this.getAbility(i + 1));
                }
            }
        }

        return this.registry;
    }

    /* --------------------------------------- */

    // TODO: ofEffect; for Night vision and Invisibility

}
