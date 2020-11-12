package iskallia.vault.skill.ability.type;

import com.google.gson.annotations.Expose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public class MegaJumpAbility extends PlayerAbility {

    @Expose private final int extraHeight;

    public MegaJumpAbility(int cost, int extraHeight) {
        super(cost, Behavior.RELEASE_TO_PERFORM);
        this.extraHeight = extraHeight;
    }

    public int getExtraHeight() {
        return extraHeight;
    }

    @Override
    public void onAction(PlayerEntity player, boolean active) {
        double magnitude = (10 + extraHeight) * 0.15;

        Vector3d jumpVector = new Vector3d(0, 1, 0);

        jumpVector = jumpVector.scale(magnitude);

        player.addVelocity(
                jumpVector.getX(),
                jumpVector.getY(),
                jumpVector.getZ()
        );

        player.startFallFlying();

        player.velocityChanged = true;

        player.playSound(SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.MASTER, 1f, 1f);
        ((ServerWorld) player.world).spawnParticle(ParticleTypes.POOF,
                player.getPosX(), player.getPosY(), player.getPosZ(),
                50, 1D, 0.5D, 1D, 0.0D);
    }

}
