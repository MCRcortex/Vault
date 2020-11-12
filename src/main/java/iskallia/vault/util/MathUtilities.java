package iskallia.vault.util;

import net.minecraft.util.math.vector.Vector3d;

import java.util.Random;

public class MathUtilities {

    public static float randomFloat(float min, float max) {
        return new Random().nextFloat() * (max - min) + min;
    }

    public static double map(double value, double x0, double y0, double x1, double y1) {
        return x1 + (y1 - x1) * ((value - x0) / (y0 - x0));
    }

    public static double extractYaw(Vector3d vec) {
        return Math.atan2(vec.getZ(), vec.getX());
    }

    public static double extractPitch(Vector3d vec) {
        return Math.asin(vec.getY() / vec.length());
    }

}
