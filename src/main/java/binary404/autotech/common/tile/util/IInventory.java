package binary404.autotech.common.tile.util;

import binary404.autotech.common.core.logistics.item.Inventory;
import net.minecraft.item.ItemStack;

public interface IInventory {

    int getSlotLimit(int slot);

    boolean canInsert(int slot, ItemStack stack);

    boolean canExtract(int slot, ItemStack stack);

    default void onSlotChanged(int slot) {}

    Inventory getInventory();

}
