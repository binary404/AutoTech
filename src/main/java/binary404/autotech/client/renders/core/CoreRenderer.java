package binary404.autotech.client.renders.core;

import binary404.autotech.common.tile.core.TileCore;
import codechicken.lib.colour.Colour;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Vector3;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntityMerger;

public class CoreRenderer extends TileEntityRenderer<TileCore> {

    public CoreRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileCore tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Matrix4 mat = new Matrix4(matrixStackIn);
        CCRenderState ccrs = CCRenderState.instance();
        ccrs.reset();

        ccrs.brightness = combinedLightIn;
        ccrs.overlay = combinedOverlayIn;
        ccrs.baseColour = 0xFFFFFFF0;

        ccrs.bind(Atlases.getCutoutBlockType(), bufferIn);

        Matrix4 coreMat = mat.copy();
        tileEntityIn.renderTileEntity(ccrs, coreMat);
    }
}
