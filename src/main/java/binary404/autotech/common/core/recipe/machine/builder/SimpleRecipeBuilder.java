package binary404.autotech.common.core.recipe.machine.builder;

import binary404.autotech.common.core.recipe.core.Recipe;
import binary404.autotech.common.core.recipe.map.RecipeBuilder;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import binary404.autotech.common.core.util.ValidationResult;
import com.google.common.collect.ImmutableMap;

public class SimpleRecipeBuilder extends RecipeBuilder<SimpleRecipeBuilder> {


    public SimpleRecipeBuilder() {

    }

    public SimpleRecipeBuilder(Recipe recipe, RecipeMap<SimpleRecipeBuilder> recipeMap) {
        super(recipe, recipeMap);
    }

    public SimpleRecipeBuilder(RecipeBuilder<SimpleRecipeBuilder> recipeBuilder) {
        super(recipeBuilder);
    }

    @Override
    public SimpleRecipeBuilder copy() {
        return new SimpleRecipeBuilder(this);
    }

    @Override
    public ValidationResult<Recipe> build() {
        return ValidationResult.newResult(finalizeAndValidate(), new Recipe(inputs, outputs, chancedOutputs, fluidInputs, fluidOutputs, ImmutableMap.of(), duration, energyPerTick, hidden));
    }
}
