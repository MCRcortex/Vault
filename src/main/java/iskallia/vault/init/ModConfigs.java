package iskallia.vault.init;

import iskallia.vault.config.AbilitiesConfig;

public class ModConfigs {

	public static AbilitiesConfig ABILITIES;

	public static void register() {
		ABILITIES = (AbilitiesConfig)new AbilitiesConfig().readConfig();
	}

}
