package binary404.autotech.client.renders;

import binary404.autotech.client.util.ClientUtil;
import binary404.autotech.common.tile.machine.TileEmpowerer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class RenderEmpowerer extends TileEntityRenderer<TileEmpowerer> {

    public RenderEmpowerer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEmpowerer tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0.5, 0.9, 0.5);
        float ticks = Minecraft.getInstance().getRenderViewEntity().ticksExisted + partialTicks;
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(ticks % 360.0F));
        ItemStack stack = tileEntityIn.getInventory().getStackInSlot(0);
        int lightCoords = 15728881 / 65536;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtil.renderItemInWorld(stack, matrixStackIn, lightCoords, combinedOverlayIn, bufferIn);
        matrixStackIn.pop();
    }
}
