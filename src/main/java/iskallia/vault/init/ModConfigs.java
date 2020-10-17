package iskallia.vault.init;

import iskallia.vault.config.AbilitiesConfig;
import iskallia.vault.config.AbilitiesGUIConfig;
import iskallia.vault.config.VaultLevelsConfig;

public class ModConfigs {

    public static AbilitiesConfig ABILITIES;
    public static AbilitiesGUIConfig ABILITIES_GUI;
    public static VaultLevelsConfig LEVELS_META;

    public static void register() {
        ABILITIES = (AbilitiesConfig) new AbilitiesConfig().readConfig();
        ABILITIES_GUI = (AbilitiesGUIConfig) new AbilitiesGUIConfig().readConfig();
        LEVELS_META = (VaultLevelsConfig) new VaultLevelsConfig().readConfig();
    }

}
