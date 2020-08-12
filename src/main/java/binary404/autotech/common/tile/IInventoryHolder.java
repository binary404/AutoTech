package binary404.autotech.common.tile;

import binary404.autotech.common.core.logistics.Inventory;
import net.minecraft.item.ItemStack;

public interface IInventoryHolder {
    int getSlotLimit(int slot);

    boolean canInsert(int slot, ItemStack stack);

    boolean canExtract(int slot, ItemStack stack);

    default void onSlotChanged(int slot) {
    }

    Inventory getInventory();
}
