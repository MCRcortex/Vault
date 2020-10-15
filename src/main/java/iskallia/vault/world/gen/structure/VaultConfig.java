package iskallia.vault.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.VillageConfig;

import java.util.function.Supplier;

public class VaultConfig implements IFeatureConfig {

	public static final Codec<VaultConfig> CODEC = RecordCodecBuilder.create(builder -> {
		return builder.group(JigsawPattern.field_244392_b_.fieldOf("start_pool").forGetter(VaultConfig::getStartPool),
				Codec.intRange(0, Integer.MAX_VALUE).fieldOf("size").forGetter(VaultConfig::getSize))
				.apply(builder, VaultConfig::new);
	});

	private final Supplier<JigsawPattern> startPool;
	private final int size;

	public VaultConfig(Supplier<JigsawPattern> startPool, int size) {
		this.startPool = startPool;
		this.size = size;
	}

	public int getSize() {
		return this.size;
	}

	public Supplier<JigsawPattern> getStartPool() {
		return this.startPool;
	}

	public VillageConfig toVillageConfig() {
		return new VillageConfig(this.getStartPool(), this.getSize());
	}

}
