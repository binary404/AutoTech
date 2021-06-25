package binary404.autotech.common.tile.transfer.pipe.item;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

public interface TransportCallbackFactory {
    @Nullable
    TransportCallback create(CompoundNBT tag);
}
