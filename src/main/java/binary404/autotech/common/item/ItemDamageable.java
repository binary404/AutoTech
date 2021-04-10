package binary404.autotech.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class ItemDamageable extends Item {

    int damage;

    public ItemDamageable(Properties properties, int damage) {
        super(properties);
        this.damage = damage;
    }

    @Override
    public boolean hasContainerItem() {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ItemStack newStack = stack.copy();
        newStack.setCount(1);
        damage(newStack, damage);
        return newStack;
    }

    public boolean damage(ItemStack stack, int amount) {
        int newDamageValue = stack.getDamage() + amount;
        stack.setDamage(newDamageValue);
        if (stack.getDamage() >= stack.getMaxDamage()) {
            stack.shrink(1);
        }
        return true;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }
}
