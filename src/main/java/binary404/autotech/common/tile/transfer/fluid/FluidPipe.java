package binary404.autotech.common.tile.transfer.fluid;

import binary404.autotech.AutoTech;
import binary404.autotech.common.network.PacketHandler;
import binary404.autotech.common.network.pipe.FluidPipeMessage;
import binary404.autotech.common.tile.transfer.pipe.Pipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidPipe extends Pipe {

    public static final ResourceLocation ID = new ResourceLocation(AutoTech.modid, "fluid");

    private final FluidPipeType type;
    private float lastFullness = 0;

    public FluidPipe(World world, BlockPos pos, FluidPipeType type) {
        super(world, pos);

        this.type = type;
    }

    @Override
    public void update() {
        super.update();

        float f = getFullness();
        if (Math.abs(lastFullness - f) >= 0.1) {
            lastFullness = f;

            sendFluidPipeUpdate();
        }
    }

    public void sendFluidPipeUpdate() {
        PacketHandler.sendInArea(world, pos, 32, new FluidPipeMessage(pos, ((FluidNetwork) network).getFluidTank().getFluid(), getFullness()));
    }

    public float getFullness() {
        int cap = ((FluidNetwork) network).getFluidTank().getCapacity();
        int stored = ((FluidNetwork) network).getFluidTank().getFluidAmount();

        return Math.round(((float) stored / (float) cap) * 10.0F) / 10.0F;
    }

    public FluidPipeType getType() {
        return type;
    }

    @Override
    public CompoundNBT writeToNbt(CompoundNBT tag) {
        tag = super.writeToNbt(tag);

        tag.putInt("type", type.ordinal());

        return tag;
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public ResourceLocation getNetworkType() {
        return type.getNetworkType();
    }

}
