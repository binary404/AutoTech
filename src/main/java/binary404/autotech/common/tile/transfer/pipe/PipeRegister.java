package binary404.autotech.common.tile.transfer.pipe;

import binary404.autotech.common.tile.transfer.attachment.AttachmentRegistry;
import binary404.autotech.common.tile.transfer.attachment.ExtractorAttachmentFactory;
import binary404.autotech.common.tile.transfer.attachment.ExtractorAttachmentType;
import binary404.autotech.common.tile.transfer.fluid.FluidNetworkFactory;
import binary404.autotech.common.tile.transfer.fluid.FluidPipe;
import binary404.autotech.common.tile.transfer.fluid.FluidPipeFactory;
import binary404.autotech.common.tile.transfer.fluid.FluidPipeType;
import binary404.autotech.common.tile.transfer.item.ItemNetwork;
import binary404.autotech.common.tile.transfer.item.ItemNetworkFactory;
import binary404.autotech.common.tile.transfer.item.ItemPipe;
import binary404.autotech.common.tile.transfer.item.ItemPipeFactory;
import binary404.autotech.common.tile.transfer.item.callback.ItemBounceBackTransportCallback;
import binary404.autotech.common.tile.transfer.item.callback.ItemInsertTransportCallback;
import binary404.autotech.common.tile.transfer.item.callback.ItemPipeGoneTransportCallback;
import binary404.autotech.common.tile.transfer.network.NetworkRegistry;
import binary404.autotech.common.tile.transfer.pipe.item.TransportCallbackFactoryRegistry;

public class PipeRegister {

    public static void init() {
        NetworkRegistry.INSTANCE.addFactory(ItemNetwork.TYPE, new ItemNetworkFactory());

        for (FluidPipeType pipeType : FluidPipeType.values()) {
            NetworkRegistry.INSTANCE.addFactory(pipeType.getNetworkType(), new FluidNetworkFactory(pipeType));
        }

        PipeRegistry.INSTANCE.addFactory(ItemPipe.ID, new ItemPipeFactory());
        PipeRegistry.INSTANCE.addFactory(FluidPipe.ID, new FluidPipeFactory());

        AttachmentRegistry.INSTANCE.addFactory(ExtractorAttachmentType.BASIC.getId(), new ExtractorAttachmentFactory(ExtractorAttachmentType.BASIC));

        TransportCallbackFactoryRegistry.INSTANCE.addFactory(ItemInsertTransportCallback.ID, ItemInsertTransportCallback::of);
        TransportCallbackFactoryRegistry.INSTANCE.addFactory(ItemBounceBackTransportCallback.ID, ItemBounceBackTransportCallback::of);
        TransportCallbackFactoryRegistry.INSTANCE.addFactory(ItemPipeGoneTransportCallback.ID, ItemPipeGoneTransportCallback::of);
    }

}
