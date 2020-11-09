package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import iskallia.vault.block.entity.VaultPedestalTileEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.LightType;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class VaultPedestalRenderer extends TileEntityRenderer<VaultPedestalTileEntity> {

	private float currentTick = 0;

	public VaultPedestalRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(VaultPedestalTileEntity pedestal, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		ItemStack itemStack = pedestal.getItem();
		Minecraft mc = Minecraft.getInstance();

		if (itemStack == null || itemStack.isEmpty()) {
			return;
		}
		currentTick = mc.player.ticksExisted;
		 float angle = currentTick + partialTicks;
		matrixStack.push();
		matrixStack.translate(.5, 1.1, .5);
		matrixStack.rotate(Vector3f.YP.rotationDegrees(angle));
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(itemStack, pedestal.getWorld(), null);
		int blockLight = pedestal.getWorld().getLightFor(LightType.BLOCK, pedestal.getPos().up());
		int skyLight = pedestal.getWorld().getLightFor(LightType.SKY, pedestal.getPos().up());
		int lightLevel = LightTexture.packLight(blockLight, skyLight);
		itemRenderer.renderItem(itemStack, TransformType.GROUND, true, matrixStack, buffer, lightLevel, combinedOverlay, ibakedmodel);

		matrixStack.pop();
		
		

	}

	public static void register() {
		ClientRegistry.bindTileEntityRenderer(ModBlocks.VAULT_PEDESTAL_TILE_ENTITY, VaultPedestalRenderer::new);
	}

}
