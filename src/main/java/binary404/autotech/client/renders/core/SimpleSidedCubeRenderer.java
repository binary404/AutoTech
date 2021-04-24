package binary404.autotech.client.renders.core;

import binary404.autotech.AutoTech;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.texture.AtlasRegistrar;
import codechicken.lib.texture.IIconRegister;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

import java.util.HashMap;
import java.util.Map;

public class SimpleSidedCubeRenderer implements ICubeRenderer, ISpriteRegister {

    public enum RenderSide {
        TOP, BOTTOM, SIDE;

        public static RenderSide bySide(Direction side) {
            if (side == Direction.UP) {
                return TOP;
            } else if (side == Direction.DOWN) {
                return BOTTOM;
            } else
                return SIDE;
        }
    }

    protected final String basePath;

    protected Map<RenderSide, TextureAtlasSprite> sprites;

    public SimpleSidedCubeRenderer(String basePath) {
        this.basePath = basePath;
        Textures.iconRegisters.add(this);
    }

    @Override
    public void registerIcons(AtlasRegistrar atlasRegistrar) {
        this.sprites = new HashMap<>();
        for (RenderSide overlayFace : RenderSide.values()) {
            String faceName = overlayFace.name().toLowerCase();
            ResourceLocation resourceLocation = new ResourceLocation(AutoTech.modid, String.format("block/%s/%s", basePath, faceName));
            atlasRegistrar.registerSprite(resourceLocation);
        }
    }

    public void populateSprites() {
        for (RenderSide overlayFace : RenderSide.values()) {
            String faceName = overlayFace.name().toLowerCase();
            ResourceLocation resourceLocation = new ResourceLocation(AutoTech.modid, String.format("block/%s/%s", basePath, faceName));
            sprites.put(overlayFace, TextureUtils.getTexture(resourceLocation));
        }
    }

    public TextureAtlasSprite getSpriteOnSide(RenderSide renderSide) {
        return sprites.get(renderSide);
    }

    @Override
    public void render(CCRenderState renderState, Cuboid6 bounds, IVertexOperation... ops) {
        for (Direction renderSide : Direction.values()) {
            RenderSide overlayFace = RenderSide.bySide(renderSide);
            TextureAtlasSprite renderSprite = sprites.get(overlayFace);
            Textures.renderFace(renderState, renderSide, bounds, renderSprite, ops);
        }
    }
}
