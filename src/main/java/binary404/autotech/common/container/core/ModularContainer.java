package binary404.autotech.common.container.core;

import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.client.gui.core.widget.INativeWidget;
import binary404.autotech.client.gui.core.widget.Widget;
import binary404.autotech.client.gui.core.widget.WidgetUiAccess;
import binary404.autotech.common.container.ModContainers;
import binary404.autotech.common.core.util.PerTickIntCounter;
import binary404.autotech.common.core.util.Util;
import binary404.autotech.common.network.PacketHandler;
import binary404.autotech.common.network.PacketUIClientAction;
import binary404.autotech.common.network.PacketUIWidgetUpdate;
import io.netty.buffer.Unpooled;
import mezz.jei.startup.NetworkHandler;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ModularContainer extends Container implements WidgetUiAccess {

    protected final HashMap<Slot, INativeWidget> slotMap = new HashMap<>();
    private ModularUserInterface modularUI;

    public boolean accumulateWidgetUpdateData = false;
    public List<PacketUIWidgetUpdate> accumulatedUpdates = new ArrayList<>();

    public ModularContainer(ModularUserInterface modularUI) {
        super(ModContainers.modular, 0);
        this.modularUI = modularUI;
        modularUI.guiWidgets.values().forEach(widget -> widget.setUiAccess(this));
        modularUI.guiWidgets.values().stream()
                .flatMap(widget -> widget.getNativeWidgets().stream())
                .forEach(nativeWidget -> {
                    Slot slot = nativeWidget.getHandle();
                    slotMap.put(slot, nativeWidget);
                    addSlot(slot);
                });
        modularUI.triggerOpenListeners();
    }

    public ModularContainer(int i, PlayerInventory inventory, PacketBuffer buffer) {
        super(ModContainers.modular, i);
    }

    @Override
    public void notifySizeChange() {
    }

    //WARNING! WIDGET CHANGES SHOULD BE *STRICTLY* SYNCHRONIZED BETWEEN SERVER AND CLIENT,
    //OTHERWISE ID MISMATCH CAN HAPPEN BETWEEN ASSIGNED SLOTS!
    @Override
    public void notifyWidgetChange() {
        List<INativeWidget> nativeWidgets = modularUI.guiWidgets.values().stream()
                .flatMap(widget -> widget.getNativeWidgets().stream())
                .collect(Collectors.toList());

        Set<INativeWidget> removedWidgets = new HashSet<>(slotMap.values());
        removedWidgets.removeAll(nativeWidgets);
        if (!removedWidgets.isEmpty()) {
            for (INativeWidget removedWidget : removedWidgets) {
                Slot slotHandle = removedWidget.getHandle();
                this.slotMap.remove(slotHandle);
                //replace removed slot with empty placeholder to avoid list index shift
                EmptySlotPlaceholder emptySlotPlaceholder = new EmptySlotPlaceholder();
                emptySlotPlaceholder.slotNumber = slotHandle.slotNumber;
                this.inventorySlots.set(slotHandle.slotNumber, emptySlotPlaceholder);
                this.inventoryItemStacks.set(slotHandle.slotNumber, ItemStack.EMPTY);
            }
        }

        Set<INativeWidget> addedWidgets = new HashSet<>(nativeWidgets);
        addedWidgets.removeAll(slotMap.values());
        if (!addedWidgets.isEmpty()) {
            int[] emptySlotIndexes = inventorySlots.stream()
                    .filter(it -> it instanceof EmptySlotPlaceholder)
                    .mapToInt(slot -> slot.slotNumber).toArray();
            int currentIndex = 0;
            for (INativeWidget addedWidget : addedWidgets) {
                Slot slotHandle = addedWidget.getHandle();
                //add or replace empty slot in inventory
                this.slotMap.put(slotHandle, addedWidget);
                if (currentIndex < emptySlotIndexes.length) {
                    int slotIndex = emptySlotIndexes[currentIndex++];
                    slotHandle.slotNumber = slotIndex;
                    this.inventorySlots.set(slotIndex, slotHandle);
                    this.inventoryItemStacks.set(slotIndex, ItemStack.EMPTY);
                } else {
                    slotHandle.slotNumber = this.inventorySlots.size();
                    this.inventorySlots.add(slotHandle);
                    this.inventoryItemStacks.add(ItemStack.EMPTY);
                }
            }
        }
    }

    public ModularUserInterface getModularUI() {
        return modularUI;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        modularUI.triggerCloseListeners();
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        modularUI.guiWidgets.values().forEach(Widget::detectAndSendChanges);
    }

    @Override
    public void sendSlotUpdate(INativeWidget slot) {
        Slot slotHandle = slot.getHandle();
        for (IContainerListener listener : listeners) {
            listener.sendSlotContents(this, slotHandle.slotNumber, slotHandle.getStack());
        }
    }

    @Override
    public void sendHeldItemUpdate() {
        for (IContainerListener listener : listeners) {
            if (listener instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) listener;
                player.connection.sendPacket(new SSetSlotPacket(-1, -1, player.inventory.getItemStack()));
            }
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (listeners.size() > 0) {
            modularUI.guiWidgets.values().forEach(Widget::detectAndSendChanges);
        }
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        if (slotId >= 0 && slotId < inventorySlots.size()) {
            Slot slot = getSlot(slotId);
            ItemStack result = slotMap.get(slot).slotClick(dragType, clickTypeIn, player);
            if (result == INativeWidget.VANILLA_LOGIC) {
                return super.slotClick(slotId, dragType, clickTypeIn, player);
            }
            return result;
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    private PerTickIntCounter transferredPerTick = new PerTickIntCounter(0);

    private List<INativeWidget> getShiftClickSlots(ItemStack itemStack, boolean fromContainer) {
        return slotMap.values().stream()
                .filter(it -> it.canMergeSlot(itemStack))
                .filter(it -> it.getSlotLocationInfo().isPlayerInventory == fromContainer)
                .sorted(Comparator.comparing(s -> (fromContainer ? -1 : 1) * s.getHandle().slotNumber))
                .collect(Collectors.toList());
    }

    @Override
    public boolean attemptMergeStack(ItemStack itemStack, boolean fromContainer, boolean simulate) {
        List<Slot> inventorySlots = getShiftClickSlots(itemStack, fromContainer).stream()
                .map(INativeWidget::getHandle)
                .collect(Collectors.toList());
        return Util.mergeItemStack(itemStack, inventorySlots, simulate);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        Slot slot = inventorySlots.get(index);
        if (!slot.canTakeStack(player)) {
            return ItemStack.EMPTY;
        }
        if (!slot.getHasStack()) {
            //return empty if we can't transfer it
            return ItemStack.EMPTY;
        }
        ItemStack stackInSlot = slot.getStack();
        ItemStack stackToMerge = slotMap.get(slot).onItemTake(player, stackInSlot.copy(), true);
        boolean fromContainer = !slotMap.get(slot).getSlotLocationInfo().isPlayerInventory;
        if (!attemptMergeStack(stackToMerge, fromContainer, true)) {
            return ItemStack.EMPTY;
        }
        int itemsMerged;
        if (stackToMerge.isEmpty() || slotMap.get(slot).canMergeSlot(stackToMerge)) {
            itemsMerged = stackInSlot.getCount() - stackToMerge.getCount();
        } else {
            //if we can't have partial stack merge, we have to use all the stack
            itemsMerged = stackInSlot.getCount();
        }
        int itemsToExtract = itemsMerged;
        itemsMerged += transferredPerTick.get(player.world);
        if (itemsMerged > stackInSlot.getMaxStackSize()) {
            //we can merge at most one stack at a time
            return ItemStack.EMPTY;
        }
        transferredPerTick.increment(player.world, itemsToExtract);
        //otherwise, perform extraction and merge
        ItemStack extractedStack = stackInSlot.split(itemsToExtract);
        if (stackInSlot.isEmpty()) {
            slot.putStack(ItemStack.EMPTY);
        } else {
            slot.onSlotChanged();
        }
        extractedStack = slotMap.get(slot).onItemTake(player, extractedStack, false);
        ItemStack resultStack = extractedStack.copy();
        if (!attemptMergeStack(extractedStack, fromContainer, false)) {
            resultStack = ItemStack.EMPTY;
        }
        if (!extractedStack.isEmpty()) {
            player.dropItem(extractedStack, false, false);
            resultStack = ItemStack.EMPTY;
        }
        return resultStack;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotMap.get(slotIn).canMergeSlot(stack);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void writeClientAction(Widget widget, int updateId, Consumer<PacketBuffer> payloadWriter) {
        int widgetId = modularUI.guiWidgets.inverse().get(widget);
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeVarInt(updateId);
        payloadWriter.accept(packetBuffer);
        if (modularUI.player instanceof ClientPlayerEntity) {
            PacketUIClientAction widgetUpdate = new PacketUIClientAction(windowId, widgetId, packetBuffer);
            PacketHandler.sendToServer(widgetUpdate);
        }
    }

    @Override
    public void writeUpdateInfo(Widget widget, int updateId, Consumer<PacketBuffer> payloadWriter) {
        int widgetId = modularUI.guiWidgets.inverse().get(widget);
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeVarInt(updateId);
        payloadWriter.accept(packetBuffer);
        if (modularUI.player instanceof ServerPlayerEntity) {
            PacketUIWidgetUpdate widgetUpdate = new PacketUIWidgetUpdate(windowId, widgetId, packetBuffer);
            if (!accumulateWidgetUpdateData) {
                PacketHandler.sendTo((ServerPlayerEntity) modularUI.player, widgetUpdate);
            } else {
                accumulatedUpdates.add(widgetUpdate);
            }
        }
    }

    private static class EmptySlotPlaceholder extends Slot {

        private static final IInventory EMPTY_INVENTORY = new Inventory(0);

        public EmptySlotPlaceholder() {
            super(EMPTY_INVENTORY, 0, -100000, -100000);
        }

        @Override
        public ItemStack getStack() {
            return ItemStack.EMPTY;
        }

        @Override
        public void putStack(ItemStack stack) {
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }

        @Override
        public boolean canTakeStack(PlayerEntity playerIn) {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }
    }

}
