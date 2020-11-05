package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.ability.AbilityFrame;
import iskallia.vault.config.entry.SkillStyle;

import java.util.HashMap;

public class ResearchesGUIConfig extends Config {

    @Expose private HashMap<String, SkillStyle> styles;

    @Override
    public String getName() {
        return "researches_gui_styles";
    }

    public HashMap<String, SkillStyle> getStyles() {
        return styles;
    }

    @Override
    protected void reset() {
        SkillStyle style;
        this.styles = new HashMap<>();

        style = new SkillStyle(0, 0, 0, 0);
        style.frameType = AbilityFrame.RECTANGULAR;
        styles.put("Vault Knowledge", style);
    }

}
