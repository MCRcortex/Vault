package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class VaultPedestalTileEntity extends TileEntity implements ITickableTileEntity {

	// playerName and all code relevant to it is simply testing that the tile entity is saved and loaded properly.
	
	private String playerName;

	private long tick = 0;

	public VaultPedestalTileEntity() {
		super(ModBlocks.VAULT_PEDESTAL_TILE_ENTITY);
	}

	@Override
	public void tick() {
		
		
		World world = this.getWorld();
		if (world.isRemote)
			return;
		if (tick++ == 19) {
			this.getWorld().getPlayers().forEach(p -> p.sendStatusMessage(new TranslationTextComponent(getPlayerName() + ": " + this.getTileEntity().hashCode()), false));
			tick = 0;
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.putString("name", getPlayerName());
		return super.write(compound);
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		setPlayerName(nbt.getString("name"));
		super.read(state, nbt);
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return this.playerName;
	}

}
