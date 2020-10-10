package iskallia.vault.mixin;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(JigsawManager.class)
public class JigsawManagerMixin {

	/*
	private static boolean VAULT_GEN = false;

	//Forgive me for I have sinned.
	@Inject(method = "func_242837_a", at = @At("HEAD"), remap = false)
	private static void generateStart(DynamicRegistries p_242837_0_, VillageConfig p_242837_1_,
	                                  JigsawManager.IPieceFactory p_242837_2_, ChunkGenerator p_242837_3_,
	                                  TemplateManager p_242837_4_, BlockPos p_242837_5_,
	                                  List<? super AbstractVillagePiece> p_242837_6_, Random p_242837_7_,
	                                  boolean p_242837_8_, boolean p_242837_9_, CallbackInfo ci) {
		VAULT_GEN = true;
	}

	@Inject(method = "func_242837_a", at = @At("RETURN"), remap = false)
	private static void generateEnd(DynamicRegistries p_242837_0_, VillageConfig p_242837_1_,
	                                  JigsawManager.IPieceFactory p_242837_2_, ChunkGenerator p_242837_3_,
	                                  TemplateManager p_242837_4_, BlockPos p_242837_5_,
	                                  List<? super AbstractVillagePiece> p_242837_6_, Random p_242837_7_,
	                                  boolean p_242837_8_, boolean p_242837_9_, CallbackInfo ci) {
		VAULT_GEN = false;
	}

	@Redirect(method = "func_242837_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/shapes/VoxelShapes;create(Lnet/minecraft/util/math/AxisAlignedBB;)Lnet/minecraft/util/math/shapes/VoxelShape;"), remap = false)
	private static VoxelShape extendShape(AxisAlignedBB bb) {
		return VAULT_GEN ? VoxelShapes.create(bb.grow(1000)) : VoxelShapes.create(bb);
	}*/

}
