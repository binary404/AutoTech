package binary404.autotech.common.core.recipe;

import binary404.autotech.client.gui.GuiTextures;
import binary404.autotech.client.gui.core.widget.ProgressWidget;
import binary404.autotech.common.core.recipe.machine.builder.BlastRecipeBuilder;
import binary404.autotech.common.core.recipe.machine.builder.SimpleRecipeBuilder;
import binary404.autotech.common.core.recipe.machine.builder.UniversalDistillationRecipeBuilder;
import binary404.autotech.common.core.recipe.map.FuelRecipeMap;
import binary404.autotech.common.core.recipe.map.RecipeMap;

public class RecipeMaps {

    public static final RecipeMap<SimpleRecipeBuilder> GRINDER_RECIPES = new RecipeMap<>("grinder", 1, 1, 1, 3, 0, 0, 0, 0, new SimpleRecipeBuilder().duration(150).energyPerTick(8));

    public static final RecipeMap<SimpleRecipeBuilder> MIXER_RECIPES = new RecipeMap<>("mixer", 0, 4, 1, 2, 0, 2, 0, 1, new SimpleRecipeBuilder().duration(200).energyPerTick(8));

    public static final RecipeMap<SimpleRecipeBuilder> BREWING_RECIPES = new RecipeMap<>("brewer", 1, 1, 0, 0, 1, 1, 1, 1, new SimpleRecipeBuilder().duration(128).energyPerTick(4));

    public static final RecipeMap<SimpleRecipeBuilder> SMELTING_RECIPES = new RecipeMap<>("furnace", 1, 1, 1, 1, 0, 0, 0, 0, new SimpleRecipeBuilder())
            .setSlotOverlay(false, false, GuiTextures.FURNACE_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, ProgressWidget.MoveType.HORIZONTAL);

    public static final RecipeMap<SimpleRecipeBuilder> COMPACTOR_RECIPES = new RecipeMap<>("compactor", 1, 1, 1, 1, 0, 0, 0,0, new SimpleRecipeBuilder().duration(300).energyPerTick(8));

    public static final RecipeMap<UniversalDistillationRecipeBuilder> DISTILLATION_RECIPES = new RecipeMap<>("distillation_tower", 0, 0, 0, 1, 1, 1, 1, 13, new UniversalDistillationRecipeBuilder());

    public static final RecipeMap<BlastRecipeBuilder> BLAST_RECIPES = new RecipeMap<>("blast_furnace", 1, 3, 1, 2, 0, 1, 0, 1, new BlastRecipeBuilder());

    public static final RecipeMap<SimpleRecipeBuilder> EMPTY = new RecipeMap<>("empty", 1, 1, 1, 1, 1, 1, 1, 1, new SimpleRecipeBuilder());

    public static final FuelRecipeMap DIESEL_GENERATOR_FUELS = new FuelRecipeMap("diesel_generator");

    public static final FuelRecipeMap STEAM_TURBINE_FUELS = new FuelRecipeMap("steam_turbine");

    public static final FuelRecipeMap SEMI_FLUID_GENERATOR_FUELS = new FuelRecipeMap("semi_fluid_generator");

    public static final FuelRecipeMap EMPTY_FUELS = new FuelRecipeMap("empty");
}
