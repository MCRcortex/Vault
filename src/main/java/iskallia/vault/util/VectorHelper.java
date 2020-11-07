package iskallia.vault.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class VectorHelper {

	public static Vector3d getDirectionNormalized(Vector3d destination, Vector3d origin) {
		return new Vector3d(destination.x - origin.x, destination.y - origin.y, destination.z - origin.z).normalize();
	}

	public static Vector3d getVectorFromPos(BlockPos pos) {
		return new Vector3d(pos.getX(), pos.getY(), pos.getZ());
	}

	public static Vector3d add(Vector3d a, Vector3d b) {
		return new Vector3d(a.x + b.x, a.y + b.y, a.z + b.z);
	}

	public static Vector3d subtract(Vector3d a, Vector3d b) {
		return new Vector3d(a.x - b.x, a.y - b.y, a.z - b.z);
	}

	public static Vector3d multiply(Vector3d velocity, float speed) {

		return new Vector3d(velocity.x * speed, velocity.y * speed, velocity.z * speed);
	}

	public static Vector3d clampMagnitude(Vector3d velocity, float max) {
		double length = (double) MathHelper.sqrt(velocity.x * velocity.x + velocity.y * velocity.y + velocity.z * velocity.z);
		return length < 1.0E-4D ? Vector3d.ZERO : new Vector3d(velocity.x / length * max, velocity.y / length * max, velocity.z / length * max);
	}

	public static Vector3d moveTowards(Vector3d current, Vector3d target, float maxDistanceDelta) {
		float toVector_x = (float) (target.x - current.x);
		float toVector_y = (float) (target.y - current.y);
		float toVector_z = (float) (target.z - current.z);

		float sqdist = toVector_x * toVector_x + toVector_y * toVector_y + toVector_z * toVector_z;

		if (sqdist == 0 || (maxDistanceDelta >= 0 && sqdist <= maxDistanceDelta * maxDistanceDelta))
			return target;
		float dist = (float) MathHelper.sqrt(sqdist);

		return new Vector3d(current.x + toVector_x / dist * maxDistanceDelta, current.y + toVector_y / dist * maxDistanceDelta, current.z + toVector_z / dist * maxDistanceDelta);
	}

}
