package binary404.autotech.common.core.recipe.map;

import binary404.autotech.client.gui.GuiTextures;
import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.client.gui.core.texture.TextureArea;
import binary404.autotech.client.gui.core.widget.ProgressWidget;
import binary404.autotech.client.gui.core.widget.SlotWidget;
import binary404.autotech.client.gui.core.widget.TankWidget;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.logistics.fluid.FluidTankList;
import binary404.autotech.common.core.logistics.fluid.IMultipleTankHandler;
import binary404.autotech.common.core.plugin.crafttweaker.CTRecipe;
import binary404.autotech.common.core.recipe.core.MatchingMode;
import binary404.autotech.common.core.recipe.core.Recipe;
import binary404.autotech.common.core.util.FluidHelper;
import binary404.autotech.common.core.util.Util;
import binary404.autotech.common.core.util.ValidationResult;
import binary404.autotech.common.core.util.ValidationResultType;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IItemStack;
import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.hash.TByteObjectHashMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.openzen.zencode.java.ZenCodeType;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.DoubleSupplier;
import java.util.stream.Collectors;

@ZenCodeType.Name("mods.autotech.recipe.RecipeMap")
@ZenRegister
public class RecipeMap<R extends RecipeBuilder<R>> {

    private static final List<RecipeMap<?>> RECIPE_MAPS = new ArrayList<>();
    @ZenCodeType.Field
    public static IChanceFunction chanceFunction = (chance, boostPerTier, tier) -> chance + (boostPerTier * (tier.ordinal() + 1));
    private static boolean foundInvalidRecipe = false;

    public final String unlocalizedName;

    private final R recipeBuilderSample;
    private final int minInputs, maxInputs;
    private final int minOutputs, maxOutputs;
    private final int minFluidInputs, maxFluidInputs;
    private final int minFluidOutputs, maxFluidOutputs;
    private final TByteObjectMap<TextureArea> slotOverlays;
    protected TextureArea progressBarTexture;
    protected ProgressWidget.MoveType moveType;

    private final Map<FluidKey, Collection<Recipe>> recipeFluidMap = new HashMap<>();
    private final Collection<Recipe> recipeList = new ArrayList<>();

    public RecipeMap(String unlocalizedName, int minInputs, int maxInputs, int minOutputs, int maxOutputs, int minFluidInputs, int maxFluidInputs, int minFluidOutputs, int maxFluidOutputs, R defaultRecipe) {
        this.unlocalizedName = unlocalizedName;
        this.slotOverlays = new TByteObjectHashMap<>();
        this.progressBarTexture = GuiTextures.PROGRESS_BAR_ARROW;
        this.moveType = ProgressWidget.MoveType.HORIZONTAL;

        this.minInputs = minInputs;
        this.minFluidInputs = minFluidInputs;
        this.minOutputs = minOutputs;
        this.minFluidOutputs = minFluidOutputs;

        this.maxInputs = maxInputs;
        this.maxFluidInputs = maxFluidInputs;
        this.maxOutputs = maxOutputs;
        this.maxFluidOutputs = maxFluidOutputs;

        defaultRecipe.setRecipeMap(this);
        this.recipeBuilderSample = defaultRecipe;
        RECIPE_MAPS.add(this);
    }

    public void clearRecipes() {
        this.recipeList.clear();
    }

    @ZenCodeType.Method
    public static List<RecipeMap<?>> getRecipeMaps() {
        return Collections.unmodifiableList(RECIPE_MAPS);
    }

    @ZenCodeType.Method
    public static RecipeMap<?> getByname(String unlocalizedName) {
        return RECIPE_MAPS.stream()
                .filter(map -> map.unlocalizedName.equals(unlocalizedName))
                .findFirst().orElse(null);
    }

    public static IChanceFunction getChanceFunction() {
        return chanceFunction;
    }

    public static boolean isFoundInvalidRecipe() {
        return foundInvalidRecipe;
    }

    public static void setFoundInvalidRecipe(boolean foundInvalidRecipe) {
        RecipeMap.foundInvalidRecipe |= foundInvalidRecipe;
    }

    public RecipeMap<R> setProgressBar(TextureArea progressBar, ProgressWidget.MoveType moveType) {
        this.progressBarTexture = progressBar;
        this.moveType = moveType;
        return this;
    }

    public RecipeMap<R> setSlotOverlay(boolean isOutput, boolean isFluid, TextureArea slotOverlay) {
        return this.setSlotOverlay(isOutput, isFluid, false, slotOverlay).setSlotOverlay(isOutput, isFluid, true, slotOverlay);
    }

    public RecipeMap<R> setSlotOverlay(boolean isOutput, boolean isFluid, boolean isLast, TextureArea slotOverlay) {
        this.slotOverlays.put((byte) ((isOutput ? 2 : 0) + (isFluid ? 1 : 0) + (isLast ? 4 : 0)), slotOverlay);
        return this;
    }

