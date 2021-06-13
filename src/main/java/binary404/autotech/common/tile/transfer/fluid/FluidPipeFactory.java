package binary404.autotech.common.tile.transfer.fluid;

import binary404.autotech.common.tile.transfer.pipe.Pipe;
import binary404.autotech.common.tile.transfer.pipe.PipeFactory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidPipeFactory implements PipeFactory {

    @Override
    public Pipe createFromNbt(World world, CompoundNBT tag) {
        BlockPos pos = BlockPos.fromLong(tag.getLong("pos"));

        FluidPipeType pipeType = FluidPipeType.values()[tag.getInt("type")];

        FluidPipe pipe = new FluidPipe(world, pos, pipeType);

        pipe.getAttachmentManager().readFromNbt(tag);

        return pipe;
    }

}
