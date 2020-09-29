package binary404.autotech.client.util;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.fluid.Fluid;

import java.util.Arrays;
import java.util.List;

public class RenderLayerHelper {

    public static void setRenderLayer(Block block, RenderType... types) {
        List<RenderType> typeList = Arrays.asList(types);
        RenderTypeLookup.setRenderLayer(block, typeList::contains);
    }

    public static void setRenderLayer(Fluid fluid, RenderType... types) {
        List<RenderType> typeList = Arrays.asList(types);
        RenderTypeLookup.setRenderLayer(fluid, typeList::contains);
    }

}
