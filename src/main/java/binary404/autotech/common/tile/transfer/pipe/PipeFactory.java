package binary404.autotech.common.tile.transfer.pipe;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public interface PipeFactory {
    Pipe createFromNbt(World world, CompoundNBT tag);
}
