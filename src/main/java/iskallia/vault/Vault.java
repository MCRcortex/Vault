package iskallia.vault;

import iskallia.vault.init.ModConfigs;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Vault.MOD_ID)
public class Vault {

	public static final String MOD_ID = "the_vault";
	public static final Logger LOGGER = LogManager.getLogger();

	public Vault() {
		ModConfigs.register();
	}

}
