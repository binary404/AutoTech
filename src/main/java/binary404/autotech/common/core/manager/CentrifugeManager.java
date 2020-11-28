package binary404.autotech.common.core.manager;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.util.ComparableItemStack;
import binary404.autotech.common.item.ModItems;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CentrifugeManager {

    private static List<CentrifugeRecipe> recipeMap = new ArrayList<>();

    public static void init() {
        addRecipe(Tier.MV, 100000, new ItemStack(ModItems.copper_ore_dust, 2), new ItemStack(ModItems.copper_dust, 3), new ItemStack(ModItems.iron_dust, 2), 45);
        addRecipe(Tier.MV, 100000, new ItemStack(ModItems.tin_ore_dust, 2), new ItemStack(ModItems.tin_dust, 3), new ItemStack(ModItems.copper_dust, 2), 40);
        addRecipe(Tier.MV, 100000, new ItemStack(ModItems.lead_ore_dust, 2), new ItemStack(ModItems.lead_dust, 3), new ItemStack(ModItems.silver_dust, 2), 30);
        addRecipe(Tier.MV, 100000, new ItemStack(ModItems.silver_ore_dust, 2), new ItemStack(ModItems.silver_dust, 3), new ItemStack(ModItems.lead_dust, 2), 35);
        addRecipe(Tier.MV, 100000, new ItemStack(ModItems.uranium_ore_dust, 2), new ItemStack(ModItems.uranium_dust, 3), ItemStack.EMPTY, 0);
        addRecipe(Tier.MV, 100000, new ItemStack(ModItems.nickel_ore_dust, 2), new ItemStack(ModItems.nickel_dust, 3), new ItemStack(ModItems.iron_dust, 2), 40);
        addRecipe(Tier.MV, 100000, new ItemStack(ModItems.platinum_ore_dust, 2), new ItemStack(ModItems.platinum_dust, 3), new ItemStack(ModItems.nickel_dust, 2), 40);
        addRecipe(Tier.MV, 100000, new ItemStack(ModItems.titanium_ore_dust, 2), new ItemStack(ModItems.titanium_dust, 3), ItemStack.EMPTY, 0);
        addRecipe(Tier.MV, 100000, new ItemStack(ModItems.iron_ore_dust, 2), new ItemStack(ModItems.iron_dust, 3), new ItemStack(ModItems.lead_dust, 2), 70);
        addRecipe(Tier.MV, 100000, new ItemStack(ModItems.gold_ore_dust, 2), new ItemStack(ModItems.gold_dust, 3), new ItemStack(ModItems.silver_dust, 2), 60);

        addRecipe(Tier.MV, 150000, new ItemStack(ModItems.netherite_ore_dust, 2), new ItemStack(ModItems.netherite_dust, 3), ItemStack.EMPTY, 0);
    }

    public static CentrifugeRecipe getRecipe(ItemStack input) {
        for (CentrifugeRecipe recipe : recipeMap)
            if (recipe.recipeMatches(input))
                return recipe;
        return null;
    }

    public static boolean recipeExists(ItemStack input) {
        boolean recipeExists = getRecipe(input) != null;

        return recipeExists;
    }

    public static void removeRecipe(ItemStack input) {
        CentrifugeRecipe recipe = getRecipe(input);
        if (recipe != null)
            recipeMap.remove(recipe);
    }

    public static CentrifugeRecipe[] getRecipeList() {
        return recipeMap.toArray(new CentrifugeRecipe[0]);
    }

    public static CentrifugeRecipe addRecipe(Tier minTier, int energy, ItemStack input, ItemStack output1, ItemStack output2, int secondChance) {
        if (input.isEmpty() || output1.isEmpty() || energy <= 0 || recipeExists(input))
            return null;

        CentrifugeRecipe recipe = new CentrifugeRecipe(input, output1, output2, secondChance, energy, minTier);
        recipeMap.add(recipe);
        return recipe;
    }

    public static class CentrifugeRecipe implements IMachineRecipe {
        ItemStack input;
        ItemStack output1;
        ItemStack output2;
        int secondChance;
        int energy;
        Tier minTier;
        ITag.INamedTag<Item> inputTag;
        int inputCount;

        CentrifugeRecipe(ItemStack input, ItemStack output1, ItemStack output2, int secondChance, int energy, Tier minTier) {
            this.input = input;
            this.inputCount = input.getCount();
            this.output1 = output1;
            this.output2 = output2;
            this.energy = energy;
            this.minTier = minTier;
            this.secondChance = secondChance;
        }

        CentrifugeRecipe(ITag.INamedTag<Item> inputTag, int tagInput, ItemStack output, ItemStack output2, int secondChance, int energy, Tier minTier) {
            this.inputTag = inputTag;
            this.inputCount = tagInput;
            this.input = new ItemStack(inputTag.getAllElements().get(0), tagInput);
            this.output1 = output;
            this.output2 = output2;
            this.secondChance = secondChance;
            this.energy = energy;
            this.minTier = minTier;
        }

        public ItemStack getInput() {
            return input;
        }

        public ItemStack getOutput() {
            return output1;
        }

        public ItemStack getOutput2() {
            return output2;
        }

        public int getEnergy() {
            return energy;
        }

        public Tier getMinTier() {
            return minTier;
        }

        public int getSecondChance() {
            return secondChance;
        }

        @Override
        public ITag.INamedTag<Item> getInputTag() {
            return inputTag;
        }

        @Override
        public int getInputCount() {
            return inputCount;
        }

        @Override
        public boolean recipeMatches(ItemStack input) {
            if (inputTag != null)
                return inputTag.contains(input.getItem());
            if (getInput().getItem().equals(input.getItem()))
                return true;
            return input.getItem().delegate.get().equals(getInput().getItem().delegate.get());
        }
    }

}
