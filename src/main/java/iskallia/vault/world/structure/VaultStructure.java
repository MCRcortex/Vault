package iskallia.vault.world.structure;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class VaultStructure extends Structure<VaultConfig> {

    private final int startY = 128;
    private final boolean idk = false;
    private final boolean surface = false;

    public VaultStructure(Codec<VaultConfig> config) {
        super(config);
    }

    public GenerationStage.Decoration func_236396_f_() {
        return GenerationStage.Decoration.UNDERGROUND_STRUCTURES;
    }

    public Structure.IStartFactory<VaultConfig> getStartFactory() {
        return (p_242778_1_, p_242778_2_, p_242778_3_, p_242778_4_, p_242778_5_, p_242778_6_) -> new Start(this, p_242778_2_, p_242778_3_, p_242778_4_, p_242778_5_, p_242778_6_);
    }

    public static class Start extends MarginedStructureStart<VaultConfig> {
        private final VaultStructure structure;

        public Start(VaultStructure structure, int chunkX, int chunkZ, MutableBoundingBox box, int references, long worldSeed) {
            super(structure, chunkX, chunkZ, box, references, worldSeed);
            this.structure = structure;
        }

        public void func_230364_a_(DynamicRegistries registry, ChunkGenerator gen, TemplateManager manager, int chunkX, int chunkZ, Biome biome, VaultConfig config) {
            BlockPos blockpos = new BlockPos(chunkX * 16, this.structure.startY, chunkZ * 16);
            VaultPools.init();
            JigsawManager.func_242837_a(registry, config.toVillageConfig(), AbstractVillagePiece::new, gen, manager, blockpos, this.components, this.rand, this.structure.idk, this.structure.surface);
            this.recalculateStructureSize();
        }
    }

}
