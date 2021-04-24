package binary404.autotech.common.core.lib.multiblock;

import binary404.autotech.client.renders.core.OrientedOverlayRenderer;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.core.logistics.energy.Energy;
import binary404.autotech.common.core.logistics.energy.EnergyList;
import binary404.autotech.common.core.logistics.fluid.FluidTankList;
import binary404.autotech.common.core.logistics.fluid.IMultipleTankHandler;
import binary404.autotech.common.core.logistics.item.ItemHandlerList;
import binary404.autotech.common.core.recipe.core.Recipe;
import binary404.autotech.common.core.recipe.machine.MultiblockRecipeLogic;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import com.google.common.collect.Lists;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class MultiblockControllerRecipe extends MultiblockControllerDisplay {

    public RecipeMap<?> recipeMap;
    protected MultiblockRecipeLogic recipeMapWorkable;

    protected IItemHandlerModifiable inputInventory;
    protected IItemHandlerModifiable outputInventory;
    protected IMultipleTankHandler inputFluidInventory;
    protected IMultipleTankHandler outputFluidInventory;
    protected IEnergyStorage energy;

    public MultiblockControllerRecipe(TileEntityType<?> tileEntityType, RecipeMap<?> recipeMap) {
        super(tileEntityType);
        this.recipeMap = recipeMap;
        this.recipeMapWorkable = new MultiblockRecipeLogic(this);
        resetTileAbilities();
    }

    public IEnergyStorage getEnergy() {
        return energy;
    }

    public IItemHandlerModifiable getInputInventory() {
        return inputInventory;
    }

    public IItemHandlerModifiable getOutputInventory() {
        return outputInventory;
    }

    public IMultipleTankHandler getInputFluidInventory() {
        return inputFluidInventory;
    }

    public IMultipleTankHandler getOutputFluidInventory() {
        return outputFluidInventory;
    }

    public boolean checkRecipe(Recipe recipe, boolean consumeIfSuccess) {
        return true;
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        initializeAbilities();
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        resetTileAbilities();
    }

    @Override
    protected void updateFormedValid() {
        this.recipeMapWorkable.updateWorkable();
    }

    private void initializeAbilities() {
        this.inputInventory = new ItemHandlerList(getAbilities(MultiblockAbility.IMPORT_ITEMS));
        this.inputFluidInventory = new FluidTankList(allowSameFluidFillForOutputs(), getAbilities(MultiblockAbility.IMPORT_FLUIDS));
        this.outputInventory = new ItemHandlerList(getAbilities(MultiblockAbility.EXPORT_ITEMS));
        this.outputFluidInventory = new FluidTankList(allowSameFluidFillForOutputs(), getAbilities(MultiblockAbility.EXPORT_FLUIDS));
        this.energy = new EnergyList(getAbilities(MultiblockAbility.INPUT_ENERGY));
    }

    private void resetTileAbilities() {
        this.inputInventory = new ItemStackHandler(0);
        this.inputFluidInventory = new FluidTankList(true);
        this.outputInventory = new ItemStackHandler(0);
        this.outputFluidInventory = new FluidTankList(true);
        this.energy = new EnergyList(Lists.newArrayList());
    }

    protected boolean allowSameFluidFillForOutputs() {
        return true;
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        if (isStructureFormed()) {
            if (recipeMapWorkable.isActive()) {
                textList.add(new TranslationTextComponent("autotech.multiblock.running"));
                int currentProgress = (int) (recipeMapWorkable.getProgressPercent() * 100);
                textList.add(new TranslationTextComponent("autotech.multiblock.progress", currentProgress));
            } else {
                textList.add(new TranslationTextComponent("autotech.multiblock.idling"));
            }

            if (recipeMapWorkable.isHasNotEnoughEnergy()) {
                textList.add(new TranslationTextComponent("autotech.multiblock.not_enough_energy").mergeStyle(Style.EMPTY.setFormatting(TextFormatting.RED)));
            }
        }
    }

    @Override
    protected boolean checkStructureComponents(List<IMultiblockPart> parts, Map<MultiblockAbility<Object>, List<Object>> abilities) {
        int itemInputsCount = abilities.getOrDefault(MultiblockAbility.IMPORT_ITEMS, Collections.emptyList())
                .stream().map(it -> (IItemHandler) it).mapToInt(IItemHandler::getSlots).sum();

        int fluidInputsCount = abilities.getOrDefault(MultiblockAbility.IMPORT_FLUIDS, Collections.emptyList()).size();
        return itemInputsCount >= recipeMap.getMinInputs() && fluidInputsCount >= recipeMap.getMinFluidInputs() && abilities.containsKey(MultiblockAbility.INPUT_ENERGY);
    }

    @Override
    public void renderTileEntity(CCRenderState renderState, IVertexOperation... pipeline) {
        super.renderTileEntity(renderState, pipeline);
        this.getFrontOverlay().render(renderState, this.facing, recipeMapWorkable != null && recipeMapWorkable.isActive(), pipeline);
    }

    protected OrientedOverlayRenderer getFrontOverlay() {
        return Textures.MULTIBLOCK_WORKABLE_OVERLAY;
    }
}
