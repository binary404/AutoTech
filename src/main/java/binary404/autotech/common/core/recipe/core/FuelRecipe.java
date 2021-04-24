package binary404.autotech.common.core.recipe.core;

import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FuelRecipe {

    private final FluidStack recipeFluid;
    private final int duration;
    private final long minVoltage;

    public FuelRecipe(FluidStack recipeFluid, int duration, long minVoltage) {
        this.recipeFluid = recipeFluid.copy();
        this.duration = duration;
        this.minVoltage = minVoltage;
    }

    public FluidStack getRecipeFluid() {
        return recipeFluid.copy();
    }

    public int getDuration() {
        return duration;
    }

    public long getMinVoltage() {
        return minVoltage;
    }

    public boolean matches(long maxVoltage, FluidStack inputFluid) {
        return maxVoltage > -getMinVoltage() && getRecipeFluid().isFluidEqual(inputFluid);
    }
}
