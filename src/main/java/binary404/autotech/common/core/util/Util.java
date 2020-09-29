package binary404.autotech.common.core.util;

import net.minecraftforge.fluids.FluidStack;

public class Util {

    public static int safeInt(long value) {
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }

    public static int safeInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int fluidHashCode(FluidStack stack) {
        return stack.getFluid().getRegistryName().toString().hashCode();
    }

}
