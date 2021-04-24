package binary404.autotech.common.core.recipe.map;

import binary404.autotech.AutoTech;
import binary404.autotech.common.core.recipe.core.CountableIngredient;
import binary404.autotech.common.core.recipe.core.Recipe;
import binary404.autotech.common.core.util.FluidHelper;
import binary404.autotech.common.core.util.Util;
import binary404.autotech.common.core.util.ValidationResult;
import binary404.autotech.common.core.util.ValidationResultType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

public abstract class RecipeBuilder<R extends RecipeBuilder<R>> {

    protected RecipeMap<R> recipeMap;

    protected List<CountableIngredient> inputs;
    protected NonNullList<ItemStack> outputs;
    protected List<Recipe.ChanceEntry> chancedOutputs;

    protected List<FluidStack> fluidInputs;
    protected List<FluidStack> fluidOutputs;

    protected int duration, energyPerTick;
    protected boolean hidden = false;

    protected ValidationResultType recipeStatus = ValidationResultType.VALID;

    protected RecipeBuilder() {
        this.inputs = NonNullList.create();
        this.outputs = NonNullList.create();
        this.chancedOutputs = new ArrayList<>();

        this.fluidInputs = new ArrayList<>(0);
        this.fluidOutputs = new ArrayList<>(0);
    }

    protected RecipeBuilder(Recipe recipe, RecipeMap<R> recipeMap) {
        this.recipeMap = recipeMap;
        this.inputs = NonNullList.create();
        this.inputs.addAll(recipe.getInputs());
        this.outputs = NonNullList.create();
        this.outputs.addAll(Util.copyStackList(recipe.getOutputs()));
        this.chancedOutputs = new ArrayList<>(recipe.getChancedOutputs());

        this.fluidInputs = FluidHelper.copyFluidList(recipe.getFluidInputs());
        this.fluidOutputs = FluidHelper.copyFluidList(recipe.getFluidOutputs());

        this.duration = recipe.getDuration();
        this.energyPerTick = recipe.getEnergyPerTick();
        this.hidden = recipe.isHidden();
    }

    @SuppressWarnings("CopyConstructorMissesField")
    protected RecipeBuilder(RecipeBuilder<R> recipeBuilder) {
        this.recipeMap = recipeBuilder.recipeMap;
        this.inputs = NonNullList.create();
        this.inputs.addAll(recipeBuilder.getInputs());
        this.outputs = NonNullList.create();
        this.outputs.addAll(Util.copyStackList(recipeBuilder.getOutputs()));
        this.chancedOutputs = new ArrayList<>(recipeBuilder.chancedOutputs);

        this.fluidInputs = FluidHelper.copyFluidList(recipeBuilder.getFluidInputs());
        this.fluidOutputs = FluidHelper.copyFluidList(recipeBuilder.getFluidOutputs());
        this.duration = recipeBuilder.duration;
        this.energyPerTick = recipeBuilder.energyPerTick;
        this.hidden = recipeBuilder.hidden;
    }

    public boolean applyProperty(String key, Object value) {
        return false;
    }

    public boolean applyProperty(String key, ItemStack item) {
        return false;
    }

    public R inputs(ItemStack... inputs) {
        return inputs(Arrays.asList(inputs));
    }

    public R inputs(Collection<ItemStack> inputs) {
        if (Util.iterableContains(inputs, stack -> stack == null || stack.isEmpty())) {
            recipeStatus = ValidationResultType.INVALID;
        }
        inputs.forEach(stack -> {
            if (!(stack == null || stack.isEmpty())) {
                this.inputs.add(CountableIngredient.from(stack));
            }
        });
        return (R) this;
    }

    public R input(ITag.INamedTag<Item> tag, int count) {
        return inputs(CountableIngredient.from(tag, count));
    }

    public R input(Item item) {
        return input(item, 1);
    }

    public R input(Item item, int count) {
        return inputs(new ItemStack(item, count));
    }

    public R input(Block item) {
        return input(item, 1);
    }

    public R input(Block item, int count) {
        return inputs(new ItemStack(item, count));
    }

    public R inputs(CountableIngredient... inputs) {
        List<CountableIngredient> ingredients = new ArrayList<>();
        for (CountableIngredient input : inputs) {
            if (input.getCount() < 0) {
                AutoTech.LOGGER.error("Count is less than 0");
                AutoTech.LOGGER.error("Stacktrace:", new IllegalArgumentException());
            } else {
                ingredients.add(input);
            }
        }

        return inputsIngredients(ingredients);
    }

    public R inputsIngredients(Collection<CountableIngredient> ingredients) {
        this.inputs.addAll(ingredients);
        return (R) this;
    }

    public R notConsumable(ItemStack itemStack) {
        return inputs(CountableIngredient.from(itemStack, 0));
    }

