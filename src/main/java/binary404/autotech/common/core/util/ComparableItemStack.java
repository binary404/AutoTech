package binary404.autotech.common.core.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;

import java.util.ArrayList;
import java.util.List;

public class ComparableItemStack {
    public Item item;

    public static ComparableItemStack convert(ItemStack stack) {
        return new ComparableItemStack(stack);
    }

    public ITag.INamedTag tag;

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

    public ComparableItemStack(ITag.INamedTag<Item> tag) {
        this.tag = tag;

        this.item = tag.getAllElements().get(0);
    }

    public boolean isEqual(ComparableItemStack other) {
        if (other == null || (other.tag == null && other.item == null)) {
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

    public boolean isItemEqual(ComparableItemStack other) {
        return other != null && isEqual(other) && tagEqual(other);
    }

    public boolean tagEqual(ComparableItemStack other) {
        //No tag to check
        if (tag == null)
            return true;

        if (other.tag != null) {
            //Tags are equal
            if (other.tag == tag) {
                return true;
            }
        }

        //Item is found in tag
        if (other.item != null  && tag.contains(other.item))
            return true;
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ComparableItemStack && isItemEqual((ComparableItemStack) obj);
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
