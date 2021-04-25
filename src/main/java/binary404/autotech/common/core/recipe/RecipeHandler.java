package binary404.autotech.common.core.recipe;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.recipe.core.FuelRecipe;
import binary404.autotech.common.core.recipe.map.FuelRecipeMap;
import binary404.autotech.common.fluid.ModFluids;
import binary404.autotech.common.item.ModItems;
import binary404.autotech.common.tags.ModTags;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

import static binary404.autotech.common.core.recipe.RecipeMaps.BREWING_RECIPES;
import static binary404.autotech.common.core.recipe.RecipeMaps.DISTILLATION_RECIPES;

public class RecipeHandler {

    public static void init() {
        addOreRecipes();

        addBrewingRecipes();

        addDistillationRecipes();

        addFuelRecipes();
    }

    public static void addOreRecipes() {
        addOreGrinderRecipe(ModTags.Items.RAW_COPPER, ModItems.copper_ore_dust, ModItems.iron_ore_dust, 5000, 500, ModItems.lead_ore_dust, 3500, 250);
        addOreGrinderRecipe(ModTags.Items.RAW_TIN, ModItems.tin_ore_dust, ModItems.iron_ore_dust, 5000, 500, ModItems.copper_ore_dust, 3500, 250);
        addOreGrinderRecipe(ModTags.Items.RAW_LEAD, ModItems.lead_ore_dust, ModItems.silver_ore_dust, 4000, 500);
        addOreGrinderRecipe(ModTags.Items.RAW_SILVER, ModItems.silver_ore_dust, ModItems.lead_ore_dust, 4000, 500, ModItems.gold_ore_dust, 2000, 250);
        addOreGrinderRecipe(ModTags.Items.RAW_URANIUM, ModItems.uranium_ore_dust, null, 0, 0);
        addOreGrinderRecipe(ModTags.Items.RAW_NICKEL, ModItems.nickel_ore_dust, ModItems.iron_ore_dust, 5000, 500, ModItems.platinum_ore_dust, 0, 10);
        addOreGrinderRecipe(ModTags.Items.RAW_IRON, ModItems.iron_ore_dust, null, 0, 0);
        addOreGrinderRecipe(ModTags.Items.RAW_GOLD, ModItems.gold_ore_dust, ModItems.silver_ore_dust, 2500, 250);

        addOreGrinderRecipe(ModTags.Items.ORES_COPPER, ModItems.copper_ore_dust, ModItems.iron_ore_dust, 5000, 500, ModItems.lead_ore_dust, 3500, 250);
        addOreGrinderRecipe(ModTags.Items.ORES_TIN, ModItems.tin_ore_dust, ModItems.iron_ore_dust, 5000, 500, ModItems.copper_ore_dust, 3500, 250);
        addOreGrinderRecipe(ModTags.Items.ORES_LEAD, ModItems.lead_ore_dust, ModItems.silver_ore_dust, 4000, 500);
        addOreGrinderRecipe(ModTags.Items.ORES_SILVER, ModItems.silver_ore_dust, ModItems.lead_ore_dust, 4000, 500, ModItems.gold_ore_dust, 2000, 250);
        addOreGrinderRecipe(ModTags.Items.ORES_URANIUM, ModItems.uranium_ore_dust, null, 0, 0);
        addOreGrinderRecipe(ModTags.Items.ORES_NICKEL, ModItems.nickel_ore_dust, ModItems.iron_ore_dust, 5000, 500, ModItems.platinum_ore_dust, 0, 10);
        addOreGrinderRecipe(Tags.Items.ORES_IRON, ModItems.iron_ore_dust, null, 0, 0);
        addOreGrinderRecipe(Tags.Items.ORES_GOLD, ModItems.gold_ore_dust, ModItems.silver_ore_dust, 2500, 250);

        addOreDustGrinderRecipe(ModTags.Items.ORE_DUSTS_COPPER, ModItems.copper_dust, ModItems.iron_dust, ModItems.lead_dust);
        addOreDustGrinderRecipe(ModTags.Items.ORE_DUSTS_TIN, ModItems.tin_dust, ModItems.iron_dust, ModItems.copper_dust);
        addOreDustGrinderRecipe(ModTags.Items.ORE_DUSTS_LEAD, ModItems.lead_dust, ModItems.silver_dust);
        addOreDustGrinderRecipe(ModTags.Items.ORE_DUSTS_SILVER, ModItems.silver_dust, ModItems.lead_dust, ModItems.gold_dust);
        addOreDustGrinderRecipe(ModTags.Items.ORE_DUSTS_URANIUM, ModItems.uranium_dust, null);
        addOreDustGrinderRecipe(ModTags.Items.ORE_DUSTS_NICKEL, ModItems.nickel_dust, ModItems.iron_dust,ModItems.platinum_dust);
        addOreDustGrinderRecipe(ModTags.Items.ORE_DUSTS_IRON, ModItems.iron_dust, null);
        addOreDustGrinderRecipe(ModTags.Items.ORE_DUSTS_GOLD, ModItems.gold_dust, ModItems.silver_dust);
    }

