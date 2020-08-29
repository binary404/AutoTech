package binary404.autotech.common.core.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ComparableItemStack {
    public Item item;

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
            return item.delegate.get() == other.item.delegate.get();
        }

        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ComparableItemStack && isEqual((ComparableItemStack) obj);
    }

    @Override
    public int hashCode() {
        return Item.getIdFromItem(item) << 16;
    }

    @Override
    public String toString() {
        return item.toString();
    }
}
