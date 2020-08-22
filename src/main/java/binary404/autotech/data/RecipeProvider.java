package binary404.autotech.data;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.item.ModItems;
import binary404.autotech.common.tags.ModTags;
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
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.ORE_COPPER), ModItems.copper_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.copper_ore))
                .build(consumer);
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.ORE_TIN), ModItems.tin_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.tin_ore))
                .build(consumer);
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.ORE_LEAD), ModItems.lead_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.lead_ore))
                .build(consumer);
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.ORE_SILVER), ModItems.silver_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.silver_ore))
                .build(consumer);
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.ORE_URANIUM), ModItems.uranium_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.uranium_ore))
                .build(consumer);
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.ORE_NICKEL), ModItems.nickel_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.nickel_ore))
                .build(consumer);
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.ORE_PLATINUM), ModItems.platinum_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.platinum_ore))
                .build(consumer);
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.ORE_TITANIUM), ModItems.titanium_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.titanium_ore))
                .build(consumer);

        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_COPPER), ModItems.copper_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.copper_dust))
                .build(consumer, "autotech:dust_copper_ingot");
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_TIN), ModItems.tin_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.tin_dust))
                .build(consumer, "autotech:dust_tin_ingot");
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_LEAD), ModItems.lead_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.lead_dust))
                .build(consumer, "autotech:dust_lead_ingot");
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_SILVER), ModItems.silver_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.silver_dust))
                .build(consumer, "autotech:dust_silver_ingot");
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_URANIUM), ModItems.uranium_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.uranium_dust))
                .build(consumer, "autotech:dust_uranium_ingot");
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_NICKEL), ModItems.nickel_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.nickel_dust))
                .build(consumer, "autotech:dust_nickel_ingot");
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_PLATINUM), ModItems.platinum_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.platinum_dust))
                .build(consumer, "autotech:dust_platinum_ingot");
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_TITANIUM), ModItems.titanium_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.titanium_dust))
                .build(consumer, "autotech:dust_titanium_ingot");

        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_BRONZE), ModItems.bronze_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.bronze_dust))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.copper_plate)
                .addIngredient(Ingredient.fromTag(ModTags.Items.INGOTS_COPPER), 4)
                .addCriterion("has_item", hasItem(ModItems.copper_ingot))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.copper_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.ORE_COPPER)
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

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.bronze_dust, 4)
                .addIngredient(ModTags.Items.DUSTS_TIN)
                .addIngredient(Ingredient.fromTag(ModTags.Items.DUSTS_COPPER), 3)
                .addCriterion("has_item", hasItem(ModItems.copper_dust))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.lv_machine_hull)
                .key('P', ModItems.copper_plate)
                .key('B', ModItems.bronze_plate)
                .patternLine("BBB")
                .patternLine("BPB")
                .patternLine("BBB")
                .addCriterion("has_item", hasItem(ModItems.bronze_plate))
                .build(consumer);
    }
}
