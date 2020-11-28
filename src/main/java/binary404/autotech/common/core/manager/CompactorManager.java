package binary404.autotech.common.core.manager;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.util.ComparableItemStack;
import binary404.autotech.common.item.ModItems;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;

import java.util.ArrayList;
import java.util.Map;

public class CompactorManager {

    private static ArrayList<CompactorRecipe> recipeMap = new ArrayList<>();

    public static void init() {
        addRecipe(Tier.MV, 40000, new ItemStack(ModItems.copper_ingot, 3), new ItemStack(ModItems.copper_plate, 2));
        addRecipe(Tier.MV, 40000, new ItemStack(ModItems.tin_ingot, 3), new ItemStack(ModItems.tin_plate, 2));
        addRecipe(Tier.MV, 40000, new ItemStack(ModItems.lead_ingot, 3), new ItemStack(ModItems.lead_plate, 2));
        addRecipe(Tier.MV, 40000, new ItemStack(ModItems.silver_ingot, 3), new ItemStack(ModItems.silver_plate, 2));
        addRecipe(Tier.MV, 40000, new ItemStack(ModItems.uranium_ingot, 3), new ItemStack(ModItems.uranium_plate, 2));
        addRecipe(Tier.MV, 40000, new ItemStack(ModItems.nickel_ingot, 3), new ItemStack(ModItems.nickel_plate, 2));
        addRecipe(Tier.MV, 40000, new ItemStack(ModItems.platinum_ingot, 3), new ItemStack(ModItems.platinum_plate, 2));
        addRecipe(Tier.MV, 40000, new ItemStack(ModItems.titanium_ingot, 3), new ItemStack(ModItems.titanium_plate, 2));
        addRecipe(Tier.MV, 40000, new ItemStack(ModItems.bronze_ingot, 3), new ItemStack(ModItems.bronze_plate, 2));
        addRecipe(Tier.MV, 40000, new ItemStack(ModItems.steel_ingot, 3), new ItemStack(ModItems.steel_plate, 2));
        addRecipe(Tier.MV, 40000, new ItemStack(Items.IRON_INGOT, 3), new ItemStack(ModItems.iron_plate, 2));
        addRecipe(Tier.MV, 40000, new ItemStack(Items.GOLD_INGOT, 3), new ItemStack(ModItems.gold_plate, 2));

        addRecipe(Tier.LV, 37000, new ItemStack(ModItems.saw_dust, 8), new ItemStack(ModItems.plywood));

        addRecipe(Tier.LV, 40000, new ItemStack(ModItems.charcoal_dust, 4), new ItemStack(ModItems.carbon_mesh));
        addRecipe(Tier.LV, 40000, new ItemStack(ModItems.coal_dust, 4), new ItemStack(ModItems.carbon_mesh));
    }

    public static CompactorRecipe getRecipe(ItemStack input) {
        for (CompactorRecipe recipe : recipeMap)
            if (recipe.recipeMatches(input))
                return recipe;
        return null;
    }

    public static boolean recipeExists(ItemStack input) {
        boolean recipeExists = getRecipe(input) != null;
        return recipeExists;
    }

    public static void removeRecipe(ItemStack input) {
        CompactorRecipe recipe = getRecipe(input);
        if (recipe != null)
            recipeMap.remove(recipe);
    }

    public static CompactorRecipe[] getRecipeList() {
        return recipeMap.toArray(new CompactorRecipe[0]);
    }

    public static CompactorRecipe addRecipe(Tier minTier, int energy, ItemStack input, ItemStack output) {
        if (input.isEmpty() || output.isEmpty() || energy <= 0 || recipeExists(input))
            return null;

        CompactorRecipe recipe = new CompactorRecipe(input, output, energy, minTier);
        recipeMap.add(recipe);
        return recipe;
    }

    public static class CompactorRecipe implements IMachineRecipe {
        ItemStack input;
        ItemStack output;
        ITag.INamedTag<Item> inputTag;
        int inputCount;
        int energy;
        Tier minTier;

        CompactorRecipe(ItemStack input, ItemStack output, int energy, Tier minTier) {
            this.input = input;
            this.inputCount = input.getCount();
            this.output = output;
            this.energy = energy;
            this.minTier = minTier;
        }

        CompactorRecipe(ITag.INamedTag<Item> inputTag, int tagCount, ItemStack output, int energy, Tier minTier) {
            this.inputTag = inputTag;
            this.inputCount = tagCount;
            this.output = output;
            this.energy = energy;
            this.minTier = minTier;
        }

        public ItemStack getInput() {
            return input;
        }

        @Override
        public int getInputCount() {
            return inputCount;
        }

        @Override
        public ITag.INamedTag<Item> getInputTag() {
            return inputTag;
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

        @Override
        public boolean recipeMatches(ItemStack input) {
            if (inputTag != null)
                return inputTag.contains(input.getItem());
            if (getInput().getItem().equals(input.getItem()))
                return true;
            return input.getItem().delegate.equals(getInput().getItem().delegate.get());
        }
    }

}
