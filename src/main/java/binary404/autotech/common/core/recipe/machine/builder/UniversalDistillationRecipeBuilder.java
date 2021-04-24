package binary404.autotech.common.core.recipe.machine.builder;

import binary404.autotech.common.core.recipe.RecipeMaps;
import binary404.autotech.common.core.recipe.core.Recipe;
import binary404.autotech.common.core.recipe.map.RecipeBuilder;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import binary404.autotech.common.core.util.ValidationResult;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.fluids.FluidStack;

public class UniversalDistillationRecipeBuilder extends RecipeBuilder<UniversalDistillationRecipeBuilder> {

    public UniversalDistillationRecipeBuilder() {

    }

    public UniversalDistillationRecipeBuilder(Recipe recipe, RecipeMap<UniversalDistillationRecipeBuilder> recipeMap) {
        super(recipe, recipeMap);
    }

    public UniversalDistillationRecipeBuilder(RecipeBuilder<UniversalDistillationRecipeBuilder> recipeBuilder) {
        super(recipeBuilder);
    }

    @Override
    public UniversalDistillationRecipeBuilder copy() {
        return new UniversalDistillationRecipeBuilder(this);
    }

    @Override
    public void buildAndRegister() {
        //Add to other recipes maybe?
        super.buildAndRegister();
    }

    @Override
    public ValidationResult<Recipe> build() {
        return ValidationResult.newResult(finalizeAndValidate(),
                new Recipe(inputs, outputs, chancedOutputs, fluidInputs, fluidOutputs,
                        ImmutableMap.of(), duration, energyPerTick, hidden));
    }
}
