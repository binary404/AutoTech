package binary404.autotech.common.core.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.stream.Stream;

public class PlayerUtil {

    public static NonNullList<ItemStack> invStacks(PlayerEntity player) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        PlayerInventory inventory = player.inventory;
        Stream.of(inventory.mainInventory, inventory.armorInventory, inventory.offHandInventory).forEach(stacks::addAll);
        return stacks;
    }

}