    public boolean canInputFluidForce(Fluid fluid) {
        return false;
    }

    public Collection<Recipe> getRecipesForFluid(FluidStack fluid) {
        return recipeFluidMap.getOrDefault(new FluidKey(fluid), Collections.emptySet());
    }

    public void addRecipe(ValidationResult<Recipe> validationResult) {
        validationResult = postValidateRecipe(validationResult);
        switch (validationResult.getType()) {
            case SKIP:
                return;
            case INVALID:
                setFoundInvalidRecipe(true);
                return;
        }
        Recipe recipe = validationResult.getResult();
        recipeList.add(recipe);

        for (FluidStack fluid : recipe.getFluidInputs()) {
            recipeFluidMap.computeIfAbsent(new FluidKey(fluid), l -> new HashSet<>(1)).add(recipe);
        }
    }

    public boolean removeRecipe(Recipe recipe) {
        if (recipeList.remove(recipe)) {
            recipeFluidMap.values().forEach(fluidMap ->
                    fluidMap.removeIf(fluidRecipe -> fluidRecipe == recipe));
            return true;
        }
        return false;
    }

    protected ValidationResult<Recipe> postValidateRecipe(ValidationResult<Recipe> validationResult) {
        ValidationResultType recipeStatus = validationResult.getType();
        Recipe recipe = validationResult.getResult();
        if (!Util.isBetweenInclusive(getMinInputs(), getMaxInputs(), recipe.getInputs().size())) {
            recipeStatus = ValidationResultType.INVALID;
        }
        if (!Util.isBetweenInclusive(getMinOutputs(), getMaxOutputs(), recipe.getOutputs().size() + recipe.getChancedOutputs().size())) {
            recipeStatus = ValidationResultType.INVALID;
        }
        if (!Util.isBetweenInclusive(getMinFluidInputs(), getMaxFluidInputs(), recipe.getFluidInputs().size())) {
            recipeStatus = ValidationResultType.INVALID;
        }
        if (!Util.isBetweenInclusive(getMinFluidOutputs(), getMaxFluidOutputs(), recipe.getFluidOutputs().size())) {
            recipeStatus = ValidationResultType.INVALID;
        }
        return ValidationResult.newResult(recipeStatus, recipe);
    }

    @Nullable
    public Recipe findRecipe(long energy, IItemHandlerModifiable inputs, IMultipleTankHandler fluidInputs, int outputFluidTankCapacity) {
        return this.findRecipe(energy, Util.itemHandlerToList(inputs), FluidHelper.fluidHandlerToList(fluidInputs), outputFluidTankCapacity, MatchingMode.DEFAULT);
    }

    @Nullable
    public Recipe findRecipe(long energy, List<ItemStack> inputs, List<FluidStack> fluidInputs, int outputFluidTankCapacity) {
        return this.findRecipe(energy, inputs, fluidInputs, outputFluidTankCapacity, MatchingMode.DEFAULT);
    }

    @Nullable
    public Recipe findRecipe(long energy, IItemHandlerModifiable inputs, IMultipleTankHandler fluidInputs, int outputTankCapacity, MatchingMode matchingMode) {
        return this.findRecipe(energy, Util.itemHandlerToList(inputs), FluidHelper.fluidHandlerToList(fluidInputs), outputTankCapacity, matchingMode);
    }

    @Nullable
    public Recipe findRecipe(long energy, List<ItemStack> inputs, List<FluidStack> fluidInputs, int outputFluidtankCapacity, MatchingMode matchingMode) {
        if (recipeList.isEmpty())
            return null;
        if (minFluidInputs > 0 && Util.amountOfNonNullElements(fluidInputs) < minFluidInputs) {
            return null;
        }
        if (minInputs > 0 && Util.amountOfNonEmptyStacks(inputs) < minInputs) {
            return null;
        }
        if (maxInputs > 0) {
            return findByInputs(energy, inputs, fluidInputs, matchingMode);
        } else {
            return findByFluidInputs(energy, inputs, fluidInputs, matchingMode);
        }
    }

    @Nullable
    private Recipe findByFluidInputs(long energy, List<ItemStack> inputs, List<FluidStack> fluidInputs, MatchingMode matchingMode) {
        for (FluidStack fluid : fluidInputs) {
            if (fluid == null || fluid.isEmpty())
                continue;
            Collection<Recipe> recipes = recipeFluidMap.get(new FluidKey(fluid));
            if (recipes == null)
                continue;
            for (Recipe tmpRecipe : recipes) {
                if (tmpRecipe.matches(false, inputs, fluidInputs, matchingMode)) {
                    return energy >= tmpRecipe.getEnergyPerTick() ? tmpRecipe : null;
                }
            }
        }
        return null;
    }

