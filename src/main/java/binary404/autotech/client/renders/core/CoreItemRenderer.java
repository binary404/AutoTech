package binary404.autotech.client.renders.core;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.LazyValue;
import net.minecraftforge.common.util.LazyOptional;

import java.util.concurrent.Callable;

public class CoreItemRenderer extends ItemTileEntityRenderer implements Callable<CoreItemRenderer> {

    public CoreItemRenderer(LazyValue<TileEntity> optional) {
        super(optional);
    }

    @Override
    public CoreItemRenderer call() throws Exception {
        return this;
    }
}
