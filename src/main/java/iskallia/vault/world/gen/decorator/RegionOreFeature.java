package iskallia.vault.world.gen.decorator;

import com.mojang.serialization.Codec;
import iskallia.vault.config.VaultOreConfig;
import iskallia.vault.init.ModConfigs;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.Random;

public class RegionOreFeature extends OreFeature {

	public static final Feature<OreFeatureConfig> INSTANCE = Registry.register(Registry.FEATURE, "vault_ore",
			new RegionOreFeature(OreFeatureConfig.field_236566_a_));

	public RegionOreFeature(Codec<OreFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean func_241855_a(ISeedReader view, ChunkGenerator gen, Random random, BlockPos pos, OreFeatureConfig config) {
		VaultOreConfig.Pool pool = ModConfigs.VAULT_ORES.getPool(view.getSeed(), pos.getX() >> 4, pos.getZ() >> 4, new SharedSeedRandom());
		return super.func_241855_a(view, gen, random, pos, pool.getRandom(random));
	}

}
