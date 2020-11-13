package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.entity.FighterEntity;
import iskallia.vault.entity.renderer.FighterRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Supplier;

public class ModEntities {

	public static EntityType<FighterEntity> FIGHTER;

	public static void register(RegistryEvent.Register<EntityType<?>> event) {
		FIGHTER = register("fighter", EntityType.Builder.create(FighterEntity::new, EntityClassification.MONSTER).size(0.6F, 1.95F), ZombieEntity::func_234342_eQ_, event);
	}

	public static <T extends LivingEntity> EntityType<T> register(String name, EntityType.Builder<T> builder, Supplier<AttributeModifierMap.MutableAttribute> attributes, RegistryEvent.Register<EntityType<?>> event) {
		EntityType<T> entityType = builder.build(Vault.sId(name));
		event.getRegistry().register(entityType.setRegistryName(Vault.id(name)));
		GlobalEntityTypeAttributes.put(entityType, attributes.get().create());
		return entityType;
	}

	public static class Renderers {

		public static void register(FMLClientSetupEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(FIGHTER, FighterRenderer::new);
		}

	}

}
