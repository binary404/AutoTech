package binary404.autotech.data;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.item.ModItems;
import binary404.autotech.common.tags.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

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
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.ORES_COPPER), ModItems.copper_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.copper_ore))
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.ORES_TIN), ModItems.tin_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.tin_ore))
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.ORES_LEAD), ModItems.lead_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.lead_ore))
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.ORES_SILVER), ModItems.silver_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.silver_ore))
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.ORES_URANIUM), ModItems.uranium_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.uranium_ore))
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.ORES_NICKEL), ModItems.nickel_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModBlocks.nickel_ore))
                .build(consumer);

        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.RAW_COPPER), ModItems.copper_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModTags.Items.RAW_COPPER))
                .build(consumer, "autotech:copper_ingot_raw");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.RAW_TIN), ModItems.tin_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModTags.Items.RAW_TIN))
                .build(consumer, "autotech:tin_ingot_raw");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.RAW_LEAD), ModItems.lead_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModTags.Items.RAW_LEAD))
                .build(consumer, "autotech:lead_ingot_raw");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.RAW_SILVER), ModItems.silver_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModTags.Items.RAW_SILVER))
                .build(consumer, "autotech:silver_ingot_raw");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.RAW_URANIUM), ModItems.uranium_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModTags.Items.RAW_URANIUM))
                .build(consumer, "autotech:uranium_ingot_raw");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.RAW_NICKEL), ModItems.nickel_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModTags.Items.RAW_NICKEL))
                .build(consumer, "autotech:nickel_ingot_raw");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.RAW_IRON), Items.IRON_INGOT, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModTags.Items.RAW_IRON))
                .build(consumer, "autotech:iron_ingot_raw");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.RAW_GOLD), Items.GOLD_INGOT, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModTags.Items.RAW_GOLD))
                .build(consumer, "autotech:gold_ingot_raw");

        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_COPPER), ModItems.copper_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.copper_dust))
                .build(consumer, "autotech:copper_ingot_dust");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_TIN), ModItems.tin_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.tin_dust))
                .build(consumer, "autotech:tin_ingot_dust");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_LEAD), ModItems.lead_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.lead_dust))
                .build(consumer, "autotech:lead_ingot_dust");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_SILVER), ModItems.silver_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.silver_dust))
                .build(consumer, "autotech:silver_ingot_dust");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_URANIUM), ModItems.uranium_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.uranium_dust))
                .build(consumer, "autotech:uranium_ingot_dust");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_NICKEL), ModItems.nickel_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.nickel_dust))
                .build(consumer, "autotech:nickel_ingot_dust");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_PLATINUM), ModItems.platinum_ingot, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.platinum_dust))
                .build(consumer, "autotech:platinum_ingot_dust");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_IRON), Items.IRON_INGOT, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.iron_dust))
                .build(consumer, "autotech:iron_ingot_dust");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_GOLD), Items.GOLD_INGOT, 0.1F, 200)
                .addCriterion("has_item", hasItem(ModItems.gold_dust))
                .build(consumer, "autotech:gold_ingot_dust");
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_NETHERITE), Items.NETHERITE_INGOT, 0.4F, 200)
                .addCriterion("has_item", hasItem(ModItems.netherite_dust))
                .build(consumer, "autotech:netherite_ingot_dust");

        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ModTags.Items.DUSTS_BRONZE), ModItems.bronze_ingot, 0.1F, 200).
                addCriterion("has_item", hasItem(ModItems.bronze_dust))
                .build(consumer);

        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModItems.flour), Items.BREAD, 0.2F, 200)
                .addCriterion("has_item", hasItem(ModItems.flour))
                .build(consumer, "autotech:bread_flour");

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.copper_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.INGOTS_COPPER)
                .addCriterion("has_item", hasItem(ModTags.Items.INGOTS_COPPER))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.tin_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.INGOTS_TIN)
                .addCriterion("has_item", hasItem(ModTags.Items.INGOTS_TIN))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.lead_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.INGOTS_LEAD)
                .addCriterion("has_item", hasItem(ModTags.Items.INGOTS_LEAD))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.silver_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.INGOTS_SILVER)
                .addCriterion("has_item", hasItem(ModTags.Items.INGOTS_SILVER))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.nickel_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.INGOTS_NICKEL)
                .addCriterion("has_item", hasItem(ModTags.Items.INGOTS_NICKEL))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.iron_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(Tags.Items.INGOTS_IRON)
                .addCriterion("has_item", hasItem(Tags.Items.INGOTS_IRON))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.gold_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(Tags.Items.INGOTS_GOLD)
                .addCriterion("has_item", hasItem(Tags.Items.INGOTS_GOLD))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.copper_ore_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.ORES_COPPER)
                .addCriterion("has_item", hasItem(ModTags.Items.ORES_COPPER))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.tin_ore_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.ORES_TIN)
                .addCriterion("has_item", hasItem(ModTags.Items.ORES_TIN))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.lead_ore_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.ORES_LEAD)
                .addCriterion("has_item", hasItem(ModTags.Items.ORES_LEAD))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.silver_ore_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.ORES_SILVER)
                .addCriterion("has_item", hasItem(ModTags.Items.ORES_SILVER))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.nickel_ore_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.ORES_NICKEL)
                .addCriterion("has_item", hasItem(ModTags.Items.ORES_NICKEL))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.iron_ore_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(Tags.Items.ORES_IRON)
                .addCriterion("has_item", hasItem(Tags.Items.ORES_IRON))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.gold_ore_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(Tags.Items.ORES_GOLD)
                .addCriterion("has_item", hasItem(Tags.Items.ORES_GOLD))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.copper_ore_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.RAW_COPPER)
                .addCriterion("has_item", hasItem(ModTags.Items.RAW_COPPER))
                .build(consumer, "raw_copper_dust");
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.tin_ore_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.RAW_TIN)
                .addCriterion("has_item", hasItem(ModTags.Items.RAW_TIN))
                .build(consumer, "raw_tin_dust");
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.lead_ore_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.RAW_LEAD)
                .addCriterion("has_item", hasItem(ModTags.Items.RAW_LEAD))
                .build(consumer, "raw_lead_dust");
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.silver_ore_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.RAW_SILVER)
                .addCriterion("has_item", hasItem(ModTags.Items.RAW_SILVER))
                .build(consumer, "raw_silver_dust");
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.nickel_ore_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.RAW_NICKEL)
                .addCriterion("has_item", hasItem(ModTags.Items.RAW_NICKEL))
                .build(consumer, "raw_nickel_dust");
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.iron_ore_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.RAW_IRON)
                .addCriterion("has_item", hasItem(ModTags.Items.RAW_IRON))
                .build(consumer, "raw_iron_dust");
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.gold_ore_dust)
                .addIngredient(ModItems.mortar)
                .addIngredient(ModTags.Items.RAW_GOLD)
                .addCriterion("has_item", hasItem(ModTags.Items.RAW_GOLD))
                .build(consumer, "raw_gold_dust");

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.copper_plate)
                .addIngredient(Ingredient.fromTag(ModTags.Items.INGOTS_COPPER), 2)
                .addIngredient(ModItems.hammer)
                .addCriterion("has_item", hasItem(ModItems.copper_ingot))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.tin_plate)
                .addIngredient(Ingredient.fromTag(ModTags.Items.INGOTS_TIN), 2)
                .addIngredient(ModItems.hammer)
                .addCriterion("has_item", hasItem(ModItems.tin_ingot))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.lead_plate)
                .addIngredient(Ingredient.fromTag(ModTags.Items.INGOTS_LEAD), 2)
                .addIngredient(ModItems.hammer)
                .addCriterion("has_item", hasItem(ModItems.lead_ingot))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.silver_plate)
                .addIngredient(Ingredient.fromTag(ModTags.Items.INGOTS_SILVER), 2)
                .addIngredient(ModItems.hammer)
                .addCriterion("has_item", hasItem(ModItems.silver_ingot))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.uranium_plate)
                .addIngredient(Ingredient.fromTag(ModTags.Items.INGOTS_URANIUM), 2)
                .addIngredient(ModItems.hammer)
                .addCriterion("has_item", hasItem(ModItems.uranium_plate))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.nickel_plate)
                .addIngredient(Ingredient.fromTag(ModTags.Items.INGOTS_NICKEL), 2)
                .addIngredient(ModItems.hammer)
                .addCriterion("has_item", hasItem(ModItems.nickel_ingot))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.iron_plate)
                .addIngredient(Ingredient.fromTag(Tags.Items.INGOTS_IRON), 2)
                .addIngredient(ModItems.hammer)
                .addCriterion("has_item", hasItem(Items.IRON_INGOT))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.gold_plate)
                .addIngredient(Ingredient.fromTag(Tags.Items.INGOTS_GOLD), 2)
                .addIngredient(ModItems.hammer)
                .addCriterion("has_item", hasItem(Items.GOLD_INGOT))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.bronze_plate)
                .addIngredient(Ingredient.fromTag(ModTags.Items.INGOTS_BRONZE), 2)
                .addIngredient(ModItems.hammer)
                .addCriterion("has_item", hasItem(ModItems.bronze_ingot))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.steel_plate)
                .addIngredient(Ingredient.fromTag(ModTags.Items.INGOTS_STEEL), 2)
                .addIngredient(ModItems.hammer)
                .addCriterion("has_item", hasItem(ModItems.steel_ingot))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.iron_rod, 2)
                .addIngredient(Ingredient.fromTag(ModTags.Items.PLATES_IRON), 2)
                .addCriterion("has_item", hasItem(ModTags.Items.PLATES_IRON))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.steel_rod, 2)
                .addIngredient(Ingredient.fromTag(ModTags.Items.PLATES_STEEL), 2)
                .addCriterion("has_item", hasItem(ModTags.Items.PLATES_STEEL))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.bronze_dust, 4)
                .addIngredient(ModTags.Items.DUSTS_TIN)
                .addIngredient(Ingredient.fromTag(ModTags.Items.DUSTS_COPPER), 3)
                .addCriterion("has_item", hasItem(ModItems.copper_dust))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.red_alloy_dust, 2)
                .addIngredient(ModTags.Items.DUSTS_COPPER)
                .addIngredient(Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE), 2)
                .addCriterion("has_item", hasItem(ModTags.Items.DUSTS_COPPER))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.mortar)
                .key('S', Items.STONE)
                .key('I', Items.IRON_INGOT)
                .patternLine(" I ")
                .patternLine("SIS")
                .patternLine("SSS")
                .addCriterion("has_item", hasItem(Items.STONE))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.saw_blade)
                .key('I', Items.IRON_INGOT)
                .key('B', ModTags.Items.PLATES_BRONZE)
                .patternLine("II ")
                .patternLine("IBI")
                .patternLine(" II")
                .addCriterion("has_item", hasItem(ModItems.bronze_plate))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.mv_grinder_blade)
                .key('D', Tags.Items.GEMS_DIAMOND)
                .key('S', ModTags.Items.INGOTS_STEEL)
                .key('N', ModTags.Items.PLATES_NICKEL)
                .patternLine("DSD")
                .patternLine("SNS")
                .patternLine("DSD")
                .addCriterion("has_item", hasItem(ModTags.Items.INGOTS_STEEL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.iron_plating, 8)
                .key('I', ModItems.iron_plate)
                .key('H', ModItems.hammer)
                .patternLine("III")
                .patternLine("IHI")
                .patternLine("III")
                .addCriterion("has_item", hasItem(ModItems.iron_plate))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.thermo_electric_component)
                .key('I', Items.IRON_INGOT)
                .key('C', ModItems.copper_ingot)
                .key('R', Items.REDSTONE)
                .patternLine("III")
                .patternLine("CRC")
                .patternLine("RCR")
                .addCriterion("has_item", hasItem(ModItems.copper_ingot))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.heat_proof_casing, 4)
                .key('I', ModBlocks.iron_plating)
                .key('L', ModItems.lead_plate)
                .key('T', ModItems.nickel_plate)
                .patternLine("ILI")
                .patternLine("LTL")
                .patternLine("ILI")
                .addCriterion("has_item", hasItem(ModItems.lead_plate))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.hammer)
                .key('I', Tags.Items.INGOTS_IRON)
                .key('S', Items.STICK)
                .patternLine("II ")
                .patternLine("IIS")
                .patternLine("II ")
                .addCriterion("has_item", hasItem(Tags.Items.INGOTS_IRON))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.mv_centrifugal_component)
                .key('M', ModItems.mv_motor)
                .key('L', ModTags.Items.PLATES_LEAD)
                .patternLine("LLL")
                .patternLine("LML")
                .patternLine("LLL")
                .addCriterion("has_item", hasItem(ModItems.mv_motor))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.mv_logic_component)
                .key('R', Items.REDSTONE)
                .key('D', Items.DIAMOND)
                .key('S', ModItems.silver_ingot)
                .key('C', ModItems.copper_ingot)
                .patternLine("RRR")
                .patternLine("SDS")
                .patternLine("CRC")
                .addCriterion("has_item", hasItem(Items.DIAMOND))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.mv_logic_circuit)
                .addIngredient(ModItems.mv_logic_component)
                .addIngredient(ModItems.basic_circuit_board)
                .addIngredient(ModItems.mv_vacuum_tube, 2)
                .addIngredient(ModItems.mv_resistor)
                .addCriterion("has_item", hasItem(ModItems.basic_circuit_board))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.mv_transmitter_component)
                .key('R', Items.REDSTONE)
                .key('S', ModItems.silver_ingot)
                .key('C', ModItems.copper_ingot)
                .key('I', Items.IRON_INGOT)
                .patternLine("RRR")
                .patternLine("CIC")
                .patternLine("SRS")
                .addCriterion("has_item", hasItem(Items.IRON_INGOT))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.mv_transmitter_circuit)
                .addIngredient(ModItems.mv_transmitter_component)
                .addIngredient(ModItems.basic_circuit_board)
                .addIngredient(ModItems.mv_vacuum_tube, 2)
                .addIngredient(ModItems.mv_resistor)
                .addCriterion("has_item", hasItem(ModItems.basic_circuit_board))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.mv_receiver_component)
                .key('R', Items.REDSTONE)
                .key('S', ModItems.silver_ingot)
                .key('C', ModItems.copper_ingot)
                .key('I', Items.IRON_INGOT)
                .patternLine("CRC")
                .patternLine("SIS")
                .patternLine("RRR")
                .addCriterion("has_item", hasItem(Items.IRON_INGOT))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.mv_receiver_circuit)
                .addIngredient(ModItems.mv_receiver_component)
                .addIngredient(ModItems.basic_circuit_board)
                .addIngredient(ModItems.mv_vacuum_tube, 2)
                .addIngredient(ModItems.mv_resistor)
                .addCriterion("has_item", hasItem(ModItems.basic_circuit_board))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.basic_circuit_board)
                .addIngredient(ModItems.carbon_mesh)
                .addIngredient(ModItems.plywood)
                .addCriterion("has_item", hasItem(ModItems.carbon_mesh))
                .build(consumer);
    }
}
