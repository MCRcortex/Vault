package iskallia.vault.init;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public class ModModels {

    public static void setupRenderLayers() {
        RenderTypeLookup.setRenderLayer(ModBlocks.ALEXANDRITE_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.BENITOITE_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.LARIMAR_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.BLACK_OPAL_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.PAINITE_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ISKALLIUM_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.RENIUM_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.GORGINITE_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.SPARKLETINE_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.WUTODIE_DOOR, RenderType.getCutout());
    }

}
