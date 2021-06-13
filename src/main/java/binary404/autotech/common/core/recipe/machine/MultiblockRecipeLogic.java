package binary404.autotech.common.core.recipe.machine;

import binary404.autotech.common.core.lib.multiblock.MultiblockControllerRecipe;
import binary404.autotech.common.core.logistics.energy.Energy;
import binary404.autotech.common.core.logistics.energy.EnergyList;
import binary404.autotech.common.core.logistics.fluid.IMultipleTankHandler;
import binary404.autotech.common.core.recipe.core.AbstractRecipeLogic;
import binary404.autotech.common.core.recipe.core.Recipe;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandlerModifiable;

public class MultiblockRecipeLogic extends AbstractRecipeLogic {

    public MultiblockRecipeLogic(MultiblockControllerRecipe tileEntity) {
        super(tileEntity, tileEntity.recipeMap);
    }

    @Override
    public void update() {

    }

    public void updateWorkable() {
        super.update();
    }

    public IEnergyStorage getEnergy() {
        MultiblockControllerRecipe controller = (MultiblockControllerRecipe) tileCore;
        return controller.getEnergy();
    }

    @Override
    protected IItemHandlerModifiable getInputInventory() {
        MultiblockControllerRecipe controller = (MultiblockControllerRecipe) tileCore;
        return controller.getInputInventory();
    }

    @Override
    protected IItemHandlerModifiable getOutputInventory() {
        MultiblockControllerRecipe controller = (MultiblockControllerRecipe) tileCore;
        return controller.getOutputInventory();
    }

    @Override
    protected IMultipleTankHandler getInputTank() {
        MultiblockControllerRecipe controller = (MultiblockControllerRecipe) tileCore;
        return controller.getInputFluidInventory();
    }

    @Override
    protected IMultipleTankHandler getOutputTank() {
        MultiblockControllerRecipe controller = (MultiblockControllerRecipe) tileCore;
        return controller.getOutputFluidInventory();
    }

    @Override
    protected boolean setupAndConsumeRecipeInputs(Recipe recipe) {
        MultiblockControllerRecipe controller = (MultiblockControllerRecipe) tileCore;
        if (controller.checkRecipe(recipe, false) && super.setupAndConsumeRecipeInputs(recipe)) {
            controller.checkRecipe(recipe, true);
            return true;
        } else
            return false;
    }

    @Override
    protected long getEnergyStored() {
        return getEnergy().getEnergyStored();
    }

    @Override
    protected long getEnergyCapacity() {
        return getEnergy().getMaxEnergyStored();
    }

    @Override
    protected boolean drawEnergy(int recipeEnergyPerTick) {
        long resultEnergy = getEnergyStored() - recipeEnergyPerTick;
        if (resultEnergy >= 0L && resultEnergy <= getEnergyCapacity()) {
            getEnergy().extractEnergy(recipeEnergyPerTick, false);
            return true;
        } else return false;
    }

    @Override
    protected long getMaxEnergyPerTick() {
        return getEnergy() instanceof EnergyList ? ((EnergyList) getEnergy()).getMaxExtract() : getEnergy().getMaxEnergyStored() / 10;
    }
}
