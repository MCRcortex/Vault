package iskallia.vault.world.structure;

import com.google.gson.annotations.Expose;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MutableBoundingBox;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class VaultPiece {

	private static final Map<String, VaultPiece> REGISTRY = new HashMap<>();

	@Expose private String id;
	@Expose private List<String> openings;
	@Expose private Map<String, List<Info>> pools;

	public VaultPiece() {

	}

	public ResourceLocation getId() {
		return new ResourceLocation(this.id);
	}

	public List<MutableBoundingBox> getOpenings() {
		return this.openings.stream().map(s -> {
			String[] data = s.split(Pattern.quote(","));
			return new MutableBoundingBox(Arrays.stream(data).mapToInt(Integer::parseInt).toArray());
		}).collect(Collectors.toList());
	}

	public VaultPiece getNextPiece(Random random, Direction direction) {
		List<Info> pool = this.pools.get(direction.toString());
		if(pool == null || pool.isEmpty())return null;
		int i = random.nextInt(this.getTotalWeight());

		for(Info info : pool) {
			if(i < info.weight) {
				return REGISTRY.get(info.id);
			}

			i -= info.weight;
		}

		return null;
	}

	private int getTotalWeight() {
		return this.pools.values().stream().flatMap(Collection::stream).mapToInt(info -> info.weight).sum();
	}

	public static class Info {
		@Expose private String id;
		@Expose private int weight;
	}

}
