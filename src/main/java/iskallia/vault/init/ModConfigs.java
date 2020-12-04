package iskallia.vault.init;

import iskallia.vault.config.*;

public class ModConfigs {

    public static StreamerMultipliersConfig STREAMER_MULTIPLIERS;
    public static AbilitiesConfig ABILITIES;
    public static AbilitiesGUIConfig ABILITIES_GUI;
    public static TalentsConfig TALENTS;
    public static TalentsGUIConfig TALENTS_GUI;
    public static ResearchConfig RESEARCHES;
    public static ResearchesGUIConfig RESEARCHES_GUI;
    public static SkillDescriptionsConfig SKILL_DESCRIPTIONS;
    public static SkillGatesConfig SKILL_GATES;
    public static VaultLevelsConfig LEVELS_META;
    public static VaultRelicsConfig VAULT_RELICS;
    public static VaultOreConfig VAULT_ORES;
    public static VaultMobsConfig VAULT_MOBS;
    public static VaultItemsConfig VAULT_ITEMS;
    public static VaultAltarConfig VAULT_ALTAR;
    public static VaultGeneralConfig VAULT_GENERAL;
    public static VaultCrystalConfig VAULT_CRYSTAL;
    public static VaultPortalConfig VAULT_PORTAL;
    public static VaultVendingConfig VENDING_CONFIG;

    public static void register() {
        STREAMER_MULTIPLIERS = (StreamerMultipliersConfig) new StreamerMultipliersConfig().readConfig();
        ABILITIES = (AbilitiesConfig) new AbilitiesConfig().readConfig();
        ABILITIES_GUI = (AbilitiesGUIConfig) new AbilitiesGUIConfig().readConfig();
        TALENTS = (TalentsConfig) new TalentsConfig().readConfig();
        TALENTS_GUI = (TalentsGUIConfig) new TalentsGUIConfig().readConfig();
        RESEARCHES = (ResearchConfig) new ResearchConfig().readConfig();
        RESEARCHES_GUI = (ResearchesGUIConfig) new ResearchesGUIConfig().readConfig();
        SKILL_DESCRIPTIONS = (SkillDescriptionsConfig) new SkillDescriptionsConfig().readConfig();
        SKILL_GATES = (SkillGatesConfig) new SkillGatesConfig().readConfig();
        LEVELS_META = (VaultLevelsConfig) new VaultLevelsConfig().readConfig();
        VAULT_RELICS = (VaultRelicsConfig) new VaultRelicsConfig().readConfig();
        VAULT_ORES = (VaultOreConfig) new VaultOreConfig().readConfig();
        VAULT_MOBS = (VaultMobsConfig) new VaultMobsConfig().readConfig();
        VAULT_ITEMS = (VaultItemsConfig) new VaultItemsConfig().readConfig();
        VAULT_ALTAR = (VaultAltarConfig) new VaultAltarConfig().readConfig();
        VAULT_GENERAL = (VaultGeneralConfig) new VaultGeneralConfig().readConfig();
        VAULT_CRYSTAL = (VaultCrystalConfig) new VaultCrystalConfig().readConfig();
        VAULT_PORTAL = (VaultPortalConfig) new VaultPortalConfig().readConfig();
        VENDING_CONFIG = (VaultVendingConfig) new VaultVendingConfig().readConfig();
    }


}
