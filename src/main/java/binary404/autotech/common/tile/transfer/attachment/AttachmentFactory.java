package binary404.autotech.common.tile.transfer.attachment;

import binary404.autotech.common.tile.transfer.pipe.Pipe;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public interface AttachmentFactory {
    Attachment createFromNbt(Pipe pipe, CompoundNBT tag);

    Attachment create(Pipe pipe, Direction dir);

    ResourceLocation getItemId();

    ResourceLocation getId();

    ResourceLocation getModelLocation();

    boolean canPlaceOnPipe(Block pipe);
}
