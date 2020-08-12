package binary404.autotech.common.core.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class StackUtil {

    public static CompoundNBT getTagOrEmpty(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        return nbt != null ? nbt : new CompoundNBT();
    }

}
