package iskallia.vault.skill.ability.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.talent.type.EffectTalent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class EffectAbility extends PlayerAbility {

    @Expose private final String effect;
    @Expose private final int amplifier;
    @Expose private final String type;

    public EffectAbility(int cost, Effect effect, int amplifier, Type type) {
        this(cost, Registry.EFFECTS.getKey(effect).toString(), amplifier, type.toString());
    }

    public Effect getEffect() {
        return Registry.EFFECTS.getOrDefault(new ResourceLocation(this.effect));
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public EffectTalent.Type getType() {
        return EffectTalent.Type.fromString(this.type);
    }

    public EffectAbility(int cost, String effect, int amplifier, String type) {
        super(cost, Behavior.PRESS_TO_TOGGLE);
        this.effect = effect;
        this.amplifier = amplifier;
        this.type = type;
    }

    @Override
    public void onRemoved(PlayerEntity player) {
        player.removePotionEffect(this.getEffect());
    }

    @Override
    public void onBlur(PlayerEntity player) {
        player.removePotionEffect(this.getEffect());
    }

    @Override
    public void onTick(PlayerEntity player, boolean active) {
        if (!active) {
            player.removePotionEffect(this.getEffect());

        } else {
            EffectInstance activeEffect = player.getActivePotionEffect(this.getEffect());
            EffectInstance newEffect = new EffectInstance(this.getEffect(),
                    Integer.MAX_VALUE, this.getAmplifier(), false,
                    this.getType().showParticles, this.getType().showIcon);

            if (activeEffect == null) {
                player.addPotionEffect(newEffect);
            }
        }
    }

    @Override
    public void onAction(PlayerEntity player, boolean active) { }

    public enum Type {
        HIDDEN("hidden", false, false),
        PARTICLES_ONLY("particles_only", true, false),
        ICON_ONLY("icon_only", false, true),
        ALL("all", true, true);

        private static Map<String, EffectAbility.Type> STRING_TO_TYPE = Arrays.stream(values())
                .collect(Collectors.toMap(EffectAbility.Type::toString, o -> o));

        private final String name;
        public final boolean showParticles;
        public final boolean showIcon;

        Type(String name, boolean showParticles, boolean showIcon) {
            this.name = name;
            this.showParticles = showParticles;
            this.showIcon = showIcon;
        }

        public static EffectAbility.Type fromString(String type) {
            return STRING_TO_TYPE.get(type);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

}
