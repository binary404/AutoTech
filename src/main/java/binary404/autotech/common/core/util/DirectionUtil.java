package binary404.autotech.common.core.util;

import net.minecraft.util.Direction;

public class DirectionUtil {
    public static Direction safeGet(byte d) {
        Direction[] v = Direction.values();

        if (d < 0 || d >= v.length) {
            return Direction.NORTH;
        }

        return v[d];
    }
}
