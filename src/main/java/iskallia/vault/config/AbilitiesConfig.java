package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.type.DashAbility;
import iskallia.vault.skill.ability.type.EffectAbility;
import iskallia.vault.skill.ability.type.SelfSustainAbility;
import iskallia.vault.skill.ability.type.VeinMinerAbility;
import net.minecraft.potion.Effects;

import java.util.Arrays;
import java.util.List;

public class AbilitiesConfig extends Config {

    @Expose public int cooldownTicks;

    @Expose public AbilityGroup<EffectAbility> NIGHT_VISION;
    @Expose public AbilityGroup<EffectAbility> INVISIBILITY;
    @Expose public AbilityGroup<VeinMinerAbility> VEIN_MINER;
    @Expose public AbilityGroup<SelfSustainAbility> SELF_SUSTAIN;
    @Expose public AbilityGroup<DashAbility> DASH;

    @Override
    public String getName() {
        return "abilities";
    }

    public List<AbilityGroup<?>> getAll() {
        return Arrays.asList(NIGHT_VISION, INVISIBILITY, VEIN_MINER,
                SELF_SUSTAIN, DASH);
    }

    public AbilityGroup<?> getByName(String name) {
        return this.getAll().stream().filter(group -> group.getParentName().equals(name)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Unknown ability with name " + name));
    }

    @Override
    protected void reset() {
        this.cooldownTicks = 20 * 10;

        this.NIGHT_VISION = AbilityGroup.ofEffect("Night Vision", Effects.NIGHT_VISION, EffectAbility.Type.ICON_ONLY, 1, i -> 1);
        this.INVISIBILITY = AbilityGroup.ofEffect("Invisibility", Effects.INVISIBILITY, EffectAbility.Type.ICON_ONLY, 1, i -> 1);
        this.VEIN_MINER = new AbilityGroup<>("Vein Miner", new VeinMinerAbility(1, 4), new VeinMinerAbility(1, 8), new VeinMinerAbility(1, 16), new VeinMinerAbility(1, 32), new VeinMinerAbility(1, 64));
        this.SELF_SUSTAIN = new AbilityGroup<>("Self Sustain", new SelfSustainAbility(3, 1), new SelfSustainAbility(3, 2), new SelfSustainAbility(3, 4));
        this.DASH = new AbilityGroup<>("Dash", new DashAbility(3, 1), new DashAbility(3, 2), new DashAbility(3, 3), new DashAbility(3, 4), new DashAbility(3, 5), new DashAbility(3, 6), new DashAbility(3, 7), new DashAbility(3, 8), new DashAbility(3, 9), new DashAbility(3, 10));
    }

}
