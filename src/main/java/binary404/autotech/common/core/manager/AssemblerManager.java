package binary404.autotech.common.core.manager;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.util.ComparableItemStack;
import binary404.autotech.common.fluid.ModFluids;
import binary404.autotech.common.item.ModItems;
import com.google.gson.internal.$Gson$Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssemblerManager {

    private static List<AssemblerRecipe> recipeMap = new ArrayList<>();

    public static void init() {

    }

    public static AssemblerRecipe getRecipe(List<ItemStack> inputs, FluidStack input) {
        for (AssemblerRecipe recipe : recipeMap) {
            if (recipe.matches(inputs, input)) {
                return recipe;
            }
        }
        return null;
    }

    public static boolean recipeExists(List<ItemStack> inputs, FluidStack input) {
        boolean recipeExists = getRecipe(inputs, input) != null;

        return recipeExists;
    }

    public static AssemblerRecipe[] getRecipeList() {
        return recipeMap.toArray(new AssemblerRecipe[0]);
    }

    public static AssemblerRecipe addRecipe(Tier minTier, int energy, NonNullList<ItemStack> input, FluidStack fluid_input, ItemStack output) {
        if (input.isEmpty() || output.isEmpty() || energy <= 0 || recipeExists(input, fluid_input))
            return null;

        AssemblerRecipe recipe = new AssemblerRecipe(input, fluid_input, output, energy, minTier);
        recipeMap.add(recipe);
        return recipe;
    }

    public static void removeRecipe(NonNullList<ItemStack> input, FluidStack fluid_input) {
        AssemblerRecipe recipe = getRecipe(input, fluid_input);
        if (recipe != null)
            recipeMap.remove(recipe);
    }


    public static class AssemblerRecipe {

        NonNullList<ItemStack> input;
        FluidStack fluid_input;
        ItemStack output;
        int energy;
        Tier minTier;

        AssemblerRecipe(NonNullList<ItemStack> input, FluidStack fluid_input, ItemStack output, int energy, Tier minTier) {
            this.input = input;
            this.fluid_input = fluid_input;
            this.output = output;
            this.energy = energy;
            this.minTier = minTier;
        }

        public boolean matches(List<ItemStack> input, FluidStack fluid_input) {
            int count = 0;
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    ItemStack stack1 = input.get(i + j);
                    ItemStack stack2 = this.input.get(i + j);
                    if (ItemStack.areItemStacksEqual(stack1, stack2)) {
                        count++;
                    }
                }
            }
            if (count == 9 && fluid_input.getFluid() == this.fluid_input.getFluid())
                return true;
            return false;
        }

        public FluidStack getFluid_input() {
            return fluid_input;
        }

        public NonNullList<ItemStack> getInput() {
            return input;
        }

        public ItemStack getOutput() {
            return output;
        }

        public int getEnergy() {
            return energy;
        }

        public Tier getMinTier() {
            return minTier;
        }
    }

}
