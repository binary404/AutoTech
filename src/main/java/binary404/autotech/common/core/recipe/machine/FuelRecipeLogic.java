package binary404.autotech.common.core.recipe.machine;

import binary404.autotech.common.core.logistics.energy.Energy;
import binary404.autotech.common.core.logistics.fluid.IMultipleTankHandler;
import binary404.autotech.common.core.recipe.map.FuelRecipeMap;
import binary404.autotech.common.core.recipe.core.*;
import binary404.autotech.common.tile.core.TileCore;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.function.Supplier;

public class FuelRecipeLogic extends TETrait implements IFuelable {

    public final FuelRecipeMap recipeMap;
    protected FuelRecipe previousRecipe;

    protected final Supplier<Energy> energy;
    protected final Supplier<IMultipleTankHandler> fluidTank;
    public final long maxEnergy;

    private int recipeDurationLeft;
    private long recipeOutputEnergy;

    private boolean isActive;
    private boolean workingEnabled = true;
    private boolean wasActiveAndNeedsUpdate = false;

    public FuelRecipeLogic(TileCore tileCore, FuelRecipeMap recipeMap, Supplier<Energy> energy, Supplier<IMultipleTankHandler> fluidTank, long maxEnergy) {
        super(tileCore);
        this.recipeMap = recipeMap;
        this.energy = energy;
        this.fluidTank = fluidTank;
        this.maxEnergy = maxEnergy;
    }

    public long getRecipeOutputEnergy() {
        return recipeOutputEnergy;
    }

    @Override
    public String getName() {
        return "FuelRecipeMapWorkable";
    }

    @Override
    public int getNetworkID() {
        return TraitNetworkIds.TRAIT_ID_WORKABLE;
    }

    @Override
    public Collection<IFuelInfo> getFuels() {
        if (!isReadyForRecipes())
            return Collections.emptySet();
        final IMultipleTankHandler fluidTanks = this.fluidTank.get();
        if (fluidTanks == null)
            return Collections.emptySet();

        final LinkedHashMap<String, IFuelInfo> fuels = new LinkedHashMap<>();
        // Fuel capacity is all tanks
        int fuelCapacity = 0;
        for (IFluidTank fluidTank : fluidTanks) {
            fuelCapacity += fluidTank.getCapacity();
        }

        for (IFluidTank fluidTank : fluidTanks) {
            final FluidStack tankContents = fluidTank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
            if (tankContents == null || tankContents.getAmount() <= 0)
                continue;
            int fuelRemaining = tankContents.getAmount();
            FuelRecipe recipe = findRecipe(tankContents);
            if (recipe == null)
                continue;
            int amountPerRecipe = calculateFuelAmount(recipe);
            int duration = calculateRecipeDuration(recipe);
            int fuelBurnTime = duration * fuelRemaining / amountPerRecipe;

            FluidFuelInfo fuelInfo = (FluidFuelInfo) fuels.get(tankContents.getTranslationKey());
            if (fuelInfo == null) {
                fuelInfo = new FluidFuelInfo(tankContents, fuelRemaining, fuelCapacity, amountPerRecipe, fuelBurnTime);
                fuels.put(tankContents.getTranslationKey(), fuelInfo);
            } else {
                fuelInfo.addFuelRemaining(fuelRemaining);
                fuelInfo.addFuelBurnTime(fuelBurnTime);
            }
        }
        return fuels.values();
    }

    @Override
    public void update() {
        if (getTileCore().getWorld().isRemote)
            return;
        if (workingEnabled) {
            if (recipeDurationLeft > 0) {
                long energySpace = energy.get().getCapacity() - energy.get().getEnergyStored();
                if (energySpace >= recipeOutputEnergy || shouldVoidExcessiveEnergy()) {
                    energy.get().produce(recipeOutputEnergy);
                    if (--this.recipeDurationLeft == 0) {
                        this.wasActiveAndNeedsUpdate = true;
                    }
                }
            }
            if (recipeDurationLeft == 0 && isReadyForRecipes()) {
                tryAcquireNewRecipe();
            }
        }
        if (wasActiveAndNeedsUpdate) {
            setActive(false);
            this.wasActiveAndNeedsUpdate = false;
        }
    }

