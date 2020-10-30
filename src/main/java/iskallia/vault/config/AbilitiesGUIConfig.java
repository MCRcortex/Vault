package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.ability.AbilityFrame;
import iskallia.vault.init.ModConfigs;

import java.util.HashMap;

public class AbilitiesGUIConfig extends Config {

    @Expose private HashMap<String, AbilityStyle> styles;

    @Override
    public String getName() {
        return "abilities_gui_styles";
    }

    public HashMap<String, AbilityStyle> getStyles() {
        return styles;
    }

    @Override
    protected void reset() {
        AbilityStyle style;
        this.styles = new HashMap<>();

        style = new AbilityStyle();
        style.x = 0;
        style.y = 0;
        style.u = 0;
        style.v = 0;
        style.frameType = AbilityFrame.RECTANGULAR;
        styles.put(ModConfigs.ABILITIES.HASTE.getParentName(), style);

        style = new AbilityStyle();
        style.x = 50;
        style.y = 0;
        style.u = 16 * 3;
        style.v = 0;
        style.frameType = AbilityFrame.RECTANGULAR;
        styles.put(ModConfigs.ABILITIES.REGENERATION.getParentName(), style);

        style = new AbilityStyle();
        style.x = 50 * 2;
        style.y = 0;
        style.u = 16;
        style.v = 0;
        style.frameType = AbilityFrame.RECTANGULAR;
        styles.put(ModConfigs.ABILITIES.VAMPIRISM.getParentName(), style);

        style = new AbilityStyle();
        style.x = 50 * 3;
        style.y = 0;
        style.u = 0;
        style.v = 0;
        style.frameType = AbilityFrame.RECTANGULAR;
        styles.put(ModConfigs.ABILITIES.RESISTANCE.getParentName(), style);

        style = new AbilityStyle();
        style.x = 50 * 4;
        style.y = 0;
        style.u = 0;
        style.v = 0;
        style.frameType = AbilityFrame.RECTANGULAR;
        styles.put(ModConfigs.ABILITIES.STRENGTH.getParentName(), style);

        style = new AbilityStyle();
        style.x = 50 * 5;
        style.y = 0;
        style.u = 16 * 4;
        style.v = 0;
        style.frameType = AbilityFrame.RECTANGULAR;
        styles.put(ModConfigs.ABILITIES.FIRE_RESISTANCE.getParentName(), style);

        style = new AbilityStyle();
        style.x = 0;
        style.y = 50;
        style.u = 0;
        style.v = 0;
        style.frameType = AbilityFrame.RECTANGULAR;
        styles.put(ModConfigs.ABILITIES.SPEED.getParentName(), style);

        style = new AbilityStyle();
        style.x = 50;
        style.y = 50;
        style.u = 16 * 4;
        style.v = 0;
        style.frameType = AbilityFrame.RECTANGULAR;
        styles.put(ModConfigs.ABILITIES.WATER_BREATHING.getParentName(), style);

        style = new AbilityStyle();
        style.x = 50 * 2;
        style.y = 50;
        style.u = 0;
        style.v = 0;
        style.frameType = AbilityFrame.RECTANGULAR;
        styles.put(ModConfigs.ABILITIES.WELL_FIT.getParentName(), style);

        style = new AbilityStyle();
        style.x = 50 * 3;
        style.y = 50;
        style.u = 0;
        style.v = 0;
        style.frameType = AbilityFrame.RECTANGULAR;
        styles.put(ModConfigs.ABILITIES.TWERKER.getParentName(), style);

        style = new AbilityStyle();
        style.x = 50 * 4;
        style.y = 50;
        style.u = 0;
        style.v = 0;
        style.frameType = AbilityFrame.RECTANGULAR;
        styles.put(ModConfigs.ABILITIES.ELVISH.getParentName(), style);

        style = new AbilityStyle();
        style.x = 50 * 5;
        style.y = 50;
        style.u = 16 * 5;
        style.v = 0;
        style.frameType = AbilityFrame.RECTANGULAR;
        styles.put(ModConfigs.ABILITIES.ANGEL.getParentName(), style);
    }

    public static class AbilityStyle {
        @Expose public int x, y;
        @Expose public AbilityFrame frameType;
        @Expose public int u, v;
    }

}
