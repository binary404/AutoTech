package binary404.autotech.common.core.plugin.crafttweaker;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.recipe.core.Recipe;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.impl.fluid.MCFluidStack;
import com.blamejared.crafttweaker.impl.item.MCItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.openzen.zencode.java.ZenCodeType.*;

public class CTRecipe {

    private final RecipeMap<?> recipeMap;
    private final Recipe backingRecipe;

    public CTRecipe(RecipeMap<?> recipeMap, Recipe backingRecipe) {
        this.recipeMap = recipeMap;
        this.backingRecipe = backingRecipe;
    }

    @Getter("inputs")
    public List<IIngredient> getInputs() {
        return this.backingRecipe.getInputs().stream()
                .map(countableIngredient -> IIngredient.fromIngredient(countableIngredient.getIngredient()))
                .collect(Collectors.toList());
    }

    @Getter("outputs")
    public List<IItemStack> getOutputs() {
        return this.backingRecipe.getOutputs().stream()
                .map(MCItemStack::new)
                .collect(Collectors.toList());
    }

    @Method
    public List<IItemStack> getResultItemOutputs(@OptionalLong(-1) long randomSeed, @OptionalLong(-1) int tier) {
        return this.backingRecipe.getResultItemOutputs(Integer.MAX_VALUE, randomSeed == -1L ? new Random() : new Random(randomSeed), Tier.values()[tier]).stream()
                .map(MCItemStack::new)
                .collect(Collectors.toList());
    }

    @Getter("changedOutputs")
    public List<ChancedEntry> getChancedOutputs() {
        ArrayList<ChancedEntry> result = new ArrayList<>();
        this.backingRecipe.getChancedOutputs().forEach(chanceEntry ->
                result.add(new ChancedEntry(new MCItemStack(chanceEntry.getItemStack()), chanceEntry.getChance(), chanceEntry.getBoostPerTier())));
        return result;
    }

    //Typo Fix
    @Getter("chancedOutputs")
    public List<ChancedEntry> getChancedOutputsFix() {
        return getChancedOutputs();
    }

    @Getter("fluidInputs")
    public List<IFluidStack> getFluidInputs() {
        return this.backingRecipe.getFluidInputs().stream()
                .map(MCFluidStack::new)
                .collect(Collectors.toList());
    }

    @Method
    public boolean hasInputFluid(IFluidStack liquidStack) {
        return this.backingRecipe.hasInputFluid(liquidStack.getInternal());
    }

    @Getter("fluidOutputs")
    public List<IFluidStack> getFluidOutputs() {
        return this.backingRecipe.getFluidOutputs().stream()
                .map(MCFluidStack::new)
                .collect(Collectors.toList());
    }

    @Getter("duration")
    public int getDuration() {
        return this.backingRecipe.getDuration();
    }

    @Getter("energyPerTick")
    public int getEnergyPerTick() {
        return this.backingRecipe.getEnergyPerTick();
    }

    @Getter("hidden")
    public boolean isHidden() {
        return this.backingRecipe.isHidden();
    }

    @Getter("propertyKeys")
    public List<String> getPropertyKeys() {
        return new ArrayList<>(this.backingRecipe.getPropertyKeys());
    }

    @Method
    public Object getProperty(String key) {
        return this.backingRecipe.getProperty(key);
    }

    @Method
    public boolean remove() {
        return this.recipeMap.removeRecipe(this.backingRecipe);
    }

}
