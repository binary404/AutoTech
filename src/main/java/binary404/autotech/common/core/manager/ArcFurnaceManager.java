package binary404.autotech.common.core.manager;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.item.ModItems;
import binary404.autotech.common.tags.ModTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;

public class ArcFurnaceManager {

    private static ArrayList<ArcFurnaceRecipe> recipeMap = new ArrayList<>();

    public static void initTags() {
        addRecipe(Tier.LV, 40000, Tags.Items.INGOTS_IRON, 1, null, 0, new ItemStack(ModItems.steel_ingot));
        addRecipe(Tier.LV, 20000, ModTags.Items.DUSTS_RED_ALLOY, 1, null, 0, new ItemStack(ModItems.red_alloy_ingot));
        addRecipe(Tier.LV, 35000, ModTags.Items.INGOTS_COPPER, 1, Tags.Items.DUSTS_REDSTONE, 2, new ItemStack(ModItems.red_alloy_ingot, 2));
    }

    public static ArcFurnaceRecipe getRecipe(ItemStack input, ItemStack input2) {
        for (ArcFurnaceRecipe recipe : recipeMap) {
            if (recipe.recipeMatches(input, input2))
                return recipe;
        }
        return null;
    }

    public static boolean recipeExists(ItemStack input, ItemStack input2) {
        boolean recipeExists = getRecipe(input, input2) != null;
        return recipeExists;
    }

    public static void removeRecipe(ItemStack input, ItemStack input2) {
        ArcFurnaceRecipe recipe = getRecipe(input, input2);
        if (recipe != null)
            recipeMap.remove(recipe);
    }

    public static ArcFurnaceRecipe[] getRecipeList() {
        return recipeMap.toArray(new ArcFurnaceRecipe[0]);
    }

    public static ArcFurnaceRecipe addRecipe(Tier minTier, int energy, ItemStack input, ItemStack input2, ItemStack output) {
        if (input.isEmpty() || output.isEmpty() || energy <= 0 || recipeExists(input, input2)) {
            return null;
        }

        ArcFurnaceRecipe recipe = new ArcFurnaceRecipe(input, input2, output, energy, minTier);
        recipeMap.add(recipe);
        return recipe;
    }

    public static ArcFurnaceRecipe addRecipe(Tier minTier, int energy, ITag.INamedTag<Item> inputTag, int tagInput, ITag.INamedTag<Item> inputTag2, int tagInput2, ItemStack output) {
        if (inputTag == null || energy <= 0) {
            return null;
        }
        if (inputTag.getAllElements().isEmpty() || output.isEmpty())
            return null;
        ArcFurnaceRecipe recipe = new ArcFurnaceRecipe(inputTag, tagInput, inputTag2, tagInput2, output, energy, minTier);
        recipeMap.add(recipe);
        return recipe;
    }

    public static class ArcFurnaceRecipe implements IMachineRecipe {
        ItemStack input1;
        ItemStack input2;
        ItemStack output;
        ITag.INamedTag<Item> inputTag;
        ITag.INamedTag<Item> inputTag2;
        Tier minTier;
        int inputCount;
        int inputCount2;
        int energy;

        ArcFurnaceRecipe(ItemStack input1, ItemStack input2, ItemStack output, int energy, Tier minTier) {
            this.input1 = input1;
            this.input2 = input2;
            this.inputCount = this.input1.getCount();
            this.inputCount2 = this.input2.getCount();
            this.output = output;
            this.energy = energy;
            this.minTier = minTier;
        }

        ArcFurnaceRecipe(ITag.INamedTag<Item> inputTag, int tagInput, ITag.INamedTag<Item> inputTag2, int tagInput2, ItemStack output, int energy, Tier minTier) {
            this.inputTag = inputTag;
            this.inputCount = tagInput;
            this.input1 = new ItemStack(inputTag.getAllElements().get(0), inputCount);
            this.inputTag2 = inputTag2;
            this.inputCount2 = tagInput2;
            if (inputTag2 != null) {
                this.input2 = new ItemStack(inputTag2.getAllElements().get(0), inputCount2);
            }
            this.output = output;
            this.energy = energy;
            this.minTier = minTier;
        }

        public ItemStack getInput() {
            return input1;
        }

        public ItemStack getInput2() {
            return input2;
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

        public ITag.INamedTag<Item> getInputTag2() {
            return inputTag2;
        }

        @Override
        public int getInputCount() {
            return inputCount;
        }

        public int getInputCount2() {
            return inputCount2;
        }

        public boolean recipeMatches(ItemStack input, ItemStack input2) {
            if (inputTag != null) {
                if (inputTag2 == null)
                    return inputTag.contains(input.getItem());
                else
                    return inputTag.contains(input.getItem()) && inputTag2.contains(input2.getItem());
            }
            if (getInput().getItem().equals(input.getItem()) && getInput2().getItem().equals(input2.getItem())) {
                return true;
            }
            return input.getItem().delegate.get().equals(getInput().getItem().delegate.get()) && input2.getItem().delegate.get().equals(getInput2().getItem().delegate.get());
        }
    }

}
