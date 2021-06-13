package binary404.autotech.common.core.recipe.machine.builder;

import binary404.autotech.common.core.recipe.core.Recipe;
import binary404.autotech.common.core.recipe.map.RecipeBuilder;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import binary404.autotech.common.core.util.ValidationResult;
import binary404.autotech.common.core.util.ValidationResultType;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BlastRecipeBuilder extends RecipeBuilder<BlastRecipeBuilder> {

    private int blastFurnaceTemp;

    public BlastRecipeBuilder() {
    }

    public BlastRecipeBuilder(Recipe recipe, RecipeMap<BlastRecipeBuilder> recipeMap) {
        super(recipe, recipeMap);
        this.blastFurnaceTemp = recipe.getIntegerProperty("blastFurnaceTemp");
    }

    public BlastRecipeBuilder(RecipeBuilder<BlastRecipeBuilder> recipeBuilder) {
        super(recipeBuilder);
    }

    @Override
    public BlastRecipeBuilder copy() {
        return new BlastRecipeBuilder(this);
    }

    @Override
    public boolean applyProperty(String key, Object value) {
        if (key.equals("temperature")) {
            this.blastFurnaceTemp(((Number) value).intValue());
            return true;
        }
        return true;
    }

    public BlastRecipeBuilder blastFurnaceTemp(int blastFurnaceTemp) {
        if (blastFurnaceTemp <= 0) {
            recipeStatus = ValidationResultType.INVALID;
        }
        this.blastFurnaceTemp = blastFurnaceTemp;
        return this;
    }

    public ValidationResult<Recipe> build() {
        return ValidationResult.newResult(finalizeAndValidate(),
                new Recipe(inputs, outputs, chancedOutputs, fluidInputs, fluidOutputs,
                        ImmutableMap.of("blast_furnace_temperature", blastFurnaceTemp),
                        duration, energyPerTick, hidden));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("blast_furnace_temperature", blastFurnaceTemp)
                .toString();
    }

}
