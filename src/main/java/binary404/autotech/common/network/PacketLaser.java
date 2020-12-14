package binary404.autotech.common.network;

import binary404.autotech.client.fx.LaserRenderHandler;
import binary404.autotech.common.item.ItemEnergySuit;
import binary404.autotech.common.item.ModItems;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketLaser {

    private double x, y, z, tx, ty, tz;

    public PacketLaser(double x, double y, double z, double tx, double ty, double tz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
    }

    public static void encode(PacketLaser msg, PacketBuffer buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeDouble(msg.tx);
        buffer.writeDouble(msg.ty);
        buffer.writeDouble(msg.tz);
    }

    public static PacketLaser decode(PacketBuffer buffer) {
        return new PacketLaser(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static void handle(PacketLaser msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        ctx.get().enqueueWork(() -> {
            LaserRenderHandler.addLaser(msg.x, msg.y, msg.z, msg.tx, msg.ty, msg.tz, 80, true);
        });
        ctx.get().setPacketHandled(true);
    }

}