    public R notConsumable(ITag.INamedTag<Item> tag) {
        return inputs(CountableIngredient.from(tag, 0));
    }

    public R notConsumable(Ingredient ingredient) {
        return inputs(new CountableIngredient(ingredient, 0));
    }

    public R notConsumable(FluidStack fluidStack) {
        return fluidInputs(new FluidStack(fluidStack, 0));
    }

    public R output(Item item) {
        return output(item, 1);
    }

    public R output(Item item, int count) {
        return outputs(new ItemStack(item, count));
    }

    public R output(Block item) {
        return output(item, 1);
    }

    public R output(Block item, int count) {
        return outputs(new ItemStack(item, count));
    }

    public R outputs(ItemStack... outputs) {
        return outputs(Arrays.asList(outputs));
    }

    public R outputs(Collection<ItemStack> outputs) {
        outputs = new ArrayList<>(outputs);
        outputs.removeIf(stack -> stack == null || stack.isEmpty());
        this.outputs.addAll(outputs);
        return (R) this;
    }

    public R fluidInputs(FluidStack... inputs) {
        return fluidInputs(Arrays.asList(inputs));
    }

    public R fluidInputs(Collection<FluidStack> inputs) {
        if (inputs.contains(null) || inputs.contains(FluidStack.EMPTY)) {
            AutoTech.LOGGER.error("Fluid input null");
            AutoTech.LOGGER.error("Stacktrace:", new IllegalArgumentException());
            recipeStatus = ValidationResultType.INVALID;
        }
        this.fluidInputs.addAll(inputs);
        this.fluidInputs.removeIf(Objects::isNull);
        return (R) this;
    }

    public R fluidOutputs(FluidStack... outputs) {
        return fluidOutputs(Arrays.asList(outputs));
    }

    public R fluidOutputs(Collection<FluidStack> outputs) {
        outputs = new ArrayList<>(outputs);
        outputs.removeIf(Objects::isNull);
        outputs.removeIf(FluidStack::isEmpty);
        this.fluidOutputs.addAll(outputs);
        return (R) this;
    }

    public R chancedOutput(ItemStack stack, int chance, int tierChanceBoost) {
        if (stack == null || stack.isEmpty()) {
            return (R) this;
        }
        if (0 >= chance || chance > Recipe.getMaxChancedValue()) {
            AutoTech.LOGGER.error("Chance out of range");
            AutoTech.LOGGER.error("Stacktrace:", new IllegalArgumentException());
            recipeStatus = ValidationResultType.INVALID;
            return (R) this;
        }
        this.chancedOutputs.add(new Recipe.ChanceEntry(stack.copy(), chance, tierChanceBoost));
        return (R) this;
    }

    public R duration(int duration) {
        this.duration = duration;
        return (R) this;
    }

    public R energyPerTick(int energyPerTick) {
        this.energyPerTick = energyPerTick;
        return (R) this;
    }

    public R hidden() {
        this.hidden = true;
        return (R) this;
    }

    public R setRecipeMap(RecipeMap<R> recipeMap) {
        this.recipeMap = recipeMap;
        return (R) this;
    }

    public abstract R copy();

    protected ValidationResultType finalizeAndValidate() {
        return validate();
    }

    public abstract ValidationResult<Recipe> build();

    protected ValidationResultType validate() {
        if (energyPerTick == 0) {
            AutoTech.LOGGER.error("Energy Per Tick cannot be equal to 0", new IllegalArgumentException());
            recipeStatus = ValidationResultType.INVALID;
        }
        if (duration <= 0) {
            AutoTech.LOGGER.error("Duration cannot be less or equal to 0", new IllegalArgumentException());
            recipeStatus = ValidationResultType.INVALID;
        }
        if (recipeStatus == ValidationResultType.INVALID) {
            AutoTech.LOGGER.error("Invalid recipe, read the errors above: {}", this);
        }
        return recipeStatus;
    }

    public void buildAndRegister() {
        ValidationResult<Recipe> validationResult = build();
        recipeMap.addRecipe(validationResult);
    }

    public List<CountableIngredient> getInputs() {
        return inputs;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }

    public List<Recipe.ChanceEntry> getChancedOutputs() {
        return chancedOutputs;
    }

    public List<FluidStack> getFluidInputs() {
        return fluidInputs;
    }

    public List<FluidStack> getFluidOutputs() {
        return fluidOutputs;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("recipeMap", recipeMap)
                .append("inputs", inputs)
                .append("outputs", outputs)
                .append("chancedOutputs", chancedOutputs)
                .append("fluidInputs", fluidInputs)
                .append("fluidOutputs", fluidOutputs)
                .append("duration", duration)
                .append("EnergyPerTick", energyPerTick)
                .append("hidden", hidden)
                .append("recipeStatus", recipeStatus)
                .toString();
    }

}
