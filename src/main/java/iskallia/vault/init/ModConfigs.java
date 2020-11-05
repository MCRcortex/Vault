package iskallia.vault.init;

import iskallia.vault.config.*;

public class ModConfigs {

    public static TalentsConfig TALENTS;
    public static TalentsGUIConfig TALENTS_GUI;
    public static ResearchConfig RESEARCHES;
    public static ResearchesGUIConfig RESEARCHES_GUI;
    public static VaultLevelsConfig LEVELS_META;
    public static VaultOreConfig VAULT_ORES;
    public static VaultScaleConfig VAULT_SCALE;
    public static VaultItemsConfig VAULT_ITEMS;

    public static void register() {
        TALENTS = (TalentsConfig) new TalentsConfig().readConfig();
        TALENTS_GUI = (TalentsGUIConfig) new TalentsGUIConfig().readConfig();
        RESEARCHES = (ResearchConfig) new ResearchConfig().readConfig();
        RESEARCHES_GUI = (ResearchesGUIConfig) new ResearchesGUIConfig().readConfig();
        LEVELS_META = (VaultLevelsConfig) new VaultLevelsConfig().readConfig();
        VAULT_ORES = (VaultOreConfig) new VaultOreConfig().readConfig();
        VAULT_SCALE = (VaultScaleConfig) new VaultScaleConfig().readConfig();
        VAULT_ITEMS = (VaultItemsConfig) new VaultItemsConfig().readConfig();
    }

}
