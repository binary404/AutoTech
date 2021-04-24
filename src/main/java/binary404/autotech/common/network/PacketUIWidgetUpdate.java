package binary404.autotech.common.network;

import binary404.autotech.client.gui.ModularGui;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUIWidgetUpdate {

    public final int windowId;
    public final int widgetId;
    public final PacketBuffer updateData;

    public PacketUIWidgetUpdate(int windowId, int widgetId, PacketBuffer updateData) {
        this.windowId = windowId;
        this.widgetId = widgetId;
        this.updateData = updateData;
    }

    public static void encode(PacketUIWidgetUpdate packet, PacketBuffer buffer) {
        buffer.writeVarInt(packet.updateData.readableBytes());
        buffer.writeBytes(packet.updateData);
        buffer.writeVarInt(packet.windowId);
        buffer.writeVarInt(packet.widgetId);
    }

    public static PacketUIWidgetUpdate decode(PacketBuffer buffer) {
        ByteBuf directSliceBuffer = buffer.readBytes(buffer.readVarInt());
        ByteBuf copiedDataBuffer = Unpooled.copiedBuffer(directSliceBuffer);
        directSliceBuffer.release();
        return new PacketUIWidgetUpdate(buffer.readVarInt(), buffer.readVarInt(), new PacketBuffer(copiedDataBuffer));
    }

    public static void handle(PacketUIWidgetUpdate packet, Supplier<NetworkEvent.Context> ctx) {
        if(ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        ctx.get().enqueueWork(() -> {
                Screen screen = Minecraft.getInstance().currentScreen;
                if (screen instanceof ModularGui) {
                    ((ModularGui) screen).handleWidgetUpdate(packet);
                }
            });

        ctx.get().setPacketHandled(true);
    }

}
