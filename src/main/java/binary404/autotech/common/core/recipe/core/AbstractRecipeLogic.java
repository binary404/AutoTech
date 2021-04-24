package binary404.autotech.common.core.recipe.core;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.logistics.fluid.IMultipleTankHandler;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import binary404.autotech.common.core.util.FluidHelper;
import binary404.autotech.common.core.util.Util;
import binary404.autotech.common.tile.core.TileCore;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.LongSupplier;

public abstract class AbstractRecipeLogic extends TETrait implements IWorkable {

    private static final String ALLOW_OVERCLOCKING = "AllowOverclocking";
    private static final String OVERCLOCK_VOLTAGE = "OverclockVoltage";

    public final RecipeMap<?> recipeMap;

    protected boolean forceRecipeRecheck;
    protected ItemStack[] lastItemInputs;
    protected FluidStack[] lastFluidInputs;
    protected Recipe previousRecipe;

    protected int progressTime;
    protected int maxProgressTime;
    protected int recipeEnergyPerTick;
    protected List<FluidStack> fluidOutputs;
    protected NonNullList<ItemStack> itemOutputs;
    protected final Random random = new Random();
    protected boolean allowOverclocking = true;
    private long overclockVoltage = 0;
    private LongSupplier overclockPolicy = this::getMaxEnergyPerTick;

    protected boolean isActive;
    protected boolean workingEnabled = true;
    protected boolean hasNotEnoughEnergy;
    protected boolean wasActiveAndNeedsUpdate;

    public AbstractRecipeLogic(TileCore tileCore, RecipeMap<?> recipeMap) {
        super(tileCore);
        this.recipeMap = recipeMap;
    }

    protected abstract long getEnergyStored();

    protected abstract long getEnergyCapacity();

    protected abstract boolean drawEnergy(int recipeEnergyPerTick);

    protected abstract long getMaxEnergyPerTick();

    protected IItemHandlerModifiable getInputInventory() {
        return tileCore.getImportItems();
    }

    protected IItemHandlerModifiable getOutputInventory() {
        return tileCore.getExportItems();
    }

    protected IMultipleTankHandler getInputTank() {
        return tileCore.getImportFluids();
    }

    protected IMultipleTankHandler getOutputTank() {
        return tileCore.getExportFluids();
    }

    @Override
    public String getName() {
        return "RecipeMapWorkable";
    }

    @Override
    public int getNetworkID() {
        return TraitNetworkIds.TRAIT_ID_WORKABLE;
    }

    @Override
    public void update() {
        if (!getTileCore().getWorld().isRemote) {
            if (workingEnabled) {
                if (progressTime > 0) {
                    updateRecipeProgress();
                }
                if (progressTime == 0) {
                    trySearchNewRecipe();
                }
            }
            if (wasActiveAndNeedsUpdate) {
                this.wasActiveAndNeedsUpdate = false;
                setActive(false);
            }
        }
    }

    protected void updateRecipeProgress() {
        boolean drawEnergy = drawEnergy(recipeEnergyPerTick);
        if (drawEnergy || (recipeEnergyPerTick < 0)) {

            if (++progressTime > maxProgressTime) {
                completeRecipe();
            }
        } else if (recipeEnergyPerTick > 0) {
            this.hasNotEnoughEnergy = true;

            if (progressTime >= 2) {
                this.progressTime = Math.max(1, progressTime - 2);
            }
        }
    }

    protected void trySearchNewRecipe() {
        long maxEnergyPerTick = getMaxEnergyPerTick();
        Recipe currentRecipe = null;
        IItemHandlerModifiable importInventory = getInputInventory();
        IMultipleTankHandler importFluids = getInputTank();
        if (previousRecipe != null && previousRecipe.matches(false, importInventory, importFluids)) {
            currentRecipe = previousRecipe;
        } else {
            boolean dirty = checkRecipeInputsDirty(importInventory, importFluids);
            if (dirty || forceRecipeRecheck) {
                this.forceRecipeRecheck = false;
                currentRecipe = findRecipe(maxEnergyPerTick, importInventory, importFluids);
                if (currentRecipe != null) {
                    this.previousRecipe = currentRecipe;
                }
            }
        }
        if (currentRecipe != null && setupAndConsumeRecipeInputs(currentRecipe)) {
            setupRecipe(currentRecipe);
        }
    }

    public void forceRecipeRecheck() {
        this.forceRecipeRecheck = true;
    }

    protected int getMinTankCapacity(IMultipleTankHandler tanks) {
        if (tanks.getTanks() == 0) {
            return 0;
        }
        int result = Integer.MAX_VALUE;
        for (IFluidTank fluidTank : tanks.getFluidTanks()) {
            result = Math.min(fluidTank.getCapacity(), result);
        }
        return result;
    }

