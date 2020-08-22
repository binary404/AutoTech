package binary404.autotech.data;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.item.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
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

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.bronze_plate)
                .addIngredient(ModItems.bronze_plate, 4)
                .addCriterion("has_item", hasItem(ModItems.bronze_plate))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.lv_machine_hull)
                .key('P', ModItems.copper_plate)
                .key('B', ModItems.bronze_plate)
                .patternLine("BBB")
                .patternLine("BPB")
                .patternLine("BBB")
                .addCriterion("has_item", hasItem(ModItems.bronze_plate))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.copper_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModBlocks.copper_ore)
                .addCriterion("has_item", hasItem(ModBlocks.copper_ore))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.tin_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModBlocks.tin_ore)
                .addCriterion("has_item", hasItem(ModBlocks.tin_ore))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.lead_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModBlocks.lead_ore)
                .addCriterion("has_item", hasItem(ModBlocks.lead_ore))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.silver_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModBlocks.silver_ore)
                .addCriterion("has_item", hasItem(ModBlocks.silver_ore))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.nickel_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModBlocks.nickel_ore)
                .addCriterion("has_item", hasItem(ModBlocks.nickel_ore))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.iron_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(Blocks.IRON_ORE)
                .addCriterion("has_item", hasItem(Blocks.IRON_ORE))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.gold_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(Blocks.GOLD_ORE)
                .addCriterion("has_item", hasItem(Blocks.GOLD_ORE))
                .build(consumer);
    }
}
