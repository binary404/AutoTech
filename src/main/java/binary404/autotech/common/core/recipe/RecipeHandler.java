package binary404.autotech.common.core.recipe;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.recipe.core.FuelRecipe;
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

import static binary404.autotech.common.core.recipe.RecipeMaps.BREWING_RECIPES;
import static binary404.autotech.common.core.recipe.RecipeMaps.DISTILLATION_RECIPES;

public class RecipeHandler {

    public static void init() {
        addGrinderRecipes();

        addBrewingRecipes();

        addDistillationRecipes();

        addFuelRecipes();
    }

    public static void addGrinderRecipes() {
        addOreGrinderRecipe(ModTags.Items.RAW_COPPER, ModItems.copper_ore_dust, ModItems.iron_ore_dust, 8500, 750, ModItems.tin_ore_dust, 6000, 500);

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
                .buildAndRegister();
    }

    public static void addOreGrinderRecipe(ITag.INamedTag<Item> input, Item output1, Item output2, int chance, int tierBoost, Item output3, int chance2, int tierBoost2) {
        RecipeMaps.GRINDER_RECIPES.recipeBuilder()
                .input(input, 1)
                .output(output1, 2)
                .chancedOutput(new ItemStack(output2), chance, tierBoost)
                .chancedOutput(new ItemStack(output3), chance2, tierBoost2)
                .buildAndRegister();
    }

}
