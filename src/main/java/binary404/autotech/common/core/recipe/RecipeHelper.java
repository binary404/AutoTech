package binary404.autotech.common.core.recipe;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.recipe.core.CountableIngredient;
import binary404.autotech.common.core.recipe.core.FuelRecipe;
import binary404.autotech.common.core.recipe.machine.builder.SimpleRecipeBuilder;
import binary404.autotech.common.core.recipe.map.FuelRecipeMap;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import com.google.common.base.Preconditions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraftforge.fluids.FluidStack;

public class RecipeHelper {

    public static void addMixerRecipe(ItemStack output1, ItemStack output2, CountableIngredient... inputs) {
        Preconditions.checkArgument(inputs.length <= 4, "Inputs must be less than or equal to 4");
        SimpleRecipeBuilder builder = RecipeMaps.MIXER_RECIPES.recipeBuilder();
        builder.inputs(inputs);
        builder.outputs(output1, output2);
        builder.buildAndRegister();
    }

    public static void addCompactorRecipe(ItemStack input1, ItemStack output1) {
        RecipeMaps.COMPACTOR_RECIPES.recipeBuilder()
                .inputs(input1)
                .outputs(output1)
                .buildAndRegister();
    }

    public static void addCompactorRecipe(ITag.INamedTag<Item> input1, int count, ItemStack output1) {
        RecipeMaps.COMPACTOR_RECIPES.recipeBuilder()
                .input(input1, count)
                .outputs(output1)
                .buildAndRegister();
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
        SimpleRecipeBuilder builder = RecipeMaps.GRINDER_RECIPES.recipeBuilder()
                .input(input, 1)
                .output(output1, 2)
                .chancedOutput(new ItemStack(output2), chance, tierBoost);
        if (chance2 > 0) {
            builder.chancedOutput(new ItemStack(output3), chance2, tierBoost2);
        }
        builder.duration(400).energyPerTick(12).buildAndRegister();
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
