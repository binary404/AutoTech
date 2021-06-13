package binary404.autotech.common.core.recipe.core;

import net.minecraft.item.ItemStack;

public class ItemFuelInfo extends AbstractFuelInfo {

    private final ItemStack itemStack;

    public ItemFuelInfo(ItemStack itemStack, int fuelRemaining, int fuelCapacity, int fuelMinConsumed, int fuelBurnTime) {
        super(fuelRemaining, fuelCapacity, fuelMinConsumed, fuelBurnTime);
        this.itemStack = itemStack;
    }

    @Override
    public String getFuelName() {
        return itemStack.getTranslationKey();
    }
}
