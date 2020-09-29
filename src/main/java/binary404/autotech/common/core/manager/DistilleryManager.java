package binary404.autotech.common.core.manager;

import binary404.autotech.common.core.util.Util;
import binary404.autotech.common.fluid.ModFluids;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;

public class DistilleryManager {

    private static Int2ObjectOpenHashMap<DistilleryRecipe> recipeMap = new Int2ObjectOpenHashMap<>();

    public static DistilleryRecipe getRecipe(FluidStack input) {
        return input == null ? null : recipeMap.get(Util.fluidHashCode(input));
    }

    public static boolean recipeExists(FluidStack input) {
        return getRecipe(input) != null;
    }

    public static void init() {
        addRecipe(30000, new FluidStack(Fluids.WATER, 2000), new FluidStack(ModFluids.distilled_water_source, 1000));

        System.out.println(getRecipe(new FluidStack(Fluids.WATER, 1000)));
    }

    public static DistilleryRecipe addRecipe(int energy, FluidStack input, FluidStack output) {
        if (input == null || output == null || energy <= 0 || recipeExists(input)) {
            return null;
        }

        DistilleryRecipe recipe = new DistilleryRecipe(input, output, energy);
        recipeMap.put(Util.fluidHashCode(input), recipe);
        return recipe;
    }

    public static class DistilleryRecipe {
        FluidStack input;
        FluidStack output;
        int energy;

        DistilleryRecipe(FluidStack input, FluidStack output, int energy) {
            this.input = input;
            this.output = output;
            this.energy = energy;
        }

        public FluidStack getInput() {
            return input;
        }

        public FluidStack getOutput() {
            return output;
        }

        public int getEnergy() {
            return energy;
        }
    }

}
