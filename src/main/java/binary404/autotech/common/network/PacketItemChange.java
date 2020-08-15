package binary404.autotech.common.network;

import binary404.autotech.common.tile.TileEnergy;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketItemChange {

    private int mode;
    private BlockPos pos;

    public PacketItemChange(int mode, BlockPos pos) {
        this.mode = mode;
        this.pos = pos;
    }

    public PacketItemChange() {
        this(0, BlockPos.ZERO);
    }

    public static void encode(PacketItemChange msg, PacketBuffer buffer) {
        buffer.writeInt(msg.mode);
        buffer.writeBlockPos(msg.pos);
    }

    public static PacketItemChange decode(PacketBuffer buffer) {
        return new PacketItemChange(buffer.readInt(), buffer.readBlockPos());
    }

    public static void handle(PacketItemChange msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                TileEntity tileEntity = player.world.getTileEntity(msg.pos);
                if (tileEntity instanceof TileEnergy) {
                    TileEnergy storage = ((TileEnergy) tileEntity);
                    storage.itemConfig.nextType(Direction.byIndex(msg.mode));
                    storage.sync();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
