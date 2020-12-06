package iskallia.vault.entity.renderer;

import iskallia.vault.Vault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.util.ResourceLocation;

public class MonsterEyeRenderer extends SlimeRenderer {

    public static final ResourceLocation TEXTURE = Vault.id("textures/entity/monster_eye.png");

    public MonsterEyeRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(SlimeEntity entity) {
        return TEXTURE;
    }

}
