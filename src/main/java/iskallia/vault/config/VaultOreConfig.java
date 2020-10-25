package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.data.VaultRaid;
import iskallia.vault.world.gen.ruletest.VaultRuleTest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VaultOreConfig extends Config {

	@Expose private List<Pool> POOLS = new ArrayList<>();
	private int totalWeight;

	@Override
	public String getName() {
		return "vault_ore";
	}

	public Pool getPool(long worldSeed, int chunkX, int chunkZ, SharedSeedRandom rand) {
		chunkX <<= 4; chunkZ <<= 4;
		int regionX = chunkX < 0 ? chunkX / VaultRaid.REGION_SIZE - 1 : chunkX / VaultRaid.REGION_SIZE;
		int regionZ = chunkZ < 0 ? chunkZ / VaultRaid.REGION_SIZE - 1 : chunkZ / VaultRaid.REGION_SIZE;
		rand.setLargeFeatureSeed(worldSeed, regionX, regionZ); //Lifty lifty :P
		int i = rand.nextInt(this.getTotalWeight());
		return this.getWeightedPoolAt(i);
	}

	public Pool getWeightedPoolAt(int index) {
		Pool current = null;

		for(Pool pool: this.POOLS) {
			current = pool;
			index -= pool.WEIGHT;
			if(index < 0)break;
		}

		return current;
	}

	public int getTotalWeight() {
		if(this.totalWeight == 0) {
			for(Pool pool: this.POOLS) {
				this.totalWeight += pool.WEIGHT;
			}
		}

		return this.totalWeight;
	}

	@Override
	protected void reset() {
		POOLS.add(new Pool(1).add(Blocks.DIAMOND_ORE, 8).add(Blocks.LAPIS_ORE, 10));
		POOLS.add(new Pool(1).add(Blocks.GOLD_ORE, 12, 2).add(Blocks.GOLD_BLOCK, 3));
		POOLS.add(new Pool(1).add(Blocks.IRON_ORE, 15).add(Blocks.COAL_ORE, 20));
		POOLS.add(new Pool(1).add(Blocks.REDSTONE_ORE, 10).add(Blocks.EMERALD_ORE, 4));
	}

	public static class Pool {
		@Expose private List<Ore> ORES;
		@Expose private int WEIGHT;
		private int totalWeight;

		public Pool(int weight) {
			this.WEIGHT = weight;
			this.ORES = new ArrayList<>();
		}

		public Pool add(Block block, int size, int weight) {
			this.ORES.add(new Ore(block.getRegistryName().toString(), size, weight));
			return this;
		}

		public Pool add(Block block, int size) {
			return this.add(block, size, 1);
		}

		public OreFeatureConfig getRandom(Random random) {
			return this.getWeightedOreAt(random.nextInt(this.getTotalWeight())).toConfig();
		}

		public Ore getWeightedOreAt(int index) {
			Ore current = null;

			for(Ore ore: this.ORES) {
				current = ore;
				index -= ore.WEIGHT;
				if(index < 0)break;
			}

			return current;
		}

		public int getTotalWeight() {
			if(this.totalWeight == 0) {
				for(Ore ore: this.ORES) {
					this.totalWeight += ore.WEIGHT;
				}
			}

			return this.totalWeight;
		}
	}

	public static class Ore {
		@Expose private String NAME;
		@Expose private int SIZE;
		@Expose private int WEIGHT;

		public Ore(String name, int size, int weight) {
			this.NAME = name;
			this.SIZE = size;
			this.WEIGHT = weight;
		}

		public OreFeatureConfig toConfig() {
			BlockState state = Registry.BLOCK.getOptional(new ResourceLocation(this.NAME)).orElse(Blocks.DIORITE).getDefaultState();
			return new OreFeatureConfig(VaultRuleTest.INSTANCE, state, this.SIZE);
		}
	}

}
