package binary404.autotech.client.gui.core.texture;

import binary404.autotech.AutoTech;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

public class AdoptableTextureArea extends SizedTextureArea {

    private final int pixelCornerWidth;
    private final int pixelCornerHeight;

    public AdoptableTextureArea(ResourceLocation imageLocation, float offsetX, float offsetY, float width, float height, float pixelImageWidth, float pixelImageHeight, int pixelCornerWidth, int pixelCornerHeight) {
        super(imageLocation, offsetX, offsetY, width, height, pixelImageWidth, pixelImageHeight);
        this.pixelCornerWidth = pixelCornerWidth;
        this.pixelCornerHeight = pixelCornerHeight;
    }

    public static AdoptableTextureArea fullImage(String imageLocation, int imageWidth, int imageHeight, int cornerWidth, int cornerHeight) {
        return new AdoptableTextureArea(new ResourceLocation(AutoTech.modid, imageLocation), 0.0F, 0.0F, 1.0F, 1.0F, imageWidth, imageHeight, cornerWidth, cornerHeight);
    }

    @Override
    public void drawSubArea(MatrixStack matrix, float x, float y, int width, int height, float drawnU, float drawnV, float drawnWidth, float drawnHeight) {
        //compute relative sizes
        float cornerWidth = pixelCornerWidth / pixelImageWidth;
        float cornerHeight = pixelCornerHeight / pixelImageHeight;
        //draw up corners
        super.drawSubArea(matrix,x, y, pixelCornerWidth, pixelCornerHeight, 0.0F, 0.0F, cornerWidth, cornerHeight);
        super.drawSubArea(matrix,x + width - pixelCornerWidth, y, pixelCornerWidth, pixelCornerHeight, 1.0F - cornerWidth, 0.0F, cornerWidth, cornerHeight);
        //draw down corners
        super.drawSubArea(matrix, x, y + height - pixelCornerHeight, pixelCornerWidth, pixelCornerHeight, 0.0F, 1.0F - cornerHeight, cornerWidth, cornerHeight);
        super.drawSubArea(matrix,x + width - pixelCornerWidth, y + height - pixelCornerHeight, pixelCornerWidth, pixelCornerHeight, 1.0F - cornerWidth, 1.0F - cornerHeight, cornerWidth, cornerHeight);
        //draw horizontal connections
        super.drawSubArea(matrix,x + pixelCornerWidth, y, width - 2 * pixelCornerWidth, pixelCornerHeight,
                cornerWidth, 0.0F, 1.0F - 2 * cornerWidth, cornerHeight);
        super.drawSubArea(matrix,x + pixelCornerWidth, y + height - pixelCornerHeight, width - 2 * pixelCornerWidth, pixelCornerHeight,
                cornerWidth, 1.0F - cornerHeight, 1.0F - 2 * cornerWidth, cornerHeight);
        //draw vertical connections
        super.drawSubArea(matrix,x, y + pixelCornerHeight, pixelCornerWidth, height - 2 * pixelCornerHeight,
                0.0F, cornerHeight, cornerWidth, 1.0F - 2 * cornerHeight);
        super.drawSubArea(matrix, x + width - pixelCornerWidth, y + pixelCornerHeight, pixelCornerWidth, height - 2 * pixelCornerHeight,
                1.0F - cornerWidth, cornerHeight, cornerWidth, 1.0F - 2 * cornerHeight);
        //draw central body
        super.drawSubArea(matrix,x + pixelCornerWidth, y + pixelCornerHeight,
                width - 2 * pixelCornerWidth, height - 2 * pixelCornerHeight,
                cornerWidth, cornerHeight, 1.0F - 2 * cornerWidth, 1.0F - 2 * cornerHeight);
    }

}
