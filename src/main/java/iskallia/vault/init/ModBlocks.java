package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.block.*;
import iskallia.vault.block.entity.VaultAltarTileEntity;
import iskallia.vault.block.entity.VaultRuneTileEntity;
import iskallia.vault.block.render.VaultAltarRenderer;
import iskallia.vault.block.render.VaultRuneRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.TallBlockItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModBlocks {

    public static final VaultPortalBlock VAULT_PORTAL = new VaultPortalBlock();
    public static final VaultAltarBlock VAULT_ALTAR = new VaultAltarBlock();
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
    public static final DoorBlock ALEXANDRITE_DOOR = new VaultDoorBlock(ModItems.ALEXANDRITE_KEY);
    public static final DoorBlock BENITOITE_DOOR = new VaultDoorBlock(ModItems.BENITOITE_KEY);
    public static final DoorBlock LARIMAR_DOOR = new VaultDoorBlock(ModItems.LARIMAR_KEY);
    public static final DoorBlock BLACK_OPAL_DOOR = new VaultDoorBlock(ModItems.BLACK_OPAL_KEY);
    public static final DoorBlock PAINITE_DOOR = new VaultDoorBlock(ModItems.PAINITE_KEY);
    public static final DoorBlock ISKALLIUM_DOOR = new VaultDoorBlock(ModItems.ISKALLIUM_KEY);
    public static final DoorBlock RENIUM_DOOR = new VaultDoorBlock(ModItems.RENIUM_KEY);
    public static final DoorBlock GORGINITE_DOOR = new VaultDoorBlock(ModItems.GORGINITE_KEY);
    public static final DoorBlock SPARKLETINE_DOOR = new VaultDoorBlock(ModItems.SPARKLETINE_KEY);
    public static final DoorBlock WUTODIE_DOOR = new VaultDoorBlock(ModItems.WUTODIE_KEY);
    public static final VaultRuneBlock VAULT_RUNE_BLOCK = new VaultRuneBlock();

    public static final TileEntityType<VaultAltarTileEntity> VAULT_ALTAR_TILE_ENTITY = TileEntityType.Builder.create(VaultAltarTileEntity::new, VAULT_ALTAR).build(null);
    public static final TileEntityType<VaultRuneTileEntity> VAULT_RUNE_TILE_ENTITY = TileEntityType.Builder.create(VaultRuneTileEntity::new, VAULT_RUNE_BLOCK).build(null);

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        registerBlock(event, VAULT_PORTAL, Vault.id("vault_portal"));
        registerBlock(event, VAULT_ALTAR, Vault.id("vault_altar"));
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
        registerBlock(event, VAULT_RUNE_BLOCK, Vault.id("vault_rune_block"));
    }

    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        registerTileEntity(event, VAULT_ALTAR_TILE_ENTITY, Vault.id("vault_altar_tile_entity"));
        registerTileEntity(event, VAULT_RUNE_TILE_ENTITY, Vault.id("vault_rune_tile_entity"));
    }

    public static void registerTileEntityRenderers() {
        ClientRegistry.bindTileEntityRenderer(ModBlocks.VAULT_ALTAR_TILE_ENTITY, VaultAltarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.VAULT_RUNE_TILE_ENTITY, VaultRuneRenderer::new);
    }

    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        registerBlockItem(event, VAULT_PORTAL);
        registerBlockItem(event, VAULT_ALTAR);
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
        registerBlockItem(event, VAULT_RUNE_BLOCK);
    }

    private static void registerBlock(RegistryEvent.Register<Block> event, Block block, ResourceLocation id) {
        block.setRegistryName(id);
        event.getRegistry().register(block);
    }

    private static <T extends TileEntity> void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event, TileEntityType<?> type, ResourceLocation id) {
        type.setRegistryName(id);
        event.getRegistry().register(type);
    }

    private static void registerBlockItem(RegistryEvent.Register<Item> event, Block block) {
        BlockItem blockItem = new BlockItem(block, new Item.Properties().group(ModItems.VAULT_MOD_GROUP).maxStackSize(64));
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
