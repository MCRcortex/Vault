package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.block.VaultDoorBlock;
import iskallia.vault.block.VaultOreBlock;
import iskallia.vault.block.VaultPortalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.TallBlockItem;
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
    public static final DoorBlock ALEXANDRITE_DOOR = new VaultDoorBlock(ModItems.POG);
    public static final DoorBlock BENITOITE_DOOR = new VaultDoorBlock(ModItems.POG);
    public static final DoorBlock LARIMAR_DOOR = new VaultDoorBlock(ModItems.POG);
    public static final DoorBlock BLACK_OPAL_DOOR = new VaultDoorBlock(ModItems.POG);
    public static final DoorBlock PAINITE_DOOR = new VaultDoorBlock(ModItems.POG);
    public static final DoorBlock ISKALLIUM_DOOR = new VaultDoorBlock(ModItems.POG);
    public static final DoorBlock RENIUM_DOOR = new VaultDoorBlock(ModItems.POG);
    public static final DoorBlock GORGINITE_DOOR = new VaultDoorBlock(ModItems.POG);
    public static final DoorBlock SPARKLETINE_DOOR = new VaultDoorBlock(ModItems.POG);
    public static final DoorBlock WUTODIE_DOOR = new VaultDoorBlock(ModItems.POG);

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
        registerBlock(event, ALEXANDRITE_DOOR, Vault.id("door_alexandrite"));
        registerBlock(event, BENITOITE_DOOR, Vault.id("door_benitoite"));
        registerBlock(event, LARIMAR_DOOR, Vault.id("door_larimar"));
        registerBlock(event, BLACK_OPAL_DOOR, Vault.id("door_black_opal"));
        registerBlock(event, PAINITE_DOOR, Vault.id("door_painite"));
        registerBlock(event, ISKALLIUM_DOOR, Vault.id("door_iskallium"));
        registerBlock(event, RENIUM_DOOR, Vault.id("door_renium"));
        registerBlock(event, GORGINITE_DOOR, Vault.id("door_gorginite"));
        registerBlock(event, SPARKLETINE_DOOR, Vault.id("door_sparkletine"));
        registerBlock(event, WUTODIE_DOOR, Vault.id("door_wutodie"));
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
        registerTallBlockItem(event, ALEXANDRITE_DOOR);
        registerTallBlockItem(event, BENITOITE_DOOR);
        registerTallBlockItem(event, LARIMAR_DOOR);
        registerTallBlockItem(event, BLACK_OPAL_DOOR);
        registerTallBlockItem(event, PAINITE_DOOR);
        registerTallBlockItem(event, ISKALLIUM_DOOR);
        registerTallBlockItem(event, RENIUM_DOOR);
        registerTallBlockItem(event, GORGINITE_DOOR);
        registerTallBlockItem(event, SPARKLETINE_DOOR);
        registerTallBlockItem(event, WUTODIE_DOOR);
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

    private static void registerTallBlockItem(RegistryEvent.Register<Item> event, Block block) {
        TallBlockItem tallBlockItem = new TallBlockItem(block, new Item.Properties()
                .group(ModItems.VAULT_MOD_GROUP)
                .maxStackSize(64));
        tallBlockItem.setRegistryName(block.getRegistryName());
        event.getRegistry().register(tallBlockItem);
    }

}
