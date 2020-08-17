package binary404.autotech.common.network;

import binary404.autotech.common.tile.core.TileEnergy;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketEnergyChange {

    private int mode;
    private BlockPos pos;

    public PacketEnergyChange(int mode, BlockPos pos) {
        this.mode = mode;
        this.pos = pos;
    }

    public PacketEnergyChange() {
        this(0, BlockPos.ZERO);
    }

    public static void encode(PacketEnergyChange msg, PacketBuffer buffer) {
        buffer.writeInt(msg.mode);
        buffer.writeBlockPos(msg.pos);
    }

    public static PacketEnergyChange decode(PacketBuffer buffer) {
        return new PacketEnergyChange(buffer.readInt(), buffer.readBlockPos());
    }

    public static void handle(PacketEnergyChange msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                TileEntity tileEntity = player.world.getTileEntity(msg.pos);
                if (tileEntity instanceof TileEnergy) {
                    TileEnergy storage = ((TileEnergy) tileEntity);
                    storage.getEnergyConfig().nextType(Direction.byIndex(msg.mode));
                    storage.sync();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
