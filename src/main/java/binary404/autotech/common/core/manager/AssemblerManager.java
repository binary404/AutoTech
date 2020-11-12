package binary404.autotech.common.core.manager;

import binary404.autotech.common.container.machine.AssemblerContainer;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.util.ComparableItemStack;
import binary404.autotech.common.item.ModItems;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssemblerManager {

    private static List<AssemblerRecipe> recipeMap = new ArrayList<>();

    public static void init() {

    }

    public static AssemblerRecipe getRecipe(List<ItemStack> inputs) {
        for (AssemblerRecipe recipe : recipeMap) {
            if (recipe.matches(inputs)) {
                return recipe;
            }
        }
        return null;
    }

    public static boolean recipeExists(List<ItemStack> inputs) {
        boolean recipeExists = getRecipe(inputs) != null;

        return recipeExists;
    }

    public static AssemblerRecipe[] getRecipeList() {
        return recipeMap.toArray(new AssemblerRecipe[0]);
    }

    public static AssemblerRecipe addRecipe(Tier minTier, int energy, int minTime, NonNullList<ItemStack> input, ItemStack output) {
        if (input.isEmpty() || output.isEmpty() || energy <= 0 || recipeExists(input))
            return null;

        AssemblerRecipe recipe = new AssemblerRecipe(input, output, energy, minTier, minTime);
        recipeMap.add(recipe);
        return recipe;
    }


    public static class AssemblerRecipe {

        NonNullList<ItemStack> input;
        ItemStack output;
        int energy;
        Tier minTier;
        int minTime;

        AssemblerRecipe(NonNullList<ItemStack> input, ItemStack output, int energy, Tier minTier, int minTime) {
            this.input = input;
            this.output = output;
            this.energy = energy;
            this.minTier = minTier;
            this.minTime = minTime;
        }

        public boolean matches(List<ItemStack> input) {
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
            if (count == 9)
                return true;
            return false;
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

        public int getMinTime() {
            return minTime;
        }
    }

}
