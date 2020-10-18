package iskallia.vault.ability;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.annotations.Expose;
import iskallia.vault.ability.passive.AttributeAbility;
import iskallia.vault.ability.passive.EffectAbility;
import iskallia.vault.util.RomanNumber;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;

import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

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
        return this.getRegistry().inverse().get(this.getAbility(level));
    }

    public T getAbility(int level) {
        assert 0 < level && level <= getMaxLevel();
        return this.levels[level - 1];
    }

    public int learningCost() {
        return this.levels[0].getCost();
    }

    public int cost(int level) {
        if (level > getMaxLevel()) return -1;
        return this.levels[level - 1].getCost();
    }

    public BiMap<String, T> getRegistry() {
        if (this.registry == null) {
            this.registry = HashBiMap.create(this.getMaxLevel());

            if (this.getMaxLevel() == 1) {
                this.registry.put(this.getParentName(), this.levels[0]);

            } else if (this.getMaxLevel() > 1) {
                for (int i = 0; i < this.getMaxLevel(); i++) {
                    this.registry.put(this.getParentName() + " " + RomanNumber.toRoman(i + 1),
                            this.getAbility(i + 1));
                }
            }
        }

        return this.registry;
    }

    public static AbilityGroup<EffectAbility> ofEffect(String name, Effect effect, EffectAbility.Type type, int maxLevel,
                                                       IntUnaryOperator cost) {
        EffectAbility[] abilities = IntStream.range(0, maxLevel)
                .mapToObj(i -> new EffectAbility(cost.applyAsInt(i + 1), effect, i, type))
                .toArray(EffectAbility[]::new);
        return new AbilityGroup<>(name, abilities);
    }

    public static AbilityGroup<AttributeAbility> ofAttribute(String name, Attribute attribute, String modifierName,
                                                             int maxLevel, IntUnaryOperator cost, IntToDoubleFunction amount,
                                                             IntFunction<AttributeModifier.Operation> operation) {
        AttributeAbility[] abilities = IntStream.range(0, maxLevel)
                .mapToObj(i -> new AttributeAbility(cost.applyAsInt(i + 1), attribute,
                        new AttributeAbility.Modifier(
                                modifierName + " " + RomanNumber.toRoman(i + 1),
                                amount.applyAsDouble(i + 1),
                                operation.apply(i + 1)
                        )))
                .toArray(AttributeAbility[]::new);
        return new AbilityGroup<>(name, abilities);
    }

}
