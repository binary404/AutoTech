package binary404.autotech.common.core.util;

import binary404.autotech.common.core.logistics.IEnergyContainerItem;
import codechicken.lib.inventory.InventoryUtils;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static binary404.autotech.common.core.util.Predicates.not;
import static binary404.autotech.common.core.util.Predicates.or;

public class Util {

    private static final Random RANDOM = new Random();

    public static int safeInt(long value) {
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }

    public static int safeInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getRandomIntXSTR(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static int fluidHashCode(FluidStack stack) {
        return stack.getFluid().getRegistryName().toString().hashCode();
    }

    public static ItemStack setDefaultEnergyTag(ItemStack container, int energy) {

        if (!container.hasTag()) {
            container.setTag(new CompoundNBT());
        }
        container.getTag().putInt("Energy", energy);

        return container;
    }

    public static boolean isEnergyContainerItem(ItemStack container) {
        return !container.isEmpty() && container.getItem() instanceof IEnergyContainerItem;
    }

    public static boolean isEnergyHandler(ItemStack stack) {
        return !stack.isEmpty() && stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
    }

    public static boolean tryPercentage(double percent) {
        return RANDOM.nextDouble() < percent;
    }

    public static boolean tryPercentage(Random random, double percent) {
        return random.nextDouble() < percent;
    }

    public static boolean mergeItemStack(ItemStack itemStack, List<Slot> slots, boolean simulate) {
        if (itemStack.isEmpty())
            return false; //if we are merging empty stack, return

        boolean merged = false;
        //iterate non-empty slots first
        //to try to insert stack into them
        for (Slot slot : slots) {
            if (!slot.isItemValid(itemStack))
                continue; //if itemstack cannot be placed into that slot, continue
            ItemStack stackInSlot = slot.getStack();
            if (!ItemStack.areItemsEqual(itemStack, stackInSlot) ||
                    !ItemStack.areItemStackTagsEqual(itemStack, stackInSlot))
                continue; //if itemstacks don't match, continue
            int slotMaxStackSize = Math.min(stackInSlot.getMaxStackSize(), slot.getItemStackLimit(stackInSlot));
            int amountToInsert = Math.min(itemStack.getCount(), slotMaxStackSize - stackInSlot.getCount());
            if (amountToInsert == 0)
                continue; //if we can't insert anything, continue
            //shrink our stack, grow slot's stack and mark slot as changed
            if (!simulate) {
                stackInSlot.grow(amountToInsert);
            }
            itemStack.shrink(amountToInsert);
            slot.onSlotChanged();
            merged = true;
            if (itemStack.isEmpty())
                return true; //if we inserted all items, return
        }

        //then try to insert itemstack into empty slots
        //breaking it into pieces if needed
        for (Slot slot : slots) {
            if (!slot.isItemValid(itemStack))
                continue; //if itemstack cannot be placed into that slot, continue
            if (slot.getHasStack())
                continue; //if slot contains something, continue
            int amountToInsert = Math.min(itemStack.getCount(), slot.getItemStackLimit(itemStack));
            if (amountToInsert == 0)
                continue; //if we can't insert anything, continue
            //split our stack and put result in slot
            ItemStack stackInSlot = itemStack.split(amountToInsert);
            if (!simulate) {
                slot.putStack(stackInSlot);
            }
            merged = true;
            if (itemStack.isEmpty())
                return true; //if we inserted all items, return
        }
        return merged;
    }

    public static List<ItemStack> itemHandlerToList(IItemHandlerModifiable inputs) {
        return new AbstractList<ItemStack>() {
            @Override
            public ItemStack set(int index, ItemStack element) {
                ItemStack oldStack = inputs.getStackInSlot(index);
                inputs.setStackInSlot(index, element == null ? ItemStack.EMPTY : element);
                return oldStack;
            }

            @Override
            public ItemStack get(int index) {
                return inputs.getStackInSlot(index);
            }

            @Override
            public int size() {
                return inputs.getSlots();
            }
        };
    }

    public static NonNullList<ItemStack> copyStackList(List<ItemStack> itemStacks) {
        ItemStack[] stacks = new ItemStack[itemStacks.size()];
        for (int i = 0; i < itemStacks.size(); i++) {
            stacks[i] = copy(itemStacks.get(i));
        }
        return NonNullList.from(ItemStack.EMPTY, stacks);
    }

    public static ItemStack copy(ItemStack... stacks) {
        for (ItemStack stack : stacks)
            if (!stack.isEmpty()) return stack.copy();
        return ItemStack.EMPTY;
    }

    public static boolean isBetweenInclusive(long start, long end, long value) {
        return start <= value && value <= end;
    }

    public static int amountOfNonNullElements(List<?> collection) {
        int amount = 0;
        for (Object object : collection) {
            if (object != null) amount++;
        }
        return amount;
    }

    public static int amountOfNonEmptyStacks(List<ItemStack> collection) {
        int amount = 0;
        for (ItemStack object : collection) {
            if (object != null && !object.isEmpty()) amount++;
        }
        return amount;
    }

    public static <T> boolean iterableContains(Iterable<T> list, Predicate<T> predicate) {
        for (T t : list) {
            if (predicate.test(t)) {
                return true;
            }
        }
        return false;
    }

    public static boolean simulateItemStackMerge(List<ItemStack> inputItems,
                                                 IItemHandler inventory) {
        // Generate a stack-minimized copy of the input items
        final List<ItemStack> itemStacks = compactItemStacks(inputItems);

        // If there's enough empty output slots then we don't need to compute merges.
        final int emptySlots = getNumberOfEmptySlotsInInventory(inventory);
        if (itemStacks.size() <= emptySlots)
            return true;

        // Sort by the number of items in each stack so we merge smallest stacks first.
        itemStacks.sort(Comparator.comparingInt(ItemStack::getCount));

        // Deep copy the contents of the target inventory, skipping empty stacks
        final List<ItemStack> inventoryStacks = deepCopy(inventory, false);

        // Perform a merge of the ItemStacks
        mergeItemStacks(itemStacks, inventoryStacks);

        // Return whether there are now sufficient empty slots to fit the unmerged items.
        return itemStacks.size() <= emptySlots;
    }

    static List<ItemStack> compactItemStacks(Collection<ItemStack> inputItems) {
        Hash.Strategy<ItemStack> strategy = ItemStackHashStrategy.comparingAllButCount();
        final Supplier<Map<ItemStack, Integer>> mapSupplier =
                () -> new Object2IntOpenCustomHashMap<>(strategy);

        return inputItems.stream()

                // keep only non-empty item stacks
                .filter(not(ItemStack::isEmpty))

                // Track the number of identical items
                .collect(Collectors.toMap(Function.identity(),
                        ItemStack::getCount,
                        Math::addExact,
                        mapSupplier))

                // Create a single stack of the combined count for each item
                .entrySet().stream()
                .map(entry -> {
                    ItemStack combined = entry.getKey().copy();
                    combined.setCount(entry.getValue());
                    return combined;
                })

                // Normalize these stacks into separate valid ItemStacks, flattening them into a List
                .map(Util::normalizeItemStack)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }


    public static int getNumberOfEmptySlotsInInventory(IItemHandler inventory) {
        // IItemHandler#getSlots() is an int, so this cast is safe.
        return (int)
                streamFrom(inventory)
                        .filter(ItemStack::isEmpty)
                        .count();
    }

    public static List<ItemStack> deepCopy(final IItemHandler inventory,
                                           final boolean keepEmpty) {
        return streamFrom(inventory)
                .filter(or(x -> keepEmpty,
                        not(ItemStack::isEmpty)))
                .map(ItemStack::copy)
                .collect(Collectors.toList());
    }

    public static List<ItemStack> normalizeItemStack(ItemStack stack) {
        if (stack.isEmpty())
            return Collections.emptyList();

        int maxCount = stack.getMaxStackSize();

        if (stack.getCount() <= maxCount)
            return Collections.singletonList(stack.copy());

        return Collections.unmodifiableList(apportionStack(stack, maxCount));
    }

    public static List<ItemStack> apportionStack(ItemStack stack,
                                                 final int maxCount) {
        if (stack.isEmpty())
            throw new IllegalArgumentException("Cannot apportion an empty stack.");
        if (maxCount <= 0)
            throw new IllegalArgumentException("Count must be non-zero and positive.");

        final ArrayList<ItemStack> splitStacks = new ArrayList<>();

        int count = stack.getCount();
        int numStacks = count / maxCount;
        int remainder = count % maxCount;

        for (int fullStackCount = numStacks; fullStackCount > 0; fullStackCount--) {
            ItemStack fullStack = stack.copy();
            fullStack.setCount(maxCount);
            splitStacks.add(fullStack);
        }

        if (remainder > 0) {
            ItemStack partialStack = stack.copy();
            partialStack.setCount(remainder);
            splitStacks.add(partialStack);
        }

        return splitStacks;
    }

    public static Stream<ItemStack> streamFrom(IItemHandler inventory) {
        return StreamSupport.stream(iterableFrom(inventory).spliterator(),
                false);
    }

    public static Iterable<ItemStack> iterableFrom(IItemHandler inventory) {
        return new Iterable<ItemStack>() {
            @Override
            public Iterator<ItemStack> iterator() {
                return new Iterator<ItemStack>() {
                    private int cursor = 0;

                    @Override
                    public boolean hasNext() {
                        return cursor < inventory.getSlots();
                    }

                    @Override
                    public ItemStack next() {
                        if (!hasNext())
                            throw new NoSuchElementException();

                        ItemStack next = inventory.getStackInSlot(cursor);
                        cursor++;
                        return next;
                    }
                };
            }

            @Override
            public Spliterator<ItemStack> spliterator() {
                return Spliterators.spliterator(iterator(), inventory.getSlots(), 0);
            }
        };
    }

    static void mergeItemStacks(Collection<ItemStack> source, Collection<ItemStack> destination) {
        // Since we're mutating the collection during iteration, use an iterator.
        final Iterator<ItemStack> sourceItemStacks = source.iterator();
        while (sourceItemStacks.hasNext()) {
            final ItemStack sourceItemStack = sourceItemStacks.next();

            // Find a matching item in the output bus, if any
            for (ItemStack destItemStack : destination)
                if (ItemStack.areItemsEqual(destItemStack, sourceItemStack) &&
                        ItemStack.areItemStackTagsEqual(destItemStack, sourceItemStack)) {
                    // if it's possible to merge stacks
                    final int availableSlots = destItemStack.getMaxStackSize() - destItemStack.getCount();
                    if (availableSlots > 0) {
                        final int itemCount = Math.min(availableSlots, sourceItemStack.getCount());
                        sourceItemStack.shrink(itemCount);
                        destItemStack.grow(itemCount);

                        // if the output stack was merged completely, remove it and stop looking
                        if (sourceItemStack.isEmpty()) {
                            sourceItemStacks.remove();
                            break;
                        }
                    }
                }
        }
    }

}
