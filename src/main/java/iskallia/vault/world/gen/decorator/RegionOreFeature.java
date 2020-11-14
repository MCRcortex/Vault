package iskallia.vault.world.gen.decorator;

import com.mojang.serialization.Codec;
import iskallia.vault.Vault;
import iskallia.vault.config.VaultOreConfig;
import iskallia.vault.init.ModConfigs;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.event.RegistryEvent;

import java.util.Random;

public class RegionOreFeature extends OreFeature {

	public static Feature<OreFeatureConfig> INSTANCE;

	public RegionOreFeature(Codec<OreFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean func_241855_a(ISeedReader view, ChunkGenerator gen, Random random, BlockPos pos, OreFeatureConfig config) {
		VaultOreConfig.Pool pool = ModConfigs.VAULT_ORES.getPool(view.getSeed(), pos.getX() >> 4, pos.getZ() >> 4, new SharedSeedRandom());
		boolean result = false;

		for(int i = 0; i < pool.getTries(); i++) {
			int x = random.nextInt(16);
			int y = random.nextInt(256);
			int z = random.nextInt(16);
			result |= super.func_241855_a(view, gen, random, pos.add(x, y, z), pool.getRandom(random));
		}

		return result;
	}

	public static void register(RegistryEvent.Register<Feature<?>> event) {
		INSTANCE = new RegionOreFeature(OreFeatureConfig.field_236566_a_);
		INSTANCE.setRegistryName(Vault.id("vault_ore"));
		 event.getRegistry().register(INSTANCE);
	}

}
