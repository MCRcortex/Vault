package iskallia.vault.entity.renderer;

import iskallia.vault.entity.ArenaTrackerEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;

public class ArenaTrackerRenderer extends EntityRenderer<ArenaTrackerEntity> {

	public ArenaTrackerRenderer(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public ResourceLocation getEntityTexture(ArenaTrackerEntity entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}

}

