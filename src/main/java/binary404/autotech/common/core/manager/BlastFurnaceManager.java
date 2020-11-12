package binary404.autotech.common.core.manager;

import binary404.autotech.common.core.util.ComparableItemStack;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class BlastFurnaceManager {

    private static Map<ComparableItemStack, BlastFurnaceRecipe> recipeMap = new Object2ObjectOpenHashMap<>();

    public static void init() {

    }

    public static BlastFurnaceRecipe getRecipe(ItemStack input) {
        if (input.isEmpty()) {
            return null;
        }
        BlastFurnaceRecipe recipe = recipeMap.get(ComparableItemStack.convert(input));
        return recipe;
    }

    public static boolean recipeExists(ItemStack input) {
        boolean recipeExists = getRecipe(input) != null;
        return recipeExists;
    }

    public static BlastFurnaceRecipe[] getRecipeList() {
        return recipeMap.values().toArray(new BlastFurnaceRecipe[0]);
    }

    public static BlastFurnaceRecipe addRecipe(int energy, ItemStack input, ItemStack output) {
        if (input.isEmpty() || output.isEmpty() || energy <= 0 || recipeExists(input)) {
            return null;
        }

        BlastFurnaceRecipe recipe = new BlastFurnaceRecipe(input, output, energy);
        recipeMap.put(ComparableItemStack.convert(input), recipe);
        return recipe;
    }

    public static class BlastFurnaceRecipe {
        ItemStack input;
        ItemStack output;
        int energy;

        BlastFurnaceRecipe(ItemStack input, ItemStack output, int energy) {
            this.input = input;
            this.output = output;
            this.energy = energy;
        }

        public ItemStack getInput() {
            return input;
        }

        public ItemStack getOutput() {
            return output;
        }

        public int getEnergy() {
            return energy;
        }
    }

}
