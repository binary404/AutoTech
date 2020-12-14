package binary404.autotech.common.core.manager;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.item.ModItems;
import binary404.autotech.common.tags.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;

public class GrinderManager {

    private static ArrayList<GrinderRecipe> recipeMap = new ArrayList<>();

    public static void init() {
        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.copper_ingot), new ItemStack(ModItems.copper_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.tin_ingot), new ItemStack(ModItems.tin_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.lead_ingot), new ItemStack(ModItems.lead_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.silver_ingot), new ItemStack(ModItems.silver_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.uranium_ingot), new ItemStack(ModItems.uranium_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.nickel_ingot), new ItemStack(ModItems.nickel_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.platinum_ingot), new ItemStack(ModItems.platinum_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.titanium_ingot), new ItemStack(ModItems.titanium_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(Items.IRON_INGOT), new ItemStack(ModItems.iron_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(Items.GOLD_INGOT), new ItemStack(ModItems.gold_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(Items.NETHERITE_INGOT), new ItemStack(ModItems.netherite_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);

        addRecipe(Tier.LV, 25000, new ItemStack(Blocks.STONE), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.SAND), 60, 30);
        addRecipe(Tier.LV, 25000, new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.SAND), new ItemStack(Items.FLINT), 50, 50);
        addRecipe(Tier.LV, 25000, new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.SAND), new ItemStack(Items.FLINT), ItemStack.EMPTY, 60, 0);

        addRecipe(Tier.LV, 10000, new ItemStack(Items.WHEAT), new ItemStack(ModItems.flour, 2), new ItemStack(ModItems.flour), new ItemStack(Items.WHEAT_SEEDS), 40, 60);

