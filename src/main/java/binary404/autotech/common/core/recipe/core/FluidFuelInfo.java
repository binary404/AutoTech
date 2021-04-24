package binary404.autotech.common.core.recipe.core;

import binary404.autotech.common.core.recipe.core.AbstractFuelInfo;
import net.minecraftforge.fluids.FluidStack;

public class FluidFuelInfo extends AbstractFuelInfo {

    private final FluidStack fluidStack;

    public FluidFuelInfo(FluidStack fluidStack, int fuelRemaining, int fuelCapacity, int fuelMinConsumed, int fuelBurnTime) {
        super(fuelRemaining, fuelCapacity, fuelMinConsumed, fuelBurnTime);
        this.fluidStack = fluidStack;
    }

    public String getFuelName() {
        return fluidStack.getTranslationKey();
    }

}
