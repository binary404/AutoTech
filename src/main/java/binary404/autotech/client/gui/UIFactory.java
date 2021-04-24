package binary404.autotech.client.gui;

import binary404.autotech.client.gui.core.IUIHolder;
import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.client.gui.core.widget.Widget;
import binary404.autotech.common.container.core.ModularContainer;
import binary404.autotech.common.network.PacketHandler;
import binary404.autotech.common.network.PacketUIOpen;
import binary404.autotech.common.network.PacketUIWidgetUpdate;
import io.netty.buffer.Unpooled;
import mezz.jei.startup.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class UIFactory<E extends IUIHolder> {

    public final void openUI(E holder, ServerPlayerEntity player) {
        if (player instanceof FakePlayer) {
            return;
        }
        ModularUserInterface uiTemplate = createUITemplate(holder, player);
        if (uiTemplate == null)
            return;
        uiTemplate.initWidgets();

        player.getNextWindowId();
        player.closeContainer();
        int currentWindowId = player.currentWindowId;

        PacketBuffer serializedHolder = new PacketBuffer(Unpooled.buffer());
        writeHolderToSyncData(serializedHolder, holder);
        int uiFactoryId = 0;

        ModularContainer container = new ModularContainer(uiTemplate);
        container.windowId = currentWindowId;
        //accumulate all initial updates of widgets in open packet
        container.accumulateWidgetUpdateData = true;
        uiTemplate.guiWidgets.values().forEach(Widget::detectAndSendChanges);
        container.accumulateWidgetUpdateData = false;
        ArrayList<PacketUIWidgetUpdate> updateData = new ArrayList<>(container.accumulatedUpdates);
        container.accumulatedUpdates.clear();

        PacketUIOpen packet = new PacketUIOpen(uiFactoryId, serializedHolder, currentWindowId, updateData);
        PacketHandler.sendTo(player, packet);

        container.addListener(player);
        player.openContainer = container;

        //and fire forge event only in the end
        MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, container));
    }

    public final void initClientUI(PacketBuffer serializedHolder, int windowId, List<PacketUIWidgetUpdate> initialWidgetUpdates) {
        E holder = readHolderFromSyncData(serializedHolder);
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerEntity entityPlayer = minecraft.player;

        ModularUserInterface uiTemplate = createUITemplate(holder, entityPlayer);
        uiTemplate.initWidgets();
        ModularGui modularUIGui = new ModularGui(uiTemplate);
        modularUIGui.getContainer().windowId = windowId;
        for (PacketUIWidgetUpdate packet : initialWidgetUpdates) {
            modularUIGui.handleWidgetUpdate(packet);
        }
        minecraft.displayGuiScreen(modularUIGui);
        minecraft.player.openContainer.windowId = windowId;
    }

    protected abstract ModularUserInterface createUITemplate(E holder, PlayerEntity entityPlayer);

    protected abstract E readHolderFromSyncData(PacketBuffer syncData);

    protected abstract void writeHolderToSyncData(PacketBuffer syncData, E holder);

}
