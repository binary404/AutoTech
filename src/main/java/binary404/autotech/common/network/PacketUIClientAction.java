package binary404.autotech.common.network;

import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.common.container.core.ModularContainer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUIClientAction {

    public final int windowId;
    public final int widgetId;
    public final PacketBuffer updateData;

    public PacketUIClientAction(int windowId, int widgetId, PacketBuffer updateData) {
        this.windowId = windowId;
        this.widgetId = widgetId;
        this.updateData = updateData;
    }

    public static void encode(PacketUIClientAction packet, PacketBuffer buffer) {
        buffer.writeVarInt(packet.updateData.readableBytes());
        buffer.writeBytes(packet.updateData);
        buffer.writeVarInt(packet.windowId);
        buffer.writeVarInt(packet.widgetId);
    }

    public static PacketUIClientAction decode(PacketBuffer buffer) {
        ByteBuf directSliceBuffer = buffer.readBytes(buffer.readVarInt());
        ByteBuf copiedBuffer = Unpooled.copiedBuffer(directSliceBuffer);
        directSliceBuffer.release();
        return new PacketUIClientAction(buffer.readVarInt(), buffer.readVarInt(), new PacketBuffer(copiedBuffer));
    }

    public static void handle(PacketUIClientAction packet, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                Container openContainer = player.openContainer;
                if (openContainer instanceof ModularContainer && openContainer.windowId == packet.windowId) {
                    ModularUserInterface modularUserInterface = ((ModularContainer) openContainer).getModularUI();
                    PacketBuffer buffer = packet.updateData;
                    modularUserInterface.guiWidgets.get(packet.widgetId).handleClientAction(buffer.readVarInt(), buffer);
                }
            }
            ctx.get().setPacketHandled(true);
        }
        ctx.get().setPacketHandled(true);
    }

}
