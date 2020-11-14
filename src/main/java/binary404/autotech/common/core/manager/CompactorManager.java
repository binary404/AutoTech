package binary404.autotech.common.core.manager;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.util.ComparableItemStack;
import binary404.autotech.common.item.ModItems;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class CompactorManager {

    private static Map<ComparableItemStack, CompactorRecipe> recipeMap = new Object2ObjectOpenHashMap<>();

    public static void init() {
        addRecipe(Tier.MV, 40000, new ItemStack(ModItems.copper_ingot, 3), new ItemStack(ModItems.copper_plate, 2));
        addRecipe(Tier.MV, 40000, new ItemStack(ModItems.tin_ingot, 3), new ItemStack(ModItems.tin_plate, 2));

        addRecipe(Tier.LV, 37000, new ItemStack(ModItems.saw_dust, 8), new ItemStack(ModItems.plywood));

        addRecipe(Tier.LV, 40000, new ItemStack(ModItems.charcoal_dust, 4), new ItemStack(ModItems.carbon_mesh));
        addRecipe(Tier.LV, 40000, new ItemStack(ModItems.coal_dust, 4), new ItemStack(ModItems.carbon_mesh));

        refresh();
    }

    public static CompactorRecipe getRecipe(ItemStack input) {
        if (input.isEmpty())
            return null;

        CompactorRecipe recipe = recipeMap.get(new ComparableItemStack(input));
        return recipe;
    }

    public static boolean recipeExists(ItemStack input) {
        boolean recipeExists = getRecipe(input) != null;
        return recipeExists;
    }

    public static CompactorRecipe[] getRecipeList() {
        return recipeMap.values().toArray(new CompactorRecipe[0]);
    }

    public static CompactorRecipe addRecipe(Tier minTier, int energy, ItemStack input, ItemStack output) {
        if (input.isEmpty() || output.isEmpty() || energy <= 0 || recipeExists(input))
            return null;

        CompactorRecipe recipe = new CompactorRecipe(input, output, energy, minTier);
        recipeMap.put(new ComparableItemStack(input), recipe);
        return recipe;
    }

    public static void refresh() {
        Map<ComparableItemStack, CompactorRecipe> tempMap = new Object2ObjectOpenHashMap<>(recipeMap.size());
        CompactorRecipe tempRecipe;

        for (Map.Entry<ComparableItemStack, CompactorRecipe> entry : recipeMap.entrySet()) {
            tempRecipe = entry.getValue();
            tempMap.put(new ComparableItemStack(tempRecipe.input), tempRecipe);
        }
        recipeMap.clear();
        recipeMap = tempMap;
    }

    public static class CompactorRecipe {
        ItemStack input;
        ItemStack output;
        int energy;
        Tier minTier;

        CompactorRecipe(ItemStack input, ItemStack output, int energy, Tier minTier) {
            this.input = input;
            this.output = output;
            this.energy = energy;
            this.minTier = minTier;
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

        public Tier getMinTier() {
            return minTier;
        }
    }

}
