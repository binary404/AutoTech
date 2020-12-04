package binary404.autotech.common.core.logistics;

import binary404.autotech.common.core.lib.multiblock.MultiBlockPart;
import binary404.autotech.common.tile.core.TileCore;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ItemHandlerProxy extends Inventory {

    private IItemHandler insertHandler;
    boolean allowInput, allowOutput;

    public ItemHandlerProxy(IItemHandler insertHandler, TileCore tile, boolean allowInput, boolean allowOutput) {
        super(insertHandler.getSlots(), tile);
        this.insertHandler = insertHandler;
        this.allowInput = allowInput;
        this.allowOutput = allowOutput;
    }

    @Override
    public int getSlots() {
        return insertHandler.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return insertHandler.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (this.getTile().canExtract(slot) && allowOutput)
            return insertHandler.extractItem(slot, amount, simulate);
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (this.getTile().canInsert(slot) && allowInput)
            return insertHandler.insertItem(slot, stack, simulate);
        return stack;
    }

    @Override
    public int getSlotLimit(int slot) {
        return insertHandler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return insertHandler.isItemValid(slot, stack);
    }
}
