package binary404.autotech.common.network.pipe;

import binary404.autotech.common.tile.transfer.fluid.TileFluidPipe;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class FluidPipeMessage {

    private BlockPos pos;
    private FluidStack fluid;
    private float fullness;

    public FluidPipeMessage(BlockPos pos, FluidStack fluid, float fullness) {
        this.pos = pos;
        this.fluid = fluid;
        this.fullness = fullness;
    }

    public FluidPipeMessage() {
    }

    public static void encode(FluidPipeMessage message, PacketBuffer buf) {
        buf.writeBlockPos(message.pos);
        buf.writeFluidStack(message.fluid);
        buf.writeFloat(message.fullness);
    }

    public static FluidPipeMessage decode(PacketBuffer buf) {
        BlockPos pos = buf.readBlockPos();
        FluidStack fluid = buf.readFluidStack();
        float fullness = buf.readFloat();

        return new FluidPipeMessage(pos, fluid, fullness);
    }

    public static void handle(FluidPipeMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TileEntity tile = Minecraft.getInstance().world.getTileEntity(message.pos);

            if (tile instanceof TileFluidPipe) {
                ((TileFluidPipe) tile).setFluid(message.fluid);
                ((TileFluidPipe) tile).setFullness(message.fullness);
            }
        });

        ctx.get().setPacketHandled(true);
    }

}
