package binary404.autotech.common.core.util;

import binary404.autotech.AutoTech;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class NBTUtil {

    public static final String TAG_STORABLE_STACK = AutoTech.modid + "tile_stack_nbt";

    public static void writeItems(IItemHandler handler, String tagName, CompoundNBT tag) {
        ListNBT tagList = new ListNBT();
        if (handler != null)
            for (int i = 0; i < handler.getSlots(); i++) {
                if (!handler.getStackInSlot(i).isEmpty()) {
                    CompoundNBT stackTag = new CompoundNBT();
                    stackTag.putInt("Slot", i);
                    handler.getStackInSlot(i).write(stackTag);
                    tagList.add(stackTag);
                }
            }

        tag.put(tagName, tagList);
    }

    public static void readItems(IItemHandlerModifiable handler, String tagName, CompoundNBT tag) {
        if (tag.contains(tagName)) {
            ListNBT tagList = tag.getList(tagName, Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < tagList.size(); i++) {
                int slot = tagList.getCompound(i).getInt("Slot");

                if (slot >= 0 && slot < handler.getSlots()) {
                    handler.setStackInSlot(slot, ItemStack.read(tagList.getCompound(i)));
                }
            }
        }
    }

}
