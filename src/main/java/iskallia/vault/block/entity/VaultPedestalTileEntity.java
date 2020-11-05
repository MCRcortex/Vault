package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class VaultPedestalTileEntity extends TileEntity {

	public VaultPedestalTileEntity(TileEntityType<?> type) {
		super(type);
	}

	public VaultPedestalTileEntity() {
		this(ModBlocks.VAULT_PEDESTAL_TILE_ENTITY);
	}

}
