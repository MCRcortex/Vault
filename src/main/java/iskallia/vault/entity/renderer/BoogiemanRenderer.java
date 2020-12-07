package iskallia.vault.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;

public class BoogiemanRenderer extends ZombieRenderer {

    public static final ResourceLocation TEXTURE = Vault.id("textures/entity/boogieman.png");

    public BoogiemanRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected void preRenderCallback(ZombieEntity entitylivingbase, MatrixStack matrixStack, float partialTickTime) {
        super.preRenderCallback(entitylivingbase, matrixStack, partialTickTime);
        matrixStack.scale(2, 2, 2);
    }

    @Override
    public ResourceLocation getEntityTexture(ZombieEntity entity) {
        return TEXTURE;
    }

}
