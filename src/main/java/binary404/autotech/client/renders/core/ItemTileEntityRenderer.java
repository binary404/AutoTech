package binary404.autotech.client.renders.core;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.block.machine.BlockMachine;
import com.google.common.base.Preconditions;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.LazyValue;
import net.minecraft.util.registry.Registry;

public class ItemTileEntityRenderer extends ItemStackTileEntityRenderer {

    private final LazyValue<TileEntity> dummy;

    public ItemTileEntityRenderer(LazyValue<TileEntity> dummy) {
        this.dummy = dummy;
    }

    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (Block.getBlockFromItem(stack.getItem()) instanceof BlockTile) {
            TileEntityRenderer r = TileEntityRendererDispatcher.instance.getRenderer(dummy.getValue());
            if (r != null)
                r.render(dummy.getValue(), 0, matrixStack, buffer, combinedLight, combinedOverlay);
        }
    }
}
