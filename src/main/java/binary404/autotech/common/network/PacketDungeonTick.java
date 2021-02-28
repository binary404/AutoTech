package binary404.autotech.common.network;

import binary404.autotech.common.core.JumpTestHandler;
import binary404.autotech.common.world.dungeon.DungeonEvents;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketDungeonTick {

    public int remainingTicks;

    public PacketDungeonTick() {

    }

    public PacketDungeonTick(int remainingTicks) {
        this.remainingTicks = remainingTicks;
    }

    public static void encode(PacketDungeonTick msg, PacketBuffer buffer) {
        buffer.writeInt(msg.remainingTicks);
    }

    public static PacketDungeonTick decode(PacketBuffer buffer) {
        return new PacketDungeonTick(buffer.readInt());
    }

    public static void handle(PacketDungeonTick msg, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            DungeonEvents.remainingTicks = msg.remainingTicks;
        });
        contextSupplier.get().setPacketHandled(true);
    }

}
