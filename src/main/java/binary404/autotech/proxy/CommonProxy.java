package binary404.autotech.proxy;

import binary404.autotech.common.tile.transfer.attachment.AttachmentRegistry;
import binary404.autotech.common.tile.transfer.attachment.ExtractorAttachmentFactory;
import binary404.autotech.common.tile.transfer.attachment.ExtractorAttachmentType;
import binary404.autotech.common.tile.transfer.fluid.FluidNetworkFactory;
import binary404.autotech.common.tile.transfer.fluid.FluidPipe;
import binary404.autotech.common.tile.transfer.fluid.FluidPipeFactory;
import binary404.autotech.common.tile.transfer.fluid.FluidPipeType;
import binary404.autotech.common.tile.transfer.network.NetworkRegistry;
import binary404.autotech.common.tile.transfer.pipe.PipeRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CommonProxy implements IProxy {

    @Override
    public void registerEventHandlers() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

}
