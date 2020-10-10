package iskallia.vault.mixin;

import iskallia.vault.init.ModStructures;
import iskallia.vault.world.structure.IChunkGeneratorAccessor;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin implements IChunkGeneratorAccessor {

	@Shadow protected abstract void func_242705_a(StructureFeature<?, ?> p_242705_1_, DynamicRegistries p_242705_2_, StructureManager p_242705_3_, IChunk p_242705_4_, TemplateManager p_242705_5_, long p_242705_6_, ChunkPos p_242705_8_, Biome p_242705_9_);

	//todo: this doesn't work for some reason
	@Override
	public void callFunc_242705_a(StructureFeature<?, ?> p_242705_1_, DynamicRegistries p_242705_2_, StructureManager p_242705_3_, IChunk p_242705_4_, TemplateManager p_242705_5_, long p_242705_6_, ChunkPos p_242705_8_, Biome p_242705_9_) {
		this.func_242705_a(p_242705_1_, p_242705_2_, p_242705_3_, p_242705_4_, p_242705_5_, p_242705_6_, p_242705_8_, p_242705_9_);
	}

	//todo: same here
	@Redirect(method = "func_242705_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/settings/DimensionStructuresSettings;func_236197_a_(Lnet/minecraft/world/gen/feature/structure/Structure;)Lnet/minecraft/world/gen/settings/StructureSeparationSettings;"))
	private StructureSeparationSettings func_242705_a(DimensionStructuresSettings old, Structure<?> structure) {
		if(structure == ModStructures.VAULT) {
			return new StructureSeparationSettings(1, 0, -1);
		}

		return old.func_236197_a_(structure);
	}

}
