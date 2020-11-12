package binary404.autotech.common.core.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ComparableItemStack {
    public Item item;

    public static ComparableItemStack convert(ItemStack stack) {
        return new ComparableItemStack(stack);
    }

    public static List<ComparableItemStack> convert(List<ItemStack> stacks) {
        List<ComparableItemStack> output = new ArrayList<>();
        for (ItemStack stack : stacks) {
            output.add(convert(stack));
        }
        return output;
    }

    public ComparableItemStack(ItemStack stack) {
        this.item = stack.getItem();
    }

    public boolean isEqual(ComparableItemStack other) {
        if (other == null) {
            return false;
        }

        if (item.equals(other.item)) {
            return true;
        }

        if (item != null && other.item != null) {
            return item.delegate.get().equals(other.item.delegate.get());
        }

        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ComparableItemStack && isEqual((ComparableItemStack) obj);
    }

    @Override
    public int hashCode() {
        int toReturn = Item.getIdFromItem(item) << 16;
        return toReturn;
    }

    @Override
    public String toString() {
        return item.toString();
    }
}
