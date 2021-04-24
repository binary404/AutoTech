package binary404.autotech.common.network;

import binary404.autotech.client.gui.TileEntityUIFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PacketUIOpen {

    public final int uiFactoryId;
    public final PacketBuffer serializedHolder;
    public final int windowId;
    public final List<PacketUIWidgetUpdate> initialWidgetUpdates;

    public PacketUIOpen(int uiFactoryId, PacketBuffer serializedHolder, int windowId, List<PacketUIWidgetUpdate> initialWidgetUpdates) {
        this.uiFactoryId = uiFactoryId;
        this.serializedHolder = serializedHolder;
        this.windowId = windowId;
        this.initialWidgetUpdates = initialWidgetUpdates;
    }

    public static void encode(PacketUIOpen packet, PacketBuffer buffer) {
        buffer.writeVarInt(packet.serializedHolder.readableBytes());
        buffer.writeBytes(packet.serializedHolder);
        buffer.writeVarInt(packet.uiFactoryId);
        buffer.writeVarInt(packet.windowId);
        buffer.writeVarInt(packet.initialWidgetUpdates.size());
        for (PacketUIWidgetUpdate widgetUpdate : packet.initialWidgetUpdates) {
            widgetUpdate.encode(widgetUpdate, buffer);
        }
    }

    public static PacketUIOpen decode(PacketBuffer buffer) {
        ByteBuf directSliceBuffer = buffer.readBytes(buffer.readVarInt());
        ByteBuf copiedDataBuffer = Unpooled.copiedBuffer(directSliceBuffer);
        directSliceBuffer.release();
        int uiFactoryId = buffer.readVarInt();
        int windowId = buffer.readVarInt();
        ArrayList<PacketUIWidgetUpdate> initialWidgetUpdates = new ArrayList<>();
        int initialWidgetUpdatesCount = buffer.readVarInt();
        for (int i = 0; i < initialWidgetUpdatesCount; i++) {
            initialWidgetUpdates.add(PacketUIWidgetUpdate.decode(buffer));
        }
        return new PacketUIOpen(uiFactoryId, new PacketBuffer(copiedDataBuffer), windowId, initialWidgetUpdates);
    }

    public static void handle(PacketUIOpen packet, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            TileEntityUIFactory.INSTANCE.initClientUI(packet.serializedHolder, packet.windowId, packet.initialWidgetUpdates);
        }
        ctx.get().setPacketHandled(true);
    }

}
