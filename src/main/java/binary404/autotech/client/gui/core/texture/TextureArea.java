package binary404.autotech.client.gui.core.texture;

import binary404.autotech.AutoTech;
import binary404.autotech.client.gui.core.math.Position;
import binary404.autotech.client.gui.core.math.PositionedRect;
import binary404.autotech.client.gui.core.math.Size;
import codechicken.lib.vec.*;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TextureArea {

    public final ResourceLocation imageLocation;

    public final float offsetX;
    public final float offsetY;

    public final float imageWidth;
    public final float imageHeight;

    public TextureArea(ResourceLocation imageLocation, float offsetX, float offsetY, float width, float height) {
        this.imageLocation = imageLocation;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.imageWidth = width;
        this.imageHeight = height;
    }

    public static TextureArea fullImage(String imageLocation) {
        return new TextureArea(new ResourceLocation(AutoTech.modid, imageLocation), 0.0F, 0.0F, 1.0F, 1.0F);
    }

    public static TextureArea areaOfImage(String imageLocation, int imageSizeX, int imageSizeY, int u, int v, int width, int height) {
        return new TextureArea(new ResourceLocation(imageLocation),
                u / (imageSizeX * 1.0F),
                v / (imageSizeY * 1.0F),
                (u + width) / (imageSizeX * 1.0F),
                (v + height) / (imageSizeY * 1.0F));
    }

    public TextureArea getSubArea(float offsetX, float offsetY, float width, float height) {
        return new TextureArea(imageLocation,
                this.offsetX + (imageWidth * offsetX),
                this.offsetY + (imageHeight * offsetY),
                this.imageWidth * width,
                this.imageHeight * height);
    }

    @OnlyIn(Dist.CLIENT)
    public void drawRotated(MatrixStack stack, int x, int y, Size areaSize, PositionedRect positionedRect, int orientation) {
        Transformation transformation = createOrientation(areaSize, orientation);
        stack.push();
        stack.translate(x, y, 0.0D);
        Matrix4 matrix4 = new Matrix4(stack);
        transformation.apply(matrix4);
        draw(stack, positionedRect.position.x, positionedRect.position.y, positionedRect.size.width, positionedRect.size.height);
        GlStateManager.popMatrix();
    }

    public static Transformation createOrientation(Size areaSize, int orientation) {
        Transformation transformation = new Rotation(Math.toRadians(orientation * 90.0), 0.0, 0.0, 1.0)
                .at(new Vector3(areaSize.width / 2.0, areaSize.height / 2.0, 0.0));
        Size orientedSize = transformSize(transformation, areaSize);
        double offsetX = (areaSize.width - orientedSize.width) / 2.0;
        double offsetY = (areaSize.height - orientedSize.height) / 2.0;
        return transformation.with(new Translation(-offsetX, -offsetY, 0.0));
    }

    public static Size transformSize(Transformation transformation, Size position) {
        Vector3 sizeVector = new Vector3(position.width, position.height, 0.0);
        Vector3 zeroVector = new Vector3(0.0, 0.0, 0.0);
        transformation.apply(zeroVector);
        transformation.apply(sizeVector);
        sizeVector.subtract(zeroVector);
        return new Size((int) Math.abs(sizeVector.x), (int) Math.abs(sizeVector.y));
    }

    public static PositionedRect transformRect(Transformation transformation, PositionedRect positionedRect) {
        Position pos1 = transformPos(transformation, positionedRect.position);
        Position pos2 = transformPos(transformation, positionedRect.position.add(positionedRect.size));
        return new PositionedRect(pos1, pos2);
    }

    public static Position transformPos(Transformation transformation, Position position) {
        Vector3 vector = new Vector3(position.x, position.y, 0.0);
        transformation.apply(vector);
        return new Position((int) vector.x, (int) vector.y);
    }

    @OnlyIn(Dist.CLIENT)
    public void draw(MatrixStack matrix, float x, float y, int width, int height) {
        drawSubArea(matrix, x, y, width, height, 0.0F, 0.0F, 1.0F, 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public void drawSubArea(MatrixStack matrix, float x, float y, int width, int height, float drawnU, float drawnV, float drawnWidth, float drawnHeight) {
        //sub area is just different width and height
        float imageU = this.offsetX + (this.imageWidth * drawnU);
        float imageV = this.offsetY + (this.imageHeight * drawnV);
        float imageWidth = this.imageWidth * drawnWidth;
        float imageHeight = this.imageHeight * drawnHeight;
        Matrix4f matrix4f = matrix.getLast().getMatrix();
        Minecraft.getInstance().textureManager.bindTexture(imageLocation);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(matrix4f, x, y + height, 0.0F).tex(imageU, imageV + imageHeight).endVertex();
        bufferbuilder.pos(matrix4f, x + width, y + height, 0.0F).tex(imageU + imageWidth, imageV + imageHeight).endVertex();
        bufferbuilder.pos(matrix4f, x + width, y, 0.0F).tex(imageU + imageWidth, imageV).endVertex();
        bufferbuilder.pos(matrix4f, x, y, 0.0F).tex(imageU, imageV).endVertex();
        tessellator.draw();
    }

}
