package binary404.autotech.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class ItemMortar extends Item {

    public ItemMortar(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 400;
    }

    @Override
    public boolean hasContainerItem() {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack stack = itemStack.copy();
        stack.attemptDamageItem(4, new Random(), null);
        return stack;
    }
}
