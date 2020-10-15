package iskallia.vault.world.gen.ruletest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;

import java.util.Random;

public class VaultRuleTest extends BlockMatchRuleTest {

	public static final VaultRuleTest INSTANCE = new VaultRuleTest(Blocks.BEDROCK);

	public VaultRuleTest(Block block) {
		super(block);
	}

	@Override
	public boolean test(BlockState state, Random random) {
		return state.getMaterial() == Material.ROCK && state.isSolid();
	}

}
