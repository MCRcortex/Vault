package iskallia.vault.config;

import com.google.gson.annotations.Expose;

public class VaultGeneralConfig extends Config {

	@Expose private int TICK_COUNTER;

	@Override
	public String getName() {
		return "vault_general";
	}

	public int getTickCounter() {
		return this.TICK_COUNTER;
	}

	@Override
	protected void reset() {
		this.TICK_COUNTER = 20 * 60 * 25;
	}

}
