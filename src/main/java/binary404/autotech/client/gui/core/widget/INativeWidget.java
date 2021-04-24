package binary404.autotech.client.gui.core.widget;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public interface INativeWidget extends EnableNotifiedWidget {

    ItemStack VANILLA_LOGIC = new ItemStack(Items.AIR);

    @Override
    void setEnabled(boolean isEnabled);

    /**
     * You should return MC slot handle instance you created earlier
     *
     * @return MC slot
     */
    Slot getHandle();

    /**
     * @return true if this slot belongs to player inventory
     */
    SlotLocationInfo getSlotLocationInfo();

    /**
     * @return true when this slot is valid for double click merging
     */
    boolean canMergeSlot(ItemStack stack);

    /**
     * Called when item is taken from the slot
     * Simulated take is used to compute slot merging behavior
     * This method should not modify slot state if it is simulated
     */
    default ItemStack onItemTake(PlayerEntity player, ItemStack stack, boolean simulate) {
        return stack;
    }

    /**
     * Called when slot is clicked in Container
     * Return {@link INativeWidget#VANILLA_LOGIC} to fallback to vanilla logic
     */
    ItemStack slotClick(int dragType, ClickType clickTypeIn, PlayerEntity player);

    class SlotLocationInfo {
        public final boolean isPlayerInventory;
        public final boolean isHotbarSlot;

        public SlotLocationInfo(boolean isPlayerInventory, boolean isHotbarSlot) {
            this.isPlayerInventory = isPlayerInventory;
            this.isHotbarSlot = isHotbarSlot;
        }
    }

}
