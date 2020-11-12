package iskallia.vault.block.render;

import java.util.Map;
import java.util.UUID;

import com.mojang.blaze3d.matrix.MatrixStack;

import iskallia.vault.altar.PedestalItem;
import iskallia.vault.block.entity.VaultAltarTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.LightType;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class VaultAltarRenderer extends TileEntityRenderer<VaultAltarTileEntity> {

	private float currentTick = 0;

	public VaultAltarRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(VaultAltarTileEntity altar, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		Minecraft mc = Minecraft.getInstance();

		PedestalItem[] items = null;
		Map<UUID, PedestalItem[]> map = altar.getNearbyPlayers();
		for (UUID id : map.keySet()) {
			if (id.equals(mc.player.getUniqueID())) {
				items = map.get(id);
				break;
			}
		}
		if (items == null)
			return;
		currentTick = mc.player.ticksExisted;
		float angle = (currentTick + partialTicks) % 360;
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		int blockLight = altar.getWorld().getLightFor(LightType.BLOCK, altar.getPos().up());
		int skyLight = altar.getWorld().getLightFor(LightType.SKY, altar.getPos().up());
		int lightLevel = LightTexture.packLight(blockLight, skyLight);
		for (int i = 0; i < items.length; i++) {
			matrixStack.push();
			double[] corner = getCorner(i);
			matrixStack.translate(corner[0], corner[1], corner[2]);
			matrixStack.rotate(Vector3f.YP.rotationDegrees(angle * 50f));
			ItemStack itemStack = items[i].getItem();
			IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(itemStack, altar.getWorld(), null);

			itemRenderer.renderItem(itemStack, TransformType.GROUND, true, matrixStack, buffer, lightLevel, combinedOverlay, ibakedmodel);

			matrixStack.pop();
		}
		matrixStack.push();
		matrixStack.translate(.5f, 1.1f, .5f);
		// matrixStack.rotate(Vector3f.YP.rotationDegrees(angle * 20f));
		ItemStack itemStack = new ItemStack(ModItems.VAULT_ROCK);
		IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(itemStack, altar.getWorld(), null);

		itemRenderer.renderItem(itemStack, TransformType.GROUND, true, matrixStack, buffer, lightLevel, combinedOverlay, ibakedmodel);

		matrixStack.pop();

	}

	private double[] getCorner(int index) {
		switch (index) {
		case 0:
			return new double[] { .9, 1.5, 0.1 };
		case 1:
			return new double[] { .9, 1.5, .9 };
		case 2:
			return new double[] { 0.1, 1.5, .9 };
		default:
			return new double[] { 0.1, 1.5, 0.1 };
		}
	}

	public static void register() {
		ClientRegistry.bindTileEntityRenderer(ModBlocks.VAULT_ALTAR_TILE_ENTITY, VaultAltarRenderer::new);
	}

}