    protected Recipe findRecipe(long maxEnergy, IItemHandlerModifiable inputs, IMultipleTankHandler fluidInputs) {
        return recipeMap.findRecipe(maxEnergy, inputs, fluidInputs, getMinTankCapacity(getOutputTank()));
    }

    protected boolean checkRecipeInputsDirty(IItemHandler inputs, IMultipleTankHandler fluidInputs) {
        boolean shouldRecheckRecipe = false;
        if (lastItemInputs == null || lastItemInputs.length != inputs.getSlots()) {
            this.lastItemInputs = new ItemStack[inputs.getSlots()];
            Arrays.fill(lastItemInputs, ItemStack.EMPTY);
        }
        if (lastFluidInputs == null || lastFluidInputs.length != fluidInputs.getTanks()) {
            this.lastFluidInputs = new FluidStack[fluidInputs.getTanks()];
        }
        for (int i = 0; i < lastItemInputs.length; i++) {
            ItemStack currentStack = inputs.getStackInSlot(i);
            ItemStack lastStack = lastItemInputs[i];
            if (!areItemStacksEqual(currentStack, lastStack)) {
                this.lastItemInputs[i] = currentStack.isEmpty() ? ItemStack.EMPTY : currentStack.copy();
                shouldRecheckRecipe = true;
            } else if (currentStack.getCount() != lastStack.getCount()) {
                lastStack.setCount(currentStack.getCount());
                shouldRecheckRecipe = true;
            }
        }
        for (int i = 0; i < lastFluidInputs.length; i++) {
            FluidStack currentStack = fluidInputs.getTankAt(i).getFluid();
            FluidStack lastStack = lastFluidInputs[i];
            if ((currentStack == null && lastStack != null) ||
                    (currentStack != null && !FluidHelper.isFluidEqual(currentStack, lastStack))) {
                this.lastFluidInputs[i] = currentStack == null ? null : currentStack.copy();
                shouldRecheckRecipe = true;
            } else if (currentStack != null && lastStack != null &&
                    currentStack.getAmount() != lastStack.getAmount()) {
                lastStack.setAmount(currentStack.getAmount());
                shouldRecheckRecipe = true;
            }
        }
        return shouldRecheckRecipe;
    }

