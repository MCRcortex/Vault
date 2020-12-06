package iskallia.vault.entity.renderer;

import iskallia.vault.Vault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.ResourceLocation;

public class RobotRenderer extends IronGolemRenderer {

    public static final ResourceLocation TEXTURE = Vault.id("textures/entity/robot.png");

    public RobotRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(IronGolemEntity entity) {
        return TEXTURE;
    }

}
