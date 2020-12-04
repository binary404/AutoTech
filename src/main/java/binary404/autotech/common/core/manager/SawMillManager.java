package binary404.autotech.common.core.manager;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;

import java.util.ArrayList;
import java.util.List;

public class SawMillManager {

    private static List<SawMillRecipe> recipeMap = new ArrayList<>();

    public static void init() {
    }

    public static void initTags() {
        for (Block log : BlockTags.LOGS.getAllElements()) {
            for (Block plank : BlockTags.PLANKS.getAllElements()) {
                String logName = log.getRegistryName().getPath().replace("_log", "");
                String plankName = plank.getRegistryName().getPath().replace("_planks", "");
                if (logName.equals(plankName)) {
                    addRecipe(30000, new ItemStack(log), new ItemStack(plank, 6), new ItemStack(ModItems.saw_dust), 55);
                }
                addRecipe(30000, new ItemStack(plank), new ItemStack(Items.STICK, 6), new ItemStack(ModItems.saw_dust), 68);
            }
        }
        addRecipe(30000, new ItemStack(Items.STICK), new ItemStack(ModItems.saw_dust, 2), new ItemStack(ModItems.saw_dust), 90);
    }

    public static SawMillRecipe getRecipe(ItemStack input) {
        for (SawMillRecipe recipe : recipeMap)
            if (recipe.recipeMatches(input))
                return recipe;
        return null;
    }

    public static boolean recipeExists(ItemStack input) {
        boolean recipeExists = getRecipe(input) != null;
        return recipeExists;
    }

    public static void removeRecipe(ItemStack input) {
        SawMillRecipe recipe = getRecipe(input);
        if (recipe != null)
            recipeMap.remove(recipe);
    }

    public static SawMillRecipe[] getRecipeList() {
        return recipeMap.toArray(new SawMillRecipe[0]);
    }

    public static SawMillRecipe addRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondOutput, int secondChance) {
        if (input.isEmpty() || primaryOutput.isEmpty() || energy <= 0 || recipeExists(input)) {
            return null;
        }

        SawMillRecipe recipe = new SawMillRecipe(input, primaryOutput, secondOutput, secondOutput.isEmpty() ? 0 : secondChance, energy);
        recipeMap.add(recipe);
        return recipe;
    }

    public static class SawMillRecipe implements IMachineRecipe {

        ItemStack input;
        ItemStack primaryOutput;
        ItemStack secondaryOutput;
        int secondaryChance;
        int energy;
        ITag.INamedTag<Item> inputTag;
        int inputCount;

        SawMillRecipe(ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int secondaryChance, int energy) {
            this.input = input;
            this.inputCount = input.getCount();
            this.primaryOutput = primaryOutput;
            this.secondaryOutput = secondaryOutput;
            this.secondaryChance = secondaryChance;
            this.energy = energy;
        }

        SawMillRecipe(ITag.INamedTag<Item> inputTag, int inputCount, ItemStack output, ItemStack secondaryOutput, int secondaryChance, int energy) {
            this.inputTag = inputTag;
            this.inputCount = inputCount;
            this.primaryOutput = output;
            this.secondaryOutput = secondaryOutput;
            this.secondaryChance = secondaryChance;
            this.energy = energy;
        }

        public ItemStack getInput() {

            return input;
        }

        public ItemStack getOutput() {

            return primaryOutput;
        }

        public ItemStack getSecondaryOutput() {

            return secondaryOutput;
        }

        public int getSecondaryOutputChance() {

            return secondaryChance;
        }

        public int getEnergy() {

            return energy;
        }

        @Override
        public Tier getMinTier() {
            return Tier.LV;
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
            if (inputTag != null)
                return inputTag.contains(input.getItem());
            if (getInput().getItem().equals(input.getItem()))
                return true;
            return getInput().getItem().delegate.get().equals(input.getItem().delegate.get());
        }
    }
}
