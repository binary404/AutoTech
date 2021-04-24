package binary404.autotech.common.fluid;

import net.minecraftforge.fluids.FluidAttributes;

public class FluidAttributeHolder {

    public static FluidAttributes.Builder addAttributes(FluidAttributes.Builder builder) {
        return builder;
    }

    public static FluidAttributes.Builder distilledWater(FluidAttributes.Builder builder) {
        return builder.viscosity(1000).density(1000);
    }

    public static FluidAttributes.Builder crudeOil(FluidAttributes.Builder builder) {
        return builder.viscosity(3000).density(1500);
    }

    public static FluidAttributes.Builder biomass(FluidAttributes.Builder builder) {
        return builder.viscosity(1500).density(1500);
    }

}
