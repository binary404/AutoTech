package binary404.autotech.common.core.recipe;

import binary404.autotech.common.core.recipe.machine.builder.SimpleRecipeBuilder;
import binary404.autotech.common.core.recipe.machine.builder.UniversalDistillationRecipeBuilder;
import binary404.autotech.common.core.recipe.map.FuelRecipeMap;
import binary404.autotech.common.core.recipe.map.RecipeMap;

public class RecipeMaps {

    public static final RecipeMap<SimpleRecipeBuilder> GRINDER_RECIPES = new RecipeMap<>("grinder", 1, 1, 1, 3, 0, 0, 0, 0, new SimpleRecipeBuilder().duration(150).energyPerTick(8));

    public static final RecipeMap<SimpleRecipeBuilder> BREWING_RECIPES = new RecipeMap<>("brewer", 1, 1, 0, 0, 1, 1, 1, 1, new SimpleRecipeBuilder().duration(128).energyPerTick(4));

    public static RecipeMap<UniversalDistillationRecipeBuilder> DISTILLATION_RECIPES = new RecipeMap<>("distillation_tower", 0, 0, 0, 1, 1, 1, 1, 13, new UniversalDistillationRecipeBuilder());

    public static final RecipeMap<SimpleRecipeBuilder> EMPTY = new RecipeMap<>("empty", 1, 1, 1, 1, 1, 1, 1, 1, new SimpleRecipeBuilder());

    public static final FuelRecipeMap STEAM_TURBINE_FUELS = new FuelRecipeMap("steam_turbine");

    public static final FuelRecipeMap EMPTY_FUELS = new FuelRecipeMap("empty");
}
