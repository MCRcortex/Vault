package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.altar.PedestalItem;
import iskallia.vault.block.entity.VaultAltarTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.Map;
import java.util.UUID;

public class VaultAltarRenderer extends TileEntityRenderer<VaultAltarTileEntity> {

    private float currentTick = 0;

    public VaultAltarRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(ModBlocks.VAULT_ALTAR_TILE_ENTITY, VaultAltarRenderer::new);
    }

    @Override
    public void render(VaultAltarTileEntity altar, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (!altar.containsVaultRock())
            return;

        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;

        Map<UUID, PedestalItem[]> nearbyPlayers = altar.getNearbyPlayers();
        if (!nearbyPlayers.containsKey(player.getUniqueID()))
            return;

        PedestalItem[] items = nearbyPlayers.get(player.getUniqueID());

        ItemRenderer itemRenderer = mc.getItemRenderer();
        FontRenderer fontRenderer = mc.fontRenderer;
        //float angle = getAngle(player, partialTicks);
        int lightLevel = getLightAtPos(altar.getWorld(), altar.getPos().up());

        for (int i = 0; i < items.length; i++) {

            //render item at index
            matrixStack.push();
            double[] corner = getCorner(i);
            matrixStack.translate(corner[0], corner[1], corner[2]);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F - player.rotationYaw));
            IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(items[i].getItem(), altar.getWorld(), null);
            itemRenderer.renderItem(items[i].getItem(), TransformType.GROUND, true, matrixStack, buffer, lightLevel, combinedOverlay, ibakedmodel);
            matrixStack.pop();

            //render amount required for the item
            matrixStack.push();
            StringTextComponent text = new StringTextComponent(String.valueOf(items[i].getAmountRequired()));
            float scale = 0.01f;
            int opacity = (int) (.4f * 255.0F) << 24;
            float offset = (float) (-fontRenderer.getStringPropertyWidth(text) / 2);
            Matrix4f matrix4f = matrixStack.getLast().getMatrix();

            matrixStack.translate(corner[0], corner[1] + .4f, corner[2]);
            matrixStack.scale(scale, scale, scale);
            matrixStack.rotate(mc.getRenderManager().getCameraOrientation());
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(180.0F));
            fontRenderer.func_243247_a(text, offset, 0, 0xffffff, false, matrix4f, buffer, false, opacity, lightLevel);
            matrixStack.pop();

        }

        //render vault rock
        matrixStack.push();
        matrixStack.translate(.5f, 1.75f, .5f);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F - player.rotationYaw));
        ItemStack vaultRock = new ItemStack(ModItems.VAULT_ROCK);
        IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(vaultRock, altar.getWorld(), null);
        itemRenderer.renderItem(vaultRock, TransformType.GROUND, true, matrixStack, buffer, lightLevel, combinedOverlay, ibakedmodel);
        matrixStack.pop();

    }

    private float getAngle(ClientPlayerEntity player, float partialTicks) {
        currentTick = player.ticksExisted;
        float angle = (currentTick + partialTicks) % 360;
        return angle;
    }

    private int getLightAtPos(World world, BlockPos pos) {
        int blockLight = world.getLightFor(LightType.BLOCK, pos);
        int skyLight = world.getLightFor(LightType.SKY, pos);
        return LightTexture.packLight(blockLight, skyLight);
    }

    private double[] getCorner(int index) {
        switch (index) {
            case 0:
                return new double[]{.9, 1.1, 0.1};
            case 1:
                return new double[]{.9, 1.1, .9};
            case 2:
                return new double[]{0.1, 1.1, .9};
            default:
                return new double[]{0.1, 1.1, 0.1};
        }
    }

}
