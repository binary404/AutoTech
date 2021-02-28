package binary404.autotech.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.royawesome.jlibnoise.module.combiner.Min;

public class ClientUtil {

    public static void renderItemInWorld(ItemStack stack, MatrixStack matrix, int light, int overlay, IRenderTypeBuffer buffer) {
        RenderSystem.disableLighting();
        RenderHelper.enableStandardItemLighting();
        Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, light, overlay, matrix, buffer);
        RenderHelper.disableStandardItemLighting();
        RenderSystem.enableLighting();
    }

    public static void drawStringWithBorder(MatrixStack stack, String text, float x, float y, int color, int borderColor) {
        Minecraft mc = Minecraft.getInstance();

        mc.fontRenderer.drawString(stack, text, x - 1, y, borderColor);
        mc.fontRenderer.drawString(stack, text, x + 1, y, borderColor);
        mc.fontRenderer.drawString(stack, text, x, y - 1, borderColor);
        mc.fontRenderer.drawString(stack, text, x, y + 1, borderColor);
        mc.fontRenderer.drawString(stack, text, x, y, color);
    }

}
