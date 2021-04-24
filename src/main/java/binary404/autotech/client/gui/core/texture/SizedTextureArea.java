package binary404.autotech.client.gui.core.texture;

import binary404.autotech.AutoTech;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

public class SizedTextureArea extends TextureArea {

    public final float pixelImageWidth;
    public final float pixelImageHeight;

    public SizedTextureArea(ResourceLocation imageLocation, float offsetX, float offsetY, float width, float height, float pixelImageWidth, float pixelImageHeight) {
        super(imageLocation, offsetX, offsetY, width, height);
        this.pixelImageWidth = pixelImageWidth;
        this.pixelImageHeight = pixelImageHeight;
    }

    @Override
    public SizedTextureArea getSubArea(float offsetX, float offsetY, float width, float height) {
        return new SizedTextureArea(imageLocation,
                this.offsetX + (imageWidth * offsetX),
                this.offsetY + (imageHeight * offsetY),
                this.imageWidth * width,
                this.imageHeight * height,
                this.pixelImageWidth * width,
                this.pixelImageHeight * height);
    }

    public static SizedTextureArea fullImage(String imageLocation, int imageWidth, int imageHeight) {
        return new SizedTextureArea(new ResourceLocation(AutoTech.modid, imageLocation), 0.0F, 0.0F, 1.0F, 1.0F, imageWidth, imageHeight);
    }

    public void drawHorizontalCutArea(MatrixStack matrix, int x, int y, int width, int height) {
        drawHorizontalCutSubArea(matrix, x, y, width, height, 0.0F, 1.0F);
    }

    public void drawVerticalCutArea(MatrixStack matrix, int x, int y, int width, int height) {
        drawVerticalCutSubArea(matrix, x, y, width, height, 0.0F, 1.0F);
    }

    public void drawHorizontalCutSubArea(MatrixStack stack, int x, int y, int width, int height, float drawnV, float drawnHeight) {
        float drawnWidth = width / 2.0F / pixelImageWidth;
        drawSubArea(stack, x, y, width / 2, height, 0.0F, drawnV, drawnWidth, drawnHeight);
        drawSubArea(stack,x + width / 2, y, width / 2, height, 1.0F - drawnWidth, drawnV, drawnWidth, drawnHeight);
    }

    public void drawVerticalCutSubArea(MatrixStack matrix, int x, int y, int width, int height, float drawnU, float drawnWidth) {
        float drawnHeight = height / 2.0F / pixelImageHeight;
        drawSubArea(matrix, x, y, width, height / 2, drawnU, 0.0F, drawnWidth, drawnHeight);
        drawSubArea(matrix, x, y + height / 2, width, height / 2, drawnU, 1.0F - drawnHeight, drawnWidth, drawnHeight);
    }

}
