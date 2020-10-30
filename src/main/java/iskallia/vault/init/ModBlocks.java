package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.block.VaultPortalBlock;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class ModBlocks {

	public static final VaultPortalBlock VAULT_PORTAL = new VaultPortalBlock();

	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		register(event, VAULT_PORTAL, Vault.id("vault_portal"));
	}

	private static void register(RegistryEvent.Register<Block> event, Block block, ResourceLocation id) {
		block.setRegistryName(id);
		event.getRegistry().register(block);
	}

}
