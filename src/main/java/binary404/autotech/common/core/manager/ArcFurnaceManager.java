package binary404.autotech.common.core.manager;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.util.ComparableItemStack;
import binary404.autotech.common.item.ModItems;
import binary404.autotech.common.tags.ModTags;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.Map;

public class ArcFurnaceManager {

    private static ArrayList<ArcFurnaceRecipe> recipeMap = new ArrayList<>();

    public static void initTags() {
        addRecipe(Tier.LV, 30000, Tags.Items.INGOTS_IRON, 1, new ItemStack(ModItems.steel_ingot));
        addRecipe(Tier.LV, 20000, ModTags.Items.DUSTS_RED_ALLOY, 1, new ItemStack(ModItems.red_alloy_ingot));
    }

    public static ArcFurnaceRecipe getRecipe(ItemStack input) {
        for (ArcFurnaceRecipe recipe : recipeMap) {
            if (recipe.recipeMatches(input))
                return recipe;
        }
        return null;
    }

    public static boolean recipeExists(ItemStack input) {
        boolean recipeExists = getRecipe(input) != null;
        return recipeExists;
    }

    public static void removeRecipe(ItemStack input) {
        ArcFurnaceRecipe recipe = getRecipe(input);
        if (recipe != null)
            recipeMap.remove(recipe);
    }

    public static ArcFurnaceRecipe[] getRecipeList() {
        return recipeMap.toArray(new ArcFurnaceRecipe[0]);
    }

    public static ArcFurnaceRecipe addRecipe(Tier minTier, int energy, ItemStack input, ItemStack output) {
        if (input.isEmpty() || output.isEmpty() || energy <= 0 || recipeExists(input)) {
            return null;
        }

        ArcFurnaceRecipe recipe = new ArcFurnaceRecipe(input, output, energy, minTier);
        recipeMap.add(recipe);
        return recipe;
    }

    public static ArcFurnaceRecipe addRecipe(Tier minTier, int energy, ITag.INamedTag<Item> inputTag, int tagInput, ItemStack output) {
        if (inputTag == null || energy <= 0) {
            return null;
        }
        if (inputTag.getAllElements().isEmpty() || output.isEmpty())
            return null;
        ArcFurnaceRecipe recipe = new ArcFurnaceRecipe(inputTag, tagInput, output, energy, minTier);
        recipeMap.add(recipe);
        return recipe;
    }

    public static class ArcFurnaceRecipe implements IMachineRecipe {
        ItemStack input;
        ItemStack output;
        ITag.INamedTag<Item> inputTag;
        Tier minTier;
        int inputCount;
        int energy;

        ArcFurnaceRecipe(ItemStack input, ItemStack output, int energy, Tier minTier) {
            this.input = input;
            this.inputCount = input.getCount();
            this.output = output;
            this.energy = energy;
            this.minTier = minTier;
        }

        ArcFurnaceRecipe(ITag.INamedTag<Item> inputTag, int tagInput, ItemStack output, int energy, Tier minTier) {
            this.inputTag = inputTag;
            this.inputCount = tagInput;
            this.input = new ItemStack(inputTag.getAllElements().get(0), tagInput);
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

        @Override
        public Tier getMinTier() {
            return minTier;
        }

        @Override
        public ITag.INamedTag<Item> getInputTag() {
            return inputTag;
        }

        @Override
        public int getInputCount() {
            return inputCount;
        }

        public boolean recipeMatches(ItemStack input) {
            if (inputTag != null) {
                return inputTag.contains(input.getItem());
            }
            if (getInput().getItem().equals(input.getItem())) {
                return true;
            }
            return input.getItem().delegate.get().equals(getInput().getItem().delegate.get());
        }
    }

}
