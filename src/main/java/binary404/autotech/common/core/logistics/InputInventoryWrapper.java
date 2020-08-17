package binary404.autotech.common.core.logistics;

import binary404.autotech.common.tile.core.TileCore;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class InputInventoryWrapper implements IItemHandlerModifiable {

    protected final IItemHandlerModifiable itemHandler; // the handler
    protected final int slotCount; // number of total slots

    protected TileCore core;

    public InputInventoryWrapper(IItemHandlerModifiable itemHandler, TileCore core) {
        this.itemHandler = itemHandler;
        int index = itemHandler.getSlots();

        this.slotCount = index;

        this.core = core;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        this.itemHandler.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return slotCount;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (this.canInsertItem(slot))
            return this.itemHandler.insertItem(slot, stack, simulate);
        return stack;
    }

    public boolean canInsertItem(int slot) {
        return this.core.canInsert(slot);
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return itemHandler.isItemValid(slot, stack);
    }

}
