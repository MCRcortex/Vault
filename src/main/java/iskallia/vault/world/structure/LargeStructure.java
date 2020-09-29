package iskallia.vault.world.structure;

import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class LargeStructure {

	public LargeStructure(ResourceLocation id, ServerWorld world) {
		//StructureHelper.doSomethingHereIdkForgotWhat
	}

	public void generate(VaultPiece start, Random random, BlockPos pos, int depth) {
		if(depth == 5)return;

		VaultPiece next = start.getNextPiece(random, Direction.byHorizontalIndex(random.nextInt(4)));
		this.generate(next, random, pos, depth + 1);
	}

}
