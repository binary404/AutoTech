package binary404.autotech.common.tile.transfer.pipe;

import binary404.autotech.common.tile.transfer.attachment.AttachmentRegistry;
import binary404.autotech.common.tile.transfer.attachment.ExtractorAttachmentFactory;
import binary404.autotech.common.tile.transfer.attachment.ExtractorAttachmentType;
import binary404.autotech.common.tile.transfer.fluid.FluidNetworkFactory;
import binary404.autotech.common.tile.transfer.fluid.FluidPipe;
import binary404.autotech.common.tile.transfer.fluid.FluidPipeFactory;
import binary404.autotech.common.tile.transfer.fluid.FluidPipeType;
import binary404.autotech.common.tile.transfer.network.NetworkRegistry;

public class PipeRegister {

    public static void init() {
        for (FluidPipeType pipeType : FluidPipeType.values()) {
            NetworkRegistry.INSTANCE.addFactory(pipeType.getNetworkType(), new FluidNetworkFactory(pipeType));
        }

        PipeRegistry.INSTANCE.addFactory(FluidPipe.ID, new FluidPipeFactory());

        AttachmentRegistry.INSTANCE.addFactory(ExtractorAttachmentType.BASIC.getId(), new ExtractorAttachmentFactory(ExtractorAttachmentType.BASIC));
    }

}