    protected boolean isReadyForRecipes() {
        return true;
    }

    protected boolean shouldVoidExcessiveEnergy() {
        return false;
    }

    private void tryAcquireNewRecipe() {
        IMultipleTankHandler fluidTanks = this.fluidTank.get();
        for (IFluidTank fluidTank : fluidTanks) {
            FluidStack tankContents = fluidTank.getFluid();
            if (tankContents != null && !tankContents.isEmpty() && tankContents.getAmount() > 0) {
                int fuelAmountUsed = tryAcquireNewRecipe(tankContents);
                if (fuelAmountUsed > 0) {
                    fluidTank.drain(fuelAmountUsed, IFluidHandler.FluidAction.EXECUTE);
                    break;
                }
            }
        }
    }

    public boolean isActive() {
        return isActive;
    }

    private int tryAcquireNewRecipe(FluidStack fluidStack) {
        FuelRecipe currentRecipe;
        if (previousRecipe != null && previousRecipe.matches(getMaxEnergy(), fluidStack)) {
            //if previous recipe still matches inputs, try to use it
            currentRecipe = previousRecipe;
        } else {
            //else, try searching new recipe for given inputs
            currentRecipe = recipeMap.findRecipe(getMaxEnergy(), fluidStack);
            //if we found recipe that can be buffered, buffer it
            if (currentRecipe != null) {
                this.previousRecipe = currentRecipe;
            }
        }
        if (currentRecipe != null && checkRecipe(currentRecipe)) {
            int fuelAmountToUse = calculateFuelAmount(currentRecipe);
            if (fluidStack.getAmount() >= fuelAmountToUse) {
                this.recipeDurationLeft = calculateRecipeDuration(currentRecipe);
                this.recipeOutputEnergy = startRecipe(currentRecipe, fuelAmountToUse, recipeDurationLeft);
                if (wasActiveAndNeedsUpdate) {
                    this.wasActiveAndNeedsUpdate = false;
                } else {
                    setActive(true);
                }
                return fuelAmountToUse;
            }
        }
        return 0;
    }

    private FuelRecipe findRecipe(FluidStack fluidStack) {
        FuelRecipe currentRecipe;
        if (previousRecipe != null && previousRecipe.matches(getMaxEnergy(), fluidStack)) {
            currentRecipe = previousRecipe;
        } else {
            currentRecipe = recipeMap.findRecipe(getMaxEnergy(), fluidStack);
        }
        if (currentRecipe != null && checkRecipe(currentRecipe))
            return currentRecipe;
        return null;
    }

    protected boolean checkRecipe(FuelRecipe recipe) {
        return true;
    }

    public long getMaxEnergy() {
        return maxEnergy;
    }

    protected int calculateFuelAmount(FuelRecipe currentRecipe) {
        return currentRecipe.getRecipeFluid().getAmount() * getEnergyMultiplier(getMaxEnergy(), currentRecipe.getMinVoltage());
    }

    protected int calculateRecipeDuration(FuelRecipe currentRecipe) {
        return currentRecipe.getDuration();
    }

    protected long startRecipe(FuelRecipe currentRecipe, int fuelAmountUsed, int recipeDuration) {
        return getMaxEnergy();
    }

    public static int getEnergyMultiplier(long maxEnergy, long minEnergy) {
        return (int) (maxEnergy / minEnergy);
    }

    protected void setActive(boolean active) {
        if (this.isActive && !active) {
            recipeDurationLeft = 0;
        }
        this.isActive = active;
        if (!tileCore.getWorld().isRemote) {
            tileCore.markDirty();
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt("RecipeDurationLeft", this.recipeDurationLeft);
        if (recipeDurationLeft > 0) {
            compoundNBT.putLong("RecipeOutputEnergy", this.recipeOutputEnergy);
        }
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT compound) {
        this.recipeDurationLeft = compound.getInt("RecipeDurationLeft");
        if (recipeDurationLeft > 0) {
            this.recipeOutputEnergy = compound.getLong("RecipeOutputEnergy");
        }
        this.isActive = recipeDurationLeft > 0;
    }
}
