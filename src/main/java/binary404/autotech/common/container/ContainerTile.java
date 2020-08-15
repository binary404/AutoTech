package binary404.autotech.common.container;

import binary404.autotech.common.tile.TileCore;
import binary404.autotech.common.tile.util.IInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class ContainerTile<T extends TileCore<?> & IInventory> extends ContainerCore {

    public final T te;

    public ContainerTile(@Nullable ContainerType<?> containerType, int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(containerType, id, inventory, getInventory(inventory.player, buffer.readBlockPos()));
    }

    public ContainerTile(@Nullable ContainerType<?> type, int id, PlayerInventory inventory, T te) {
        super(type, id, inventory);
        this.te = te;
        init(inventory, te);
        this.te.setContainerOpen(true);
    }

    @Override
    protected final void init(PlayerInventory inventory) {
        super.init(inventory);
    }

    protected void init(PlayerInventory inventory, T te) {
        super.init(inventory);
    }

    @SuppressWarnings("unchecked")
    protected static <T extends TileCore<?>> T getInventory(PlayerEntity player, BlockPos pos) {
        TileEntity tile = player.world.getTileEntity(pos);
        if (tile instanceof TileCore<?>)
            return (T) tile;
        return (T) new TileCore<>(TileEntityType.SIGN);
    }

    @Override
    public void onContainerClosed(PlayerEntity player) {
        super.onContainerClosed(player);
        this.te.setContainerOpen(false);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();
            int size = this.te.getInventory().getSlots();
            if (index < size) {
                if (!mergeItemStack(stack1, size, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(stack1, 0, size, false)) {
                return ItemStack.EMPTY;
            }
            if (stack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return stack;
    }

}
