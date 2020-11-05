package iskallia.vault.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class VaultAltarBlock extends Block {

	public VaultAltarBlock() {
		super(Properties.create(Material.ROCK, MaterialColor.DIAMOND)
				.setRequiresTool()
				.hardnessAndResistance(3f, 3f)
		);
	}
}
