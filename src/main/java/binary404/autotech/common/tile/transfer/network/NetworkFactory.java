package binary404.autotech.common.tile.transfer.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public interface NetworkFactory {
    Network create(BlockPos pos);

    Network create(CompoundNBT tag);
}
