package binary404.autotech.common.network;

import binary404.autotech.common.tile.core.TileCore;
import binary404.autotech.common.tile.util.IRedstoneInteract;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRedstoneChange {

    private BlockPos pos;

    public PacketRedstoneChange(BlockPos pos) {
        this.pos = pos;
    }

    public PacketRedstoneChange() {
        this(BlockPos.ZERO);
    }

    public static void encode(PacketRedstoneChange msg, PacketBuffer buffer) {
        buffer.writeBlockPos(msg.pos);
    }

    public static PacketRedstoneChange decode(PacketBuffer buffer) {
        return new PacketRedstoneChange(buffer.readBlockPos());
    }

    public static void handle(PacketRedstoneChange msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                TileEntity tileEntity = player.world.getTileEntity(msg.pos);
                if (tileEntity instanceof TileCore) {
                    if (tileEntity instanceof IRedstoneInteract) {
                        ((IRedstoneInteract) tileEntity).nextRedstoneMode();
                        ((TileCore) tileEntity).sync();
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
