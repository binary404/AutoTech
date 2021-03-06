package binary404.autotech.common.core.logistics.item;

import binary404.autotech.common.core.logistics.item.Inventory;
import binary404.autotech.common.tile.core.TileCore;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ItemHandlerProxy implements IItemHandler {

    private IItemHandler insertHandler;
    private IItemHandler extractHandler;

    public ItemHandlerProxy(IItemHandler insertHandler, IItemHandler extractHandler) {
        this.insertHandler = insertHandler;
        this.extractHandler = extractHandler;
    }

    @Override
    public int getSlots() {
        return insertHandler.getSlots() + extractHandler.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot < insertHandler.getSlots() ? insertHandler.getStackInSlot(slot) : extractHandler.getStackInSlot(slot - insertHandler.getSlots());
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return slot < insertHandler.getSlots() ? insertHandler.insertItem(slot, stack, simulate) : stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return slot >= insertHandler.getSlots() ? extractHandler.extractItem(slot - insertHandler.getSlots(), amount, simulate) : ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return slot < insertHandler.getSlots() ? insertHandler.getSlotLimit(slot) : extractHandler.getSlotLimit(slot - insertHandler.getSlots());
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return slot < insertHandler.getSlots() ? insertHandler.isItemValid(slot, stack) : extractHandler.isItemValid(slot - insertHandler.getSlots(), stack);
    }
}
