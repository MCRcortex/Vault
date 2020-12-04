package iskallia.vault.config;

import com.google.gson.annotations.Expose;

public class ArenaGeneralConfig extends Config {

	@Expose public int BOSS_COUNT;
	@Expose public float BOSS_SIZE;

	@Override
	public String getName() {
		return "arena_general";
	}

	@Override
	protected void reset() {
		this.BOSS_COUNT = 3;
		this.BOSS_SIZE = 3.0F;
	}

}
