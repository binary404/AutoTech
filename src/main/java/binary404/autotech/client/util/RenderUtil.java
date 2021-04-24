package binary404.autotech.client.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class RenderUtil {

    public static void setGlColorFromInt(int colorValue, int opacity) {
        int i = (colorValue & 16711680) >> 16;
        int j = (colorValue & 65280) >> 8;
        int k = (colorValue & 255);
        RenderSystem.color4f(i / 255.0f, j / 255.0f, k / 255.0f, opacity / 255.0f);
    }

    public static void drawFluidForGui(FluidStack contents, int tankCapacity, int startX, int startY, int widthT, int heightT) {
        widthT--;
        heightT--;
        Fluid fluid = contents.getFluid();
        ResourceLocation fluidStill = fluid.getAttributes().getStillTexture(contents);
        TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getModelManager().getAtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).getSprite(fluidStill);
        int fluidColor = fluid.getAttributes().getColor(contents);
        int scaledAmount = contents.getAmount() * heightT / tankCapacity;
        if (contents.getAmount() > 0 && scaledAmount < 1) {
            scaledAmount = 1;
        }
        if (scaledAmount > heightT) {
            scaledAmount = heightT;
        }
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        setGlColorFromInt(fluidColor, 200);

        final int xTileCount = widthT / 16;
        final int xRemainder = widthT - xTileCount * 16;
        final int yTileCount = scaledAmount / 16;
        final int yRemainder = scaledAmount - yTileCount * 16;

        final int yStart = startY + heightT;

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int width = xTile == xTileCount ? xRemainder : 16;
                int height = yTile == yTileCount ? yRemainder : 16;
                int x = startX + xTile * 16;
                int y = yStart - (yTile + 1) * 16;
                if (width > 0 && height > 0) {
                    int maskTop = 16 - height;
                    int maskRight = 16 - width;

                    drawFluidTexture(x, y, fluidStillSprite, maskTop, maskRight, 0.0);
                }
            }
        }
        RenderSystem.popMatrix();
        RenderSystem.disableBlend();
    }

    private static void drawFluidTexture(float xCoord, float yCoord, TextureAtlasSprite textureSprite, int maskTop, int maskRight, double zLevel) {
        float uMin = textureSprite.getMinU();
        float uMax = textureSprite.getMaxU();
        float vMin = textureSprite.getMinV();
        float vMax = textureSprite.getMaxV();
        uMax = uMax - maskRight / 16.0F * (uMax - uMin);
        vMax = vMax - maskTop / 16.0F * (vMax - vMin);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(xCoord, yCoord + 16, zLevel).tex(uMin, vMax).endVertex();
        buffer.pos(xCoord + 16 - maskRight, yCoord + 16, zLevel).tex(uMax, vMax).endVertex();
        buffer.pos(xCoord + 16 - maskRight, yCoord + maskTop, zLevel).tex(uMax, vMin).endVertex();
        buffer.pos(xCoord, yCoord + maskTop, zLevel).tex(uMin, vMin).endVertex();
        tessellator.draw();
    }

}
