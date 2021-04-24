package binary404.autotech.common.core.recipe.machine;

import binary404.autotech.common.core.logistics.energy.Energy;
import binary404.autotech.common.core.recipe.core.AbstractRecipeLogic;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import binary404.autotech.common.tile.core.TileCore;

import java.util.function.Supplier;

public class RecipeLogicEnergy extends AbstractRecipeLogic {

    private final Supplier<Energy> energyStorage;

    public RecipeLogicEnergy(TileCore tileCore, RecipeMap<?> recipeMap, Supplier<Energy> energyStorage) {
        super(tileCore, recipeMap);
        this.energyStorage = energyStorage;
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
        } else return false;
    }

    @Override
    protected long getMaxEnergyPerTick() {
        return Math.max(energyStorage.get().getMaxReceive(), energyStorage.get().getMaxExtract());
    }
}