    protected static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
        return (stackA.isEmpty() && stackB.isEmpty()) ||
                (ItemStack.areItemsEqual(stackA, stackB) &&
                        ItemStack.areItemStackTagsEqual(stackA, stackB));
    }

    protected boolean setupAndConsumeRecipeInputs(Recipe recipe) {
        int[] resultOverclock = calculateOverclock(recipe.getEnergyPerTick(), recipe.getDuration());
        int totalEnergy = resultOverclock[0] * resultOverclock[1];
        IItemHandlerModifiable importInventory = getInputInventory();
        IItemHandlerModifiable exportInventory = getOutputInventory();
        IMultipleTankHandler importFluids = getInputTank();
        IMultipleTankHandler exportFluids = getOutputTank();

        return (totalEnergy >= 0 ? getEnergyStored() >= (totalEnergy > getEnergyCapacity() / 2 ? resultOverclock[0] : totalEnergy) :
                (getEnergyStored() - resultOverclock[0] <= getEnergyCapacity())) &&
                TileCore.addItemsToItemHandler(exportInventory, true, recipe.getAllItemOutputs(exportInventory.getSlots())) &&
                TileCore.addFluidsToFluidHandler(exportFluids, IFluidHandler.FluidAction.SIMULATE, recipe.getFluidOutputs()) &&
                recipe.matches(true, importInventory, importFluids);
    }

    protected int[] calculateOverclock(int EUt, int duration) {
        return calculateOverclock(EUt, this.overclockPolicy.getAsLong(), duration);
    }

    protected int[] calculateOverclock(int EUt, long voltage, int duration) {
        if (!allowOverclocking) {
            return new int[]{EUt, duration};
        }
        boolean negativeEU = EUt < 0;
        int tier = getOverclockingTier(voltage);
        if (Tier.values()[tier].use <= EUt || tier == 0)
            return new int[]{EUt, duration};
        if (negativeEU)
            EUt = -EUt;
        if (EUt <= 16) {
            int multiplier = EUt <= 8 ? tier : tier - 1;
            int resultEUt = EUt * (1 << multiplier) * (1 << multiplier);
            int resultDuration = duration / (1 << multiplier);
            return new int[]{negativeEU ? -resultEUt : resultEUt, resultDuration};
        } else {
            int resultEUt = EUt;
            double resultDuration = duration;
            //do not overclock further if duration is already too small
            while (resultDuration >= 3 && resultEUt <= Tier.values()[tier - 1].use) {
                resultEUt *= 4;
                resultDuration /= 2.8;
            }
            return new int[]{negativeEU ? -resultEUt : resultEUt, (int) Math.ceil(resultDuration)};
        }
    }

    protected int getOverclockingTier(long voltage) {
        return Tier.getTierByVoltage(voltage).ordinal();
    }

    protected void setupRecipe(Recipe recipe) {
        int[] resultOverclock = calculateOverclock(recipe.getEnergyPerTick(), recipe.getDuration());
        this.progressTime = 1;
        setMaxProgress(resultOverclock[1]);
        this.recipeEnergyPerTick = resultOverclock[0];
        this.fluidOutputs = FluidHelper.copyFluidList(recipe.getFluidOutputs());
        Tier tier = getMachineTierForRecipe(recipe);
        this.itemOutputs = Util.copyStackList(recipe.getResultItemOutputs(getOutputInventory().getSlots(), random, tier));
        if (this.wasActiveAndNeedsUpdate) {
            this.wasActiveAndNeedsUpdate = false;
        } else {
            this.setActive(true);
        }
    }

    protected Tier getMachineTierForRecipe(Recipe recipe) {
        return Tier.getTierByVoltage(getMaxEnergyPerTick());
    }

    protected void completeRecipe() {
        TileCore.addItemsToItemHandler(getOutputInventory(), false, itemOutputs);
        TileCore.addFluidsToFluidHandler(getOutputTank(), IFluidHandler.FluidAction.EXECUTE, fluidOutputs);
        this.progressTime = 0;
        setMaxProgress(0);
        this.recipeEnergyPerTick = 0;
        this.fluidOutputs = null;
        this.itemOutputs = null;
        this.hasNotEnoughEnergy = false;
        this.wasActiveAndNeedsUpdate = true;
        this.forceRecipeRecheck = true;
    }

    public double getProgressPercent() {
        return getMaxProgress() == 0 ? 0.0 : getProgress() / (getMaxProgress() * 1.0);
    }

    public int getTicksTimeLeft() {
        return maxProgressTime == 0 ? 0 : (maxProgressTime - progressTime);
    }

    @Override
    public int getProgress() {
        return progressTime;
    }

    @Override
    public int getMaxProgress() {
        return maxProgressTime;
    }

    public int getRecipeEnergyPerTick() {
        return recipeEnergyPerTick;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgressTime = maxProgress;
        tileCore.markDirty();
    }

    protected void setActive(boolean active) {
        this.isActive = active;
        tileCore.markDirty();
    }

    public boolean isHasNotEnoughEnergy() {
        return hasNotEnoughEnergy;
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putBoolean("WorkEnabled", workingEnabled);
        if (progressTime > 0) {
            compound.putInt("Progress", progressTime);
            compound.putInt("MaxProgress", maxProgressTime);
            compound.putInt("RecipeEnergyPerTick", this.recipeEnergyPerTick);
            ListNBT itemOutputsList = new ListNBT();
            for (ItemStack itemOutput : itemOutputs) {
                itemOutputsList.add(itemOutput.write(new CompoundNBT()));
            }
            ListNBT fluidOutputsList = new ListNBT();
            for (FluidStack fluidOutput : fluidOutputs) {
                fluidOutputsList.add(fluidOutput.writeToNBT(new CompoundNBT()));
            }
            compound.put("ItemOutputs", itemOutputsList);
            compound.put("FluidOutputs", fluidOutputsList);
        }
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT compound) {
        this.workingEnabled = compound.getBoolean("WorkEnabled");
        this.progressTime = compound.getInt("Progress");
        this.isActive = false;
        if (progressTime > 0) {
            this.isActive = true;
            this.maxProgressTime = compound.getInt("MaxProgress");
            this.recipeEnergyPerTick = compound.getInt("RecipeEnergyPerTick");
            ListNBT itemOutputsList = compound.getList("ItemOutputs", Constants.NBT.TAG_COMPOUND);
            this.itemOutputs = NonNullList.create();
            for (int i = 0; i < itemOutputsList.size(); i++) {
                this.itemOutputs.add(ItemStack.read(itemOutputsList.getCompound(i)));
            }
            ListNBT fluidOutputsList = compound.getList("FluidOutputs", Constants.NBT.TAG_COMPOUND);
            this.fluidOutputs = new ArrayList<>();
            for (int i = 0; i < fluidOutputsList.size(); i++) {
                this.fluidOutputs.add(FluidStack.loadFluidStackFromNBT(fluidOutputsList.getCompound(i)));
            }
        }
    }
}
