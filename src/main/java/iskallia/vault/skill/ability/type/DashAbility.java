package iskallia.vault.skill.ability.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.MathUtilities;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.EnumSet;

public class DashAbility extends PlayerAbility {

    @Expose private final int extraRadius;

    public DashAbility(int cost, int extraRadius) {
        super(cost, Behavior.RELEASE_TO_PERFORM);
        this.extraRadius = extraRadius;
    }

    public int getExtraRadius() {
        return extraRadius;
    }

    @Override
    public void onAction(PlayerEntity player, boolean active) {
        Vector3d lookVector = player.getLookVec();

        double magnitude = (10 + extraRadius) * 0.15;

        Vector3d dashVector = new Vector3d(
                lookVector.getX(),
                lookVector.getY(),
                lookVector.getZ()
        );

        float initialYaw = (float) MathUtilities.extractYaw(dashVector);
        
        dashVector = dashVector.rotateYaw(initialYaw);

        double dashPitch = Math.toDegrees(MathUtilities.extractPitch(dashVector));

        if (dashPitch + 20 > 90) {
            dashVector = new Vector3d(0, 1, 0);
            dashPitch = 90;
        } else {
            dashVector = dashVector.rotateRoll((float) Math.toRadians(-20));
            dashVector = dashVector.rotateYaw(-initialYaw);
            dashVector = dashVector.normalize();
        }

        double coef = 1.5 - MathUtilities.map(Math.abs(dashPitch),
                0.0d, 90.0d,
                0.5, 1.0d);

        dashVector = dashVector.mul(
                magnitude,
                magnitude * coef,
                magnitude
        );

        player.addVelocity(
                dashVector.getX(),
                dashVector.getY(),
                dashVector.getZ()
        );

        player.velocityChanged = true;

        player.playSound(SoundEvents.ITEM_TRIDENT_RIPTIDE_3, SoundCategory.MASTER, 1f, 1f);
    }

}