        addRecipe(Tier.LV, 15000, new ItemStack(Items.CHARCOAL), new ItemStack(ModItems.charcoal_dust), new ItemStack(ModItems.charcoal_dust), ItemStack.EMPTY, 40, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(Items.COAL), new ItemStack(ModItems.coal_dust), new ItemStack(ModItems.coal_dust), ItemStack.EMPTY, 40, 0);
    }

    public static void initTags() {
        addRecipe(Tier.LV, 40000, ModTags.Items.ORES_COPPER, 1, new ItemStack(ModItems.copper_ore_dust, 2), new ItemStack(ModItems.iron_ore_dust), new ItemStack(ModItems.lead_ore_dust), 50, 30);
        addRecipe(Tier.LV, 40000, ModTags.Items.ORES_TIN, 1, new ItemStack(ModItems.tin_ore_dust, 2), new ItemStack(ModItems.iron_ore_dust), new ItemStack(ModItems.copper_ore_dust), 40, 20);
        addRecipe(Tier.LV, 50000, ModTags.Items.ORES_LEAD, 1, new ItemStack(ModItems.lead_ore_dust, 2), new ItemStack(ModItems.silver_ore_dust), ItemStack.EMPTY, 75, 0);
        addRecipe(Tier.LV, 35000, ModTags.Items.ORE_SILVER, 1, new ItemStack(ModItems.silver_ore_dust, 2), new ItemStack(ModItems.lead_ore_dust), new ItemStack(ModItems.gold_ore_dust), 80, 70);
        addRecipe(Tier.EV, 80000, ModTags.Items.ORE_URANIUM, 1, new ItemStack(ModItems.uranium_ore_dust, 2), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 50000, ModTags.Items.ORE_NICKEL, 1, new ItemStack(ModItems.nickel_ore_dust, 2), new ItemStack(ModItems.iron_ore_dust), new ItemStack(ModItems.platinum_ore_dust), 60, 10);
        addRecipe(Tier.HV, 40000, ModTags.Items.ORE_PLATINUM, 1, new ItemStack(ModItems.platinum_ore_dust, 2), new ItemStack(ModItems.nickel_ore_dust), new ItemStack(ModItems.copper_ore_dust), 70, 70);
        addRecipe(Tier.MV, 65000, ModTags.Items.ORE_TITANIUM, 1, new ItemStack(ModItems.titanium_ore_dust, 2), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 40000, Tags.Items.ORES_IRON, 1, new ItemStack(ModItems.iron_ore_dust, 2), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 40000, Tags.Items.ORES_GOLD, 1, new ItemStack(ModItems.gold_ore_dust, 2), new ItemStack(ModItems.silver_ore_dust), ItemStack.EMPTY, 45, 0);
        addRecipe(Tier.LV, 80000, Tags.Items.ORES_NETHERITE_SCRAP, 1, new ItemStack(ModItems.netherite_ore_dust, 2), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);

        addRecipe(Tier.LV, 20000, ModTags.Items.ORE_DUSTS_COPPER, 1, new ItemStack(ModItems.copper_dust), new ItemStack(ModItems.iron_dust), new ItemStack(ModItems.lead_dust), 50, 30);
        addRecipe(Tier.LV, 20000, ModTags.Items.ORE_DUSTS_TIN, 1, new ItemStack(ModItems.tin_dust), new ItemStack(ModItems.iron_dust), new ItemStack(ModItems.copper_dust), 40, 20);
        addRecipe(Tier.LV, 20000, ModTags.Items.ORE_DUSTS_LEAD, 1, new ItemStack(ModItems.lead_dust), new ItemStack(ModItems.silver_dust), ItemStack.EMPTY, 75, 0);
        addRecipe(Tier.LV, 20000, ModTags.Items.ORE_DUSTS_SILVER, 1, new ItemStack(ModItems.silver_dust), new ItemStack(ModItems.lead_dust), new ItemStack(ModItems.gold_dust), 80, 70);
        addRecipe(Tier.LV, 20000, ModTags.Items.ORE_DUSTS_URANIUM, 1, new ItemStack(ModItems.uranium_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
    }

    public static GrinderRecipe getRecipe(ItemStack input) {
        for (GrinderRecipe recipe : recipeMap) {
            if (recipe.recipeMatches(input))
                return recipe;
        }
        return null;
    }

    public static boolean recipeExists(ItemStack input) {
        boolean recipeExists = getRecipe(input) != null;
        System.out.println(input + " " + recipeExists);
        return recipeExists;
    }

    public static void removeRecipe(ItemStack stack) {
        GrinderRecipe recipe = getRecipe(stack);
        if (recipe != null)
            recipeMap.remove(recipe);
    }

    public static GrinderRecipe[] getRecipeList() {
        return recipeMap.toArray(new GrinderRecipe[0]);
    }

    public static GrinderRecipe addRecipe(Tier minTier, int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondOutput, ItemStack thirdOutput, int secondChance, int thirdChance) {
        if (input.isEmpty() || primaryOutput.isEmpty() || energy <= 0 || recipeExists(input)) {
            return null;
        }

        GrinderRecipe recipe = new GrinderRecipe(input, primaryOutput, secondOutput, thirdOutput, secondOutput.isEmpty() ? 0 : secondChance, thirdOutput.isEmpty() ? 0 : thirdChance, energy, minTier);
        recipeMap.add(recipe);
        return recipe;
    }

    public static GrinderRecipe addRecipe(Tier minTier, int energy, ITag.INamedTag<Item> inputTag, int tagInput, ItemStack primaryOutput, ItemStack secondOutput, ItemStack thirdOutput, int secondChance, int thirdChance) {
        if (inputTag == null)
            return null;

        if (inputTag.getAllElements().isEmpty() || primaryOutput.isEmpty() || energy <= 0) {
            return null;
        }

        GrinderRecipe recipe = new GrinderRecipe(inputTag, tagInput, primaryOutput, secondOutput, thirdOutput, secondOutput.isEmpty() ? 0 : secondChance, thirdOutput.isEmpty() ? 0 : thirdChance, energy, minTier);
        recipeMap.add(recipe);
        return recipe;
    }

    public static class GrinderRecipe implements IMachineRecipe {
        ItemStack input;
        ItemStack primaryOutput;
        ItemStack secondaryOutput;
        ItemStack thirdOutput;
        int secondaryChance;
        int thirdChance;
        int energy;
        Tier minTier;
        ITag.INamedTag<Item> inputTag;
        int inputCount;

        GrinderRecipe(ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, ItemStack thirdOutput, int secondaryChance, int thirdChance, int energy, Tier minTier) {
            this.input = input;
            this.inputCount = input.getCount();
            this.primaryOutput = primaryOutput;
            this.secondaryOutput = secondaryOutput;
            this.thirdOutput = thirdOutput;
            this.secondaryChance = secondaryChance;
            this.thirdChance = thirdChance;
            this.energy = energy;
            this.minTier = minTier;
        }

        GrinderRecipe(ITag.INamedTag<Item> inputTag, int tagInput, ItemStack output, ItemStack output2, ItemStack output3, int secondChance, int thirdChance, int energy, Tier minTier) {
            this.inputTag = inputTag;
            this.inputCount = tagInput;
            this.input = new ItemStack(inputTag.getAllElements().get(0), tagInput);
            this.primaryOutput = output;
            this.secondaryOutput = output2;
            this.thirdOutput = output3;
            this.secondaryChance = secondChance;
            this.thirdChance = thirdChance;
            this.energy = energy;
            this.minTier = minTier;
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

        public ItemStack getThirdOutput() {
            return thirdOutput;
        }

        public int getSecondaryChance() {
            return secondaryChance;
        }

        public int getThirdChance() {
            return thirdChance;
        }

        public int getEnergy() {
            return energy;
        }

        public Tier getMinTier() {
            return minTier;
        }

        public ITag.INamedTag<Item> getInputTag() {
            return inputTag;
        }

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
