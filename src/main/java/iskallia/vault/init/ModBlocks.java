package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.block.*;
import iskallia.vault.block.entity.VaultAltarTileEntity;
import iskallia.vault.block.entity.VaultCrateTileEntity;
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
    public static final VaultArtifactBlock ARTIFACT_1 = new VaultArtifactBlock(1);
    public static final VaultArtifactBlock ARTIFACT_2 = new VaultArtifactBlock(2);
    public static final VaultArtifactBlock ARTIFACT_3 = new VaultArtifactBlock(3);
    public static final VaultArtifactBlock ARTIFACT_4 = new VaultArtifactBlock(4);
    public static final VaultArtifactBlock ARTIFACT_5 = new VaultArtifactBlock(5);
    public static final VaultArtifactBlock ARTIFACT_6 = new VaultArtifactBlock(6);
    public static final VaultArtifactBlock ARTIFACT_7 = new VaultArtifactBlock(7);
    public static final VaultArtifactBlock ARTIFACT_8 = new VaultArtifactBlock(8);
    public static final VaultArtifactBlock ARTIFACT_9 = new VaultArtifactBlock(9);
    public static final VaultArtifactBlock ARTIFACT_10 = new VaultArtifactBlock(10);
    public static final VaultArtifactBlock ARTIFACT_11 = new VaultArtifactBlock(11);
    public static final VaultArtifactBlock ARTIFACT_12 = new VaultArtifactBlock(12);
    public static final VaultArtifactBlock ARTIFACT_13 = new VaultArtifactBlock(13);
    public static final VaultArtifactBlock ARTIFACT_14 = new VaultArtifactBlock(14);
    public static final VaultArtifactBlock ARTIFACT_15 = new VaultArtifactBlock(15);
    public static final VaultArtifactBlock ARTIFACT_16 = new VaultArtifactBlock(16);
    public static final VaultCrateBlock VAULT_CRATE_NORMAL = new VaultCrateBlock();
    public static final VaultCrateBlock VAULT_CRATE_RARE = new VaultCrateBlock();
    public static final VaultCrateBlock VAULT_CRATE_EPIC = new VaultCrateBlock();
    public static final VaultCrateBlock VAULT_CRATE_OMEGA = new VaultCrateBlock();
    public static final VaultCrateBlock VAULT_CRATE_ARENA = new VaultCrateBlock();
    public static final ObeliskBlock OBELISK = new ObeliskBlock();

    public static final TileEntityType<VaultAltarTileEntity> VAULT_ALTAR_TILE_ENTITY = TileEntityType.Builder.create(VaultAltarTileEntity::new, VAULT_ALTAR).build(null);
    public static final TileEntityType<VaultRuneTileEntity> VAULT_RUNE_TILE_ENTITY = TileEntityType.Builder.create(VaultRuneTileEntity::new, VAULT_RUNE_BLOCK).build(null);
    public static final TileEntityType<VaultCrateTileEntity> VAULT_CRATE_TILE_ENTITY = TileEntityType.Builder.
            create(VaultCrateTileEntity::new, VAULT_CRATE_NORMAL, VAULT_CRATE_RARE, VAULT_CRATE_EPIC, VAULT_CRATE_OMEGA, VAULT_CRATE_ARENA).build(null);

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
        registerBlock(event, ARTIFACT_1, Vault.id("artifact_1"));
        registerBlock(event, ARTIFACT_2, Vault.id("artifact_2"));
        registerBlock(event, ARTIFACT_3, Vault.id("artifact_3"));
        registerBlock(event, ARTIFACT_4, Vault.id("artifact_4"));
        registerBlock(event, ARTIFACT_5, Vault.id("artifact_5"));
        registerBlock(event, ARTIFACT_6, Vault.id("artifact_6"));
        registerBlock(event, ARTIFACT_7, Vault.id("artifact_7"));
        registerBlock(event, ARTIFACT_8, Vault.id("artifact_8"));
        registerBlock(event, ARTIFACT_9, Vault.id("artifact_9"));
        registerBlock(event, ARTIFACT_10, Vault.id("artifact_10"));
        registerBlock(event, ARTIFACT_11, Vault.id("artifact_11"));
        registerBlock(event, ARTIFACT_12, Vault.id("artifact_12"));
        registerBlock(event, ARTIFACT_13, Vault.id("artifact_13"));
        registerBlock(event, ARTIFACT_14, Vault.id("artifact_14"));
        registerBlock(event, ARTIFACT_15, Vault.id("artifact_15"));
        registerBlock(event, ARTIFACT_16, Vault.id("artifact_16"));
        registerBlock(event, VAULT_CRATE_NORMAL, Vault.id("vault_crate_normal"));
        registerBlock(event, VAULT_CRATE_RARE, Vault.id("vault_crate_rare"));
        registerBlock(event, VAULT_CRATE_EPIC, Vault.id("vault_crate_epic"));
        registerBlock(event, VAULT_CRATE_OMEGA, Vault.id("vault_crate_omega"));
        registerBlock(event, VAULT_CRATE_ARENA, Vault.id("vault_crate_arena"));
        registerBlock(event, OBELISK, Vault.id("obelisk"));
    }

    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        registerTileEntity(event, VAULT_ALTAR_TILE_ENTITY, Vault.id("vault_altar_tile_entity"));
        registerTileEntity(event, VAULT_RUNE_TILE_ENTITY, Vault.id("vault_rune_tile_entity"));
        registerTileEntity(event, VAULT_CRATE_TILE_ENTITY, Vault.id("vault_crate_tile_entity"));
    }

    public static void registerTileEntityRenderers() {
        ClientRegistry.bindTileEntityRenderer(ModBlocks.VAULT_ALTAR_TILE_ENTITY, VaultAltarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.VAULT_RUNE_TILE_ENTITY, VaultRuneRenderer::new);
    }

    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        registerBlockItem(event, VAULT_PORTAL);
        registerBlockItem(event, VAULT_ALTAR, 1);
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
        registerBlockItem(event, ARTIFACT_1, 1);
        registerBlockItem(event, ARTIFACT_2, 1);
        registerBlockItem(event, ARTIFACT_3, 1);
        registerBlockItem(event, ARTIFACT_4, 1);
        registerBlockItem(event, ARTIFACT_5, 1);
        registerBlockItem(event, ARTIFACT_6, 1);
        registerBlockItem(event, ARTIFACT_7, 1);
        registerBlockItem(event, ARTIFACT_8, 1);
        registerBlockItem(event, ARTIFACT_9, 1);
        registerBlockItem(event, ARTIFACT_10, 1);
        registerBlockItem(event, ARTIFACT_11, 1);
        registerBlockItem(event, ARTIFACT_12, 1);
        registerBlockItem(event, ARTIFACT_13, 1);
        registerBlockItem(event, ARTIFACT_14, 1);
        registerBlockItem(event, ARTIFACT_15, 1);
        registerBlockItem(event, ARTIFACT_16, 1);
        registerBlockItem(event, VAULT_CRATE_NORMAL, 1);
        registerBlockItem(event, VAULT_CRATE_RARE, 1);
        registerBlockItem(event, VAULT_CRATE_EPIC, 1);
        registerBlockItem(event, VAULT_CRATE_OMEGA, 1);
        registerBlockItem(event, VAULT_CRATE_ARENA, 1);
        registerBlockItem(event, OBELISK, 1);
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

    private static void registerBlockItem(RegistryEvent.Register<Item> event, Block block, int maxStackSize) {
        BlockItem blockItem = new BlockItem(block, new Item.Properties().group(ModItems.VAULT_MOD_GROUP).maxStackSize(maxStackSize));
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
