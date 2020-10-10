package iskallia.vault.world.data;

import iskallia.vault.Vault;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

public class VaultRaidData extends WorldSavedData {

	protected static final String DATA_NAME = Vault.MOD_ID + "_PlayerAbilities";



	public VaultRaidData() {
		this(DATA_NAME);
	}

	public VaultRaidData(String name) {
		super(name);
	}

	@Override
	public void read(CompoundNBT nbt) {

	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		return null;
	}

	public static PlayerAbilitiesData get(ServerWorld world) {
		return world.getServer().func_241755_D_().getSavedData().getOrCreate(PlayerAbilitiesData::new, DATA_NAME);
	}

}
