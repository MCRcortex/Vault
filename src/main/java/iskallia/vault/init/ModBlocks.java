package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.block.VaultOreBlock;
import iskallia.vault.block.VaultPortalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.LootFunctionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class ModBlocks {

    public static final VaultPortalBlock VAULT_PORTAL = new VaultPortalBlock();
    public static final OreBlock ALEXANDRITE_ORE = new VaultOreBlock();
    public static final OreBlock BENITOITE_ORE = new VaultOreBlock();
    public static final OreBlock LARIMAR_ORE = new VaultOreBlock();
    public static final OreBlock BLACK_OPAL_ORE = new VaultOreBlock();
    public static final OreBlock PAINITE_ORE = new VaultOreBlock();
    public static final OreBlock ISKALLIUM_ORE = new VaultOreBlock();
    public static final OreBlock RENIUM_ORE = new VaultOreBlock();
    public static final OreBlock GORGINITE_ORE = new VaultOreBlock();
    public static final OreBlock SPARKLETINE_ORE = new VaultOreBlock();
    public static final OreBlock WUTODIE_ORE = new VaultOreBlock();
    public static final OreBlock VAULT_ROCK_ORE = new VaultOreBlock();

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        registerBlock(event, VAULT_PORTAL, Vault.id("vault_portal"));
        registerBlock(event, ALEXANDRITE_ORE, Vault.id("ore_alexandrite"));
        registerBlock(event, BENITOITE_ORE, Vault.id("ore_benitoite"));
        registerBlock(event, LARIMAR_ORE, Vault.id("ore_larimar"));
        registerBlock(event, BLACK_OPAL_ORE, Vault.id("ore_black_opal"));
        registerBlock(event, PAINITE_ORE, Vault.id("ore_painite"));
        registerBlock(event, ISKALLIUM_ORE, Vault.id("ore_iskallium"));
        registerBlock(event, RENIUM_ORE, Vault.id("ore_renium"));
        registerBlock(event, GORGINITE_ORE, Vault.id("ore_gorginite"));
        registerBlock(event, SPARKLETINE_ORE, Vault.id("ore_sparkletine"));
        registerBlock(event, WUTODIE_ORE, Vault.id("ore_wutodie"));
        registerBlock(event, VAULT_ROCK_ORE, Vault.id("ore_vault_rock"));
    }

    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        registerBlockItem(event, VAULT_PORTAL);
        registerBlockItem(event, ALEXANDRITE_ORE);
        registerBlockItem(event, BENITOITE_ORE);
        registerBlockItem(event, LARIMAR_ORE);
        registerBlockItem(event, BLACK_OPAL_ORE);
        registerBlockItem(event, PAINITE_ORE);
        registerBlockItem(event, ISKALLIUM_ORE);
        registerBlockItem(event, RENIUM_ORE);
        registerBlockItem(event, GORGINITE_ORE);
        registerBlockItem(event, SPARKLETINE_ORE);
        registerBlockItem(event, WUTODIE_ORE);
        registerBlockItem(event, VAULT_ROCK_ORE);
    }

    private static void registerBlock(RegistryEvent.Register<Block> event, Block block, ResourceLocation id) {
        block.setRegistryName(id);
        event.getRegistry().register(block);
    }

    private static void registerBlockItem(RegistryEvent.Register<Item> event, Block block) {
        BlockItem blockItem = new BlockItem(block, new Item.Properties()
                .group(ModItems.VAULT_MOD_GROUP)
                .maxStackSize(64)
        );
        blockItem.setRegistryName(block.getRegistryName());
        event.getRegistry().register(blockItem);
    }

}
