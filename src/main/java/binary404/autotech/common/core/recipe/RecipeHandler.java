package binary404.autotech.common.core.recipe;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.recipe.core.CountableIngredient;
import binary404.autotech.common.core.recipe.core.FuelRecipe;
import binary404.autotech.common.core.recipe.machine.builder.SimpleRecipeBuilder;
import binary404.autotech.common.core.recipe.map.FuelRecipeMap;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import binary404.autotech.common.fluid.ModFluids;
import binary404.autotech.common.item.ModItems;
import binary404.autotech.common.tags.ModTags;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

import static binary404.autotech.common.core.recipe.RecipeHelper.*;
import static binary404.autotech.common.core.recipe.RecipeMaps.*;

public class RecipeHandler {

    public static void init() {
        for(RecipeMap<?> recipeMap : RecipeMap.getRecipeMaps()) {
            recipeMap.clearRecipes();
        }
        addOreRecipes();

        addBrewingRecipes();

        addDistillationRecipes();
        addBlastFurnaceRecipes();

        addFuelRecipes();

        addMixerRecipes();

        addCompactorRecipes();
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
        addOreDustGrinderRecipe(ModTags.Items.ORE_DUSTS_NICKEL, ModItems.nickel_dust, ModItems.iron_dust, ModItems.platinum_dust);
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

    public static void addBlastFurnaceRecipes() {
        BLAST_RECIPES.recipeBuilder().duration(400).energyPerTick(100).input(ModTags.Items.DUSTS_TITANIUM, 1).output(ModItems.titanium_ingot).blastFurnaceTemp(1200).buildAndRegister();
    }

    public static void addFuelRecipes() {
        addFuelRecipe(RecipeMaps.STEAM_TURBINE_FUELS, new FluidStack(ModFluids.steam, 100), 1, Tier.LV);
    }

    public static void addMixerRecipes() {
        addMixerRecipe(new ItemStack(ModItems.bronze_dust, 4), ItemStack.EMPTY, CountableIngredient.from(ModTags.Items.DUSTS_COPPER, 3), CountableIngredient.from(ModTags.Items.DUSTS_TIN, 1));
    }

    public static void addCompactorRecipes() {
        addCompactorRecipe(ModTags.Items.INGOTS_BRONZE, 3, new ItemStack(ModItems.bronze_plate, 2));
    }

}
