package binary404.autotech.common.core.manager;

import binary404.autotech.common.core.util.ComparableItemStack;
import binary404.autotech.common.item.ModItems;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Map;

public class ArcFurnaceManager {

    private static Map<ComparableItemStack, ArcFurnaceRecipe> recipeMap = new Object2ObjectOpenHashMap<>();

    public static void init() {
        addRecipe(60000, new ItemStack(Items.IRON_INGOT), new ItemStack(ModItems.steel_ingot));
    }

    public static ArcFurnaceRecipe getRecipe(ItemStack input) {
        if (input.isEmpty()) {
            return null;
        }
        ArcFurnaceRecipe recipe = recipeMap.get(ComparableItemStack.convert(input));
        return recipe;
    }

    public static boolean recipeExists(ItemStack input) {
        boolean recipeExists = getRecipe(input) != null;
        return recipeExists;
    }

    public static ArcFurnaceRecipe removeRecipe(ItemStack input) {
        return recipeMap.remove(ComparableItemStack.convert(input));
    }

    public static ArcFurnaceRecipe[] getRecipeList() {
        return recipeMap.values().toArray(new ArcFurnaceRecipe[0]);
    }

    public static ArcFurnaceRecipe addRecipe(int energy, ItemStack input, ItemStack output) {
        if (input.isEmpty() || output.isEmpty() || energy <= 0 || recipeExists(input)) {
            return null;
        }

        ArcFurnaceRecipe recipe = new ArcFurnaceRecipe(input, output, energy);
        recipeMap.put(ComparableItemStack.convert(input), recipe);
        return recipe;
    }

    public static class ArcFurnaceRecipe {
        ItemStack input;
        ItemStack output;
        int energy;

        ArcFurnaceRecipe(ItemStack input, ItemStack output, int energy) {
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
