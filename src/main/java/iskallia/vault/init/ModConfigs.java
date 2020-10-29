package iskallia.vault.init;

import iskallia.vault.config.*;

public class ModConfigs {

    public static AbilitiesConfig ABILITIES;
    public static AbilitiesGUIConfig ABILITIES_GUI;
    public static VaultLevelsConfig LEVELS_META;
    public static VaultOreConfig VAULT_ORES;
    public static VaultScaleConfig VAULT_SCALE;
    public static VaultItemsConfig VAULT_ITEMS;

    public static void register() {
        ABILITIES = (AbilitiesConfig) new AbilitiesConfig().readConfig();
        ABILITIES_GUI = (AbilitiesGUIConfig) new AbilitiesGUIConfig().readConfig();
        LEVELS_META = (VaultLevelsConfig) new VaultLevelsConfig().readConfig();
        VAULT_ORES = (VaultOreConfig) new VaultOreConfig().readConfig();
        VAULT_SCALE = (VaultScaleConfig) new VaultScaleConfig().readConfig();
        VAULT_ITEMS = (VaultItemsConfig) new VaultItemsConfig().readConfig();
    }

}
