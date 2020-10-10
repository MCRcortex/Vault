package iskallia.vault.util;

import net.minecraft.util.math.vector.Vector2f;

// The moment you know you missed Typescript :c
public class Rectangle {

    public int x0, y0;
    public int x1, y1;

    public int getWidth() {
        return x1 - x0;
    }

    public int getHeight() {
        return y1 - y0;
    }

    public boolean contains(int x, int y) {
        return x0 <= x && x <= x1
                && y0 <= y && y <= y1;
    }

    public Vector2f midpoint() {
        return new Vector2f(
                (x1 - x0) / 2f,
                (y1 - y0) / 2f
        );
    }

}
