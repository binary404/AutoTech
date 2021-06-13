package binary404.autotech.common.core.recipe.machine;

import binary404.autotech.common.core.logistics.energy.Energy;
import binary404.autotech.common.core.logistics.fluid.IMultipleTankHandler;
import binary404.autotech.common.core.recipe.core.AbstractRecipeLogic;
import binary404.autotech.common.core.recipe.core.Recipe;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import binary404.autotech.common.core.util.Util;
import binary404.autotech.common.tile.core.TileCore;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class SmeltingRecipeLogic extends AbstractRecipeLogic {

    private final Supplier<Energy> energyStorage;

    public SmeltingRecipeLogic(TileCore tileCore, RecipeMap<?> recipeMap, Supplier<Energy> energyStorage) {
        super(tileCore, recipeMap);
        this.energyStorage = energyStorage;
    }

    @Override
    protected Recipe findRecipe(long maxEnergy, IItemHandlerModifiable inputs, IMultipleTankHandler fluidInputs) {
        RecipeManager mgr = tileCore.getWorld().getRecipeManager();
        List<ItemStack> inputList = Util.itemHandlerToList(inputs);
        for (ItemStack stack : inputList) {
            IInventory inventory = new Inventory(stack);
            Optional<IRecipe<IInventory>> optRecipe = (Optional<IRecipe<IInventory>>) ObjectUtils.firstNonNull(
                    mgr.getRecipe(IRecipeType.SMELTING, inventory, tileCore.getWorld()),
                    mgr.getRecipe(IRecipeType.CAMPFIRE_COOKING, inventory, tileCore.getWorld()),
                    mgr.getRecipe(IRecipeType.SMOKING, inventory, tileCore.getWorld()),
                    Optional.empty()
            );
            if (optRecipe.isPresent()) {
                ItemStack output = optRecipe.get().getRecipeOutput();
                return this.recipeMap.recipeBuilder().inputs(new ItemStack(stack.getItem(), 1))
                        .outputs(output)
                        .duration(128).energyPerTick(4)
                        .build().getResult();
            }
        }
        return null;
    }

    @Override
    protected long getEnergyStored() {
        return energyStorage.get().getEnergyStored();
    }

    @Override
    protected long getEnergyCapacity() {
        return energyStorage.get().getMaxEnergyStored();
    }

    @Override
    protected boolean drawEnergy(int recipeEnergyPerTick) {
        long resultEnergy = getEnergyStored() - recipeEnergyPerTick;
        if (resultEnergy >= 0L && resultEnergy <= getEnergyCapacity()) {
            energyStorage.get().extractEnergy(recipeEnergyPerTick, false);
            return true;
        } else
            return false;
    }

    @Override
    protected long getMaxEnergyPerTick() {
        return energyStorage.get().getMaxExtract();
    }

}
