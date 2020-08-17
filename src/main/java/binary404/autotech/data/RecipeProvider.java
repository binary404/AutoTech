package binary404.autotech.data;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.item.ModItems;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.crafting.Ingredient;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.RecipeProvider {

    public RecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        registerMain(consumer);
    }

    private void registerMain(Consumer<IFinishedRecipe> consumer) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModBlocks.copper_ore), ModItems.copper_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.copper_ore))
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModBlocks.tin_ore), ModItems.tin_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.tin_ore))
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModBlocks.lead_ore), ModItems.lead_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.lead_ore))
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModBlocks.silver_ore), ModItems.silver_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.silver_ore))
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModBlocks.uranium_ore), ModItems.uranium_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.uranium_ore))
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModBlocks.nickel_ore), ModItems.nickel_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.nickel_ore))
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModBlocks.platinum_ore), ModItems.platinum_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.platinum_ore))
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModBlocks.titanium_ore), ModItems.titanium_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.titanium_ore))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.copper_plate)
                .addIngredient(ModItems.copper_ingot, 4)
                .addCriterion("has_item", hasItem(ModItems.copper_ingot))
                .build(consumer);
    }
}