    @Nullable
    private Recipe findByInputs(long energy, List<ItemStack> inputs, List<FluidStack> fluidInputs, MatchingMode matchingMode) {
        for (Recipe recipe : recipeList) {
            if (recipe.matches(false, inputs, fluidInputs, matchingMode)) {
                return energy >= recipe.getEnergyPerTick() ? recipe : null;
            }
        }
        return null;
    }

    public ModularUserInterface.Builder createJeiUITemplate(IItemHandlerModifiable importItems, IItemHandlerModifiable exportItems, FluidTankList importFluids, FluidTankList exportFluids) {
        return createUITemplate(() -> 0.0, importItems, exportItems, importFluids, exportFluids);
    }

    //this DOES NOT include machine control widgets or binds player inventory
    public ModularUserInterface.Builder createUITemplate(DoubleSupplier progressSupplier, IItemHandlerModifiable importItems, IItemHandlerModifiable exportItems, FluidTankList importFluids, FluidTankList exportFluids) {
        ModularUserInterface.Builder builder = ModularUserInterface.defaultBuilder();
        builder.widget(new ProgressWidget(progressSupplier, 77, 22, 21, 20, progressBarTexture, moveType));
        addInventorySlotGroup(builder, importItems, importFluids, false);
        addInventorySlotGroup(builder, exportItems, exportFluids, true);
        return builder;
    }

    protected void addInventorySlotGroup(ModularUserInterface.Builder builder, IItemHandlerModifiable itemHandler, FluidTankList fluidHandler, boolean isOutputs) {
        int itemInputsCount = itemHandler.getSlots();
        int fluidInputsCount = fluidHandler.getTanks();
        boolean invertFluids = false;
        if (itemInputsCount == 0) {
            int tmp = itemInputsCount;
            itemInputsCount = fluidInputsCount;
            fluidInputsCount = tmp;
            invertFluids = true;
        }
        int[] inputSlotGrid = determineSlotsGrid(itemInputsCount);
        int itemSlotsToLeft = inputSlotGrid[0];
        int itemSlotsToDown = inputSlotGrid[1];
        int startInputsX = isOutputs ? 106 : 69 - itemSlotsToLeft * 18;
        int startInputsY = 32 - (int) (itemSlotsToDown / 2.0 * 18);
        for (int i = 0; i < itemSlotsToDown; i++) {
            for (int j = 0; j < itemSlotsToLeft; j++) {
                int slotIndex = i * itemSlotsToLeft + j;
                int x = startInputsX + 18 * j;
                int y = startInputsY + 18 * i;
                addSlot(builder, x, y, slotIndex, itemHandler, fluidHandler, invertFluids, isOutputs);
            }
        }
        if (fluidInputsCount > 0 || invertFluids) {
            if (itemSlotsToDown >= fluidInputsCount && itemSlotsToLeft < 3) {
                int startSpecX = isOutputs ? startInputsX + itemSlotsToLeft * 18 : startInputsX - 18;
                for (int i = 0; i < fluidInputsCount; i++) {
                    int y = startInputsY + 18 * i;
                    addSlot(builder, startSpecX, y, i, itemHandler, fluidHandler, !invertFluids, isOutputs);
                }
            } else {
                int startSpecY = startInputsY + itemSlotsToDown * 18;
                for (int i = 0; i < fluidInputsCount; i++) {
                    int x = isOutputs ? startInputsX + 18 * (i % 3) : startInputsX + itemSlotsToLeft * 18 - 18 - 18 * (i % 3);
                    int y = startSpecY + (i / 3) * 18;
                    addSlot(builder, x, y, i, itemHandler, fluidHandler, !invertFluids, isOutputs);
                }
            }
        }
    }

    protected void addSlot(ModularUserInterface.Builder builder, int x, int y, int slotIndex, IItemHandlerModifiable itemHandler, FluidTankList fluidHandler, boolean isFluid, boolean isOutputs) {
        if (!isFluid) {
            builder.widget(new SlotWidget(itemHandler, slotIndex, x, y, true, !isOutputs)
                    .setBackgroundTexture(getOverlaysForSlot(isOutputs, false, slotIndex == itemHandler.getSlots() - 1)));
        } else {
            builder.widget(new TankWidget(fluidHandler.getTankAt(slotIndex), x, y, 18, 18)
                    .setAlwaysShowFull(true)
                    .setBackgroundTexture(getOverlaysForSlot(isOutputs, true, slotIndex == fluidHandler.getTanks() - 1))
                    .setContainerClicking(true, !isOutputs));
        }
    }

