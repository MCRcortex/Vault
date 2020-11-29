package iskallia.vault.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;
import java.util.Random;

public class VaultOreBlock extends OreBlock {

    public VaultOreBlock() {
        super(Properties.create(Material.ROCK, MaterialColor.DIAMOND)
                .setRequiresTool()
                .setLightLevel(state -> 9)
                .hardnessAndResistance(3f, 3f)
        );
    }

    @Override
    protected int getExperience(Random rand) {
        return MathHelper.nextInt(rand, 3, 7);
    }

}
