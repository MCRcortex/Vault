package iskallia.vault.init;

import iskallia.vault.config.*;

public class ModConfigs {

	public static AbilitiesConfig ABILITIES;
	public static AbilitiesGUIConfig ABILITIES_GUI;
	public static TalentsConfig TALENTS;
	public static TalentsGUIConfig TALENTS_GUI;
	public static ResearchConfig RESEARCHES;
	public static ResearchesGUIConfig RESEARCHES_GUI;
	public static SkillDescriptionsConfig SKILL_DESCRIPTIONS;
    public static VaultLevelsConfig LEVELS_META;
	public static VaultOreConfig VAULT_ORES;
	public static VaultMobsConfig VAULT_MOBS;
	public static VaultItemsConfig VAULT_ITEMS;
	public static VaultAltarConfig VAULT_ALTAR;
    public static VaultGeneralConfig VAULT_GENERAL;

	public static void register() {
		ABILITIES = (AbilitiesConfig) new AbilitiesConfig().readConfig();
		ABILITIES_GUI = (AbilitiesGUIConfig) new AbilitiesGUIConfig().readConfig();
		TALENTS = (TalentsConfig) new TalentsConfig().readConfig();
		TALENTS_GUI = (TalentsGUIConfig) new TalentsGUIConfig().readConfig();
		RESEARCHES = (ResearchConfig) new ResearchConfig().readConfig();
		RESEARCHES_GUI = (ResearchesGUIConfig) new ResearchesGUIConfig().readConfig();
		SKILL_DESCRIPTIONS = (SkillDescriptionsConfig) new SkillDescriptionsConfig().readConfig();LEVELS_META = (VaultLevelsConfig) new VaultLevelsConfig().readConfig();
		VAULT_ORES = (VaultOreConfig) new VaultOreConfig().readConfig();
		VAULT_MOBS = (VaultMobsConfig) new VaultMobsConfig().readConfig();
		VAULT_ITEMS = (VaultItemsConfig) new VaultItemsConfig().readConfig();
		VAULT_ALTAR = (VaultAltarConfig) new VaultAltarConfig().readConfig();
        VAULT_GENERAL = (VaultGeneralConfig) new VaultGeneralConfig().readConfig();
	}


}