    protected TextureArea[] getOverlaysForSlot(boolean isOutput, boolean isFluid, boolean isLast) {
        TextureArea base = isFluid ? GuiTextures.FLUID_SLOT : GuiTextures.SLOT;
        /*
        if (!isOutput && !isFluid && isLast && recipeBuilderSample instanceof IntCircuitRecipeBuilder) {
            //automatically add int circuit overlay to last item input slot
            return new TextureArea[]{base, GuiTextures.INT_CIRCUIT_OVERLAY};
        }
         */
        byte overlayKey = (byte) ((isOutput ? 2 : 0) + (isFluid ? 1 : 0) + (isLast ? 4 : 0));
        if (slotOverlays.containsKey(overlayKey)) {
            return new TextureArea[]{base, slotOverlays.get(overlayKey)};
        }
        return new TextureArea[]{base};
    }

    protected static int[] determineSlotsGrid(int itemInputsCount) {
        int itemSlotsToLeft = 0;
        int itemSlotsToDown = 0;
        double sqrt = Math.sqrt(itemInputsCount);
        if (sqrt % 1 == 0) { //check if square root is integer
            //case for 1, 4, 9 slots - it's square inputs (the most common case)
            itemSlotsToLeft = itemSlotsToDown = (int) sqrt;
        } else if (itemInputsCount % 3 == 0) {
            //case for 3 and 6 slots - 3 by horizontal and i / 3 by vertical (common case too)
            itemSlotsToDown = itemInputsCount / 3;
            itemSlotsToLeft = 3;
        } else if (itemInputsCount % 2 == 0) {
            //case for 2 inputs - 2 by horizontal and i / 3 by vertical (for 2 slots)
            itemSlotsToDown = itemInputsCount / 2;
            itemSlotsToLeft = 2;
        }
        return new int[]{itemSlotsToLeft, itemSlotsToDown};
    }

    public Collection<Recipe> getRecipeList() {
        return Collections.unmodifiableCollection(recipeList);
    }

    @ZenCodeType.Method("findRecipe")
    @Nullable
    public CTRecipe ctFindRecipe(long energy, IItemStack[] itemInputs, IFluidStack[] fluidInputs, @ZenCodeType.OptionalInt(Integer.MAX_VALUE) int outputFluidTankCapacity) {
        List<ItemStack> mcItemInputs = itemInputs == null ? Collections.emptyList() :
                Arrays.stream(itemInputs)
                        .map(iItemStack -> iItemStack.getInternal())
                        .collect(Collectors.toList());
        List<FluidStack> mcFluidInputs = fluidInputs == null ? Collections.emptyList() :
                Arrays.stream(fluidInputs)
                        .map(fluidInput -> fluidInput.getInternal())
                        .collect(Collectors.toList());
        Recipe backingRecipe = findRecipe(energy, mcItemInputs, mcFluidInputs, outputFluidTankCapacity);
        return backingRecipe == null ? null : new CTRecipe(this, backingRecipe);
    }

    @ZenCodeType.Getter("recipes")
    public List<CTRecipe> ccGetRecipeList() {
        return getRecipeList().stream()
                .map(recipe -> new CTRecipe(this, recipe))
                .collect(Collectors.toList());
    }

    @ZenCodeType.Getter("localizedName")
    public String getLocalizedName() {
        return I18n.format("recipemap." + unlocalizedName + ".name");
    }

    @ZenCodeType.Getter("unlocalizedName")
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public R recipeBuilder() {
        return recipeBuilderSample.copy();
    }

    @ZenCodeType.Getter("minInputs")
    public int getMinInputs() {
        return minInputs;
    }

    @ZenCodeType.Getter("maxInputs")
    public int getMaxInputs() {
        return maxInputs;
    }

    @ZenCodeType.Getter("minOutputs")
    public int getMinOutputs() {
        return minOutputs;
    }

    @ZenCodeType.Getter("maxOutputs")
    public int getMaxOutputs() {
        return maxOutputs;
    }

    @ZenCodeType.Getter("minFluidInputs")
    public int getMinFluidInputs() {
        return minFluidInputs;
    }

    @ZenCodeType.Getter("maxFluidInputs")
    public int getMaxFluidInputs() {
        return maxFluidInputs;
    }

    @ZenCodeType.Getter("minFluidOutputs")
    public int getMinFluidOutputs() {
        return minFluidOutputs;
    }

    @ZenCodeType.Getter("maxFluidOutputs")
    public int getMaxFluidOutputs() {
        return maxFluidOutputs;
    }

    @Override
    @ZenCodeType.Method
    public String toString() {
        return "RecipeMap{" +
                "unlocalizedName='" + unlocalizedName + '\'' + '}';
    }

    @FunctionalInterface
    @ZenCodeType.Name("mods.autotech.recipe.IChanceFunction")
    @ZenRegister
    public interface IChanceFunction {
        int chanceFor(int chance, int boostPerTier, Tier boostTier);
    }
}
