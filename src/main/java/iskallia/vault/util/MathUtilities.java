package iskallia.vault.util;

import java.util.Random;

public class MathUtilities {

    public static float randomFloat(float min, float max) {
        return new Random().nextFloat() * (max - min) + min;
    }

}
