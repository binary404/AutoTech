package binary404.autotech.common.core.logistics.item;

import binary404.autotech.common.tile.core.TileCore;
import binary404.autotech.common.tile.util.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Inventory extends ItemStackHandler implements INBTSerializable<CompoundNBT> {

    @Nullable
    private TileCore tile;

    public Inventory(int size) {
        this(size, null);
    }

    Inventory(int size, @Nullable TileCore tile) {
        super(size);
        this.tile = tile;
    }

    public static Inventory create(int size, @Nullable TileCore tile) {
        return new Inventory(size, tile);
    }

    public static Inventory createBlank(@Nullable TileCore tile) {
        return new Inventory(0, tile);
    }

    public static Inventory create(int size) {
        return new Inventory(size, null);
    }

    public static Inventory createBlank() {
        return new Inventory(0, null);
    }

    public void setTile(@Nullable TileCore tile) {
        this.tile = tile;
    }

    public void deserializeNBT(CompoundNBT nbt) {
        if (isBlank()) return;
        nbt.putInt("Size", getSlots());
        super.deserializeNBT(nbt);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return isBlank() ? new CompoundNBT() : super.serializeNBT();
    }

    public Inventory set(int size) {
        this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
        onContentsChanged(0);
        return this;
    }

    public Inventory add(int size) {
        this.stacks = NonNullList.withSize(size + this.stacks.size(), ItemStack.EMPTY);
        return this;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return slot <= this.getSlots() ? super.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }

    @Nullable
    public TileCore getTile() {
        return this.tile;
    }

    public ItemStack getFirst() {
        return getStackInSlot(0);
    }

    public ItemStack getLast() {
        return getStackInSlot(getSlots() - 1);
    }

    public boolean isEmpty() {
        for (ItemStack stack : this.stacks) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isFull() {
        for (ItemStack stack : this.stacks) {
            if (stack.getCount() < stack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasEmptySlot() {
        for (ItemStack stack : this.stacks) {
            if (stack.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean isSlotEmpty(int slot) {
        return this.stacks.get(slot).isEmpty();
    }

    public ItemStack setSlotEmpty(int slot) {
        ItemStack stack = this.stacks.set(slot, ItemStack.EMPTY);
        onContentsChanged(slot);
        return stack;
    }

    public ItemStack setStack(int slot, ItemStack stack) {
        ItemStack stack1 = this.stacks.set(slot, stack);
        onContentsChanged(slot);
        return stack1;
    }

    public void clear() {
        set(getSlots());
    }

    public boolean isBlank() {
        return this.stacks.size() <= 0;
    }

    public NonNullList<ItemStack> getStacks() {
        return this.stacks;
    }

    public List<ItemStack> getNonEmptyStacks() {
        List<ItemStack> stacks = new ArrayList<>(this.stacks);
        stacks.removeIf(ItemStack::isEmpty);
        return stacks;
    }

    public ItemStack addNext(ItemStack stack) {
        for (int i = 0; i < getSlots(); ++i) {
            if (isItemValid(i, stack)) {
                insertItem(i, stack.copy(), false);
                return stack.copy();
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack removeNext() {
        for (int i = getSlots() - 1; i >= 0; --i) {
            ItemStack stack = setSlotEmpty(i);
            if (!stack.isEmpty()) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public Pair<Integer, ItemStack> firstNonEmpty() {
        return firstNonEmpty(stack -> true);
    }

    public Pair<Integer, ItemStack> lastNonEmpty() {
        return lastNonEmpty(stack -> true);
    }

    public Pair<Integer, ItemStack> firstNonEmpty(Predicate<ItemStack> filter) {
        return firstNonEmpty(filter, i -> true);
    }

    public Pair<Integer, ItemStack> firstNonEmptySlot(Predicate<Integer> index) {
        return firstNonEmpty(stack -> true, index);
    }

    public Pair<Integer, ItemStack> firstNonEmpty(Predicate<ItemStack> filter, Predicate<Integer> index) {
        for (int i = 0; i < getSlots(); i++) {
            ItemStack stack = getStackInSlot(i);
            if (!stack.isEmpty() && filter.test(stack) && index.test(i))
                return Pair.of(i, stack);
        }
        return Pair.of(0, ItemStack.EMPTY);
    }

    public Pair<Integer, ItemStack> lastNonEmpty(Predicate<ItemStack> filter) {
        return lastNonEmpty(filter, i -> true);
    }

    public Pair<Integer, ItemStack> lastNonEmptySlot(Predicate<Integer> index) {
        return lastNonEmpty(stack -> true, index);
    }

    public Pair<Integer, ItemStack> lastNonEmpty(Predicate<ItemStack> filter, Predicate<Integer> index) {
        for (int i = getSlots() - 1; i >= 0; i--) {
            ItemStack stack = getStackInSlot(i);
            if (!stack.isEmpty() && filter.test(stack) && index.test(i))
                return Pair.of(i, stack);
        }
        return Pair.of(0, ItemStack.EMPTY);
    }

    public void drop(World world, BlockPos pos) {
        this.stacks.forEach(stack -> {
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        });
        clear();
    }

    public void drop(int index, World world, BlockPos pos) {
        ItemStack stack = getStackInSlot(index);
        if (!stack.isEmpty()) {
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            setStack(index, ItemStack.EMPTY);
        }
    }

    public static Inventory from(IItemHandler handler) {
        Inventory inventory = new Inventory(handler.getSlots()) {
            @Override
            public ItemStack getStackInSlot(int slot) {
                return handler.getStackInSlot(slot);
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                return handler.insertItem(slot, stack, simulate);
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return handler.extractItem(slot, amount, true);
            }

            @Override
            public int getSlotLimit(int slot) {
                return handler.getSlotLimit(slot);
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return handler.isItemValid(slot, stack);
            }
        };
        return inventory;
    }

    public static NonNullList<ItemStack> toList(IItemHandler handler) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(handler.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < handler.getSlots(); i++) {
            stacks.set(i, handler.getStackInSlot(i));
        }
        return stacks;
    }

    public static LazyOptional<IItemHandler> get(World world, BlockPos pos, Direction side) {
        TileEntity te = world.getTileEntity(pos);
        return te != null ? te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side) : LazyOptional.empty();
    }

}
