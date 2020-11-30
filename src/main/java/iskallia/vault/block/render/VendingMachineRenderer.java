package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.block.entity.VendingMachineTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class VendingMachineRenderer extends TileEntityRenderer<VendingMachineTileEntity> {

    public VendingMachineRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(VendingMachineTileEntity altar, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {


    }

    private int getLightAtPos(World world, BlockPos pos) {
        int blockLight = world.getLightFor(LightType.BLOCK, pos);
        int skyLight = world.getLightFor(LightType.SKY, pos);
        return LightTexture.packLight(blockLight, skyLight);
    }

}