    public static void addBrewingRecipes() {
        BREWING_RECIPES.recipeBuilder()
                .input(Tags.Items.CROPS, 1)
                .fluidInputs(new FluidStack(Fluids.WATER, 100))
                .fluidOutputs(new FluidStack(ModFluids.biomass, 100))
                .duration(600).energyPerTick(3).buildAndRegister();
    }

    public static void addDistillationRecipes() {
        DISTILLATION_RECIPES.recipeBuilder()
                .fluidInputs(new FluidStack(Fluids.WATER, 100))
                .fluidOutputs(new FluidStack(ModFluids.biomass, 100))
                .fluidOutputs(new FluidStack(ModFluids.distilled_water, 100))
                .duration(120).energyPerTick(4).buildAndRegister();
    }

    public static void addFuelRecipes() {
        addFuelRecipe(RecipeMaps.STEAM_TURBINE_FUELS, new FluidStack(Fluids.WATER, 100), 10, Tier.LV);
    }

    public static void addFuelRecipe(FuelRecipeMap map, FluidStack fuelStack, int duration, Tier tier) {
        map.addRecipe(new FuelRecipe(fuelStack, duration, tier.use));
    }

    public static void addOreGrinderRecipe(ITag.INamedTag<Item> input, Item output1, Item output2, int chance, int tierBoost) {
        RecipeMaps.GRINDER_RECIPES.recipeBuilder()
                .input(input, 1)
                .output(output1, 2)
                .chancedOutput(new ItemStack(output2), chance, tierBoost)
                .duration(400).energyPerTick(12)
                .buildAndRegister();
    }

    public static void addOreGrinderRecipe(ITag.INamedTag<Item> input, Item output1, Item output2, int chance, int tierBoost, Item output3, int chance2, int tierBoost2) {
        RecipeMaps.GRINDER_RECIPES.recipeBuilder()
                .input(input, 1)
                .output(output1, 2)
                .chancedOutput(new ItemStack(output2), chance, tierBoost)
                .chancedOutput(new ItemStack(output3), chance2, tierBoost2)
                .duration(400).energyPerTick(12)
                .buildAndRegister();
    }

    public static void addOreDustGrinderRecipe(ITag.INamedTag<Item> input, Item output1, Item output2) {
        RecipeMaps.GRINDER_RECIPES.recipeBuilder()
                .input(input, 1)
                .output(output1, 1)
                .chancedOutput(new ItemStack(output2), 1400, 850)
                .duration(200).energyPerTick(12)
                .buildAndRegister();
    }

    public static void addOreDustGrinderRecipe(ITag.INamedTag<Item> input, Item output1, Item output2, Item output3) {
        RecipeMaps.GRINDER_RECIPES.recipeBuilder()
                .input(input, 1)
                .output(output1, 1)
                .chancedOutput(new ItemStack(output2), 1400, 850)
                .chancedOutput(new ItemStack(output3), 1000, 850)
                .duration(200).energyPerTick(12)
                .buildAndRegister();
    }

}
