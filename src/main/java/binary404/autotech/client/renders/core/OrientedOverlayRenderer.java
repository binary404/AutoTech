package binary404.autotech.client.renders.core;

import binary404.autotech.AutoTech;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.texture.AtlasRegistrar;
import codechicken.lib.texture.IIconRegister;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Cuboid6;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class OrientedOverlayRenderer implements ISpriteRegister {

    public enum OverlayFace {
        FRONT, BACK, TOP, BOTTOM, SIDE;

        public static OverlayFace bySide(Direction side, Direction front) {
            if (side == front) {
                return FRONT;
            } else if (side.getOpposite() == front) {
                return BACK;
            } else if (side == Direction.UP) {
                return TOP;
            } else if (side == Direction.DOWN) {
                return BOTTOM;
            } else
                return SIDE;
        }
    }

    private final String basePath;
    private OverlayFace[] faces;

    private Map<OverlayFace, ActivePredicate> sprites;

    private static class ActivePredicate {

        private TextureAtlasSprite normalSprite;
        private TextureAtlasSprite activeSprite;

        public ActivePredicate(TextureAtlasSprite normalSprite, TextureAtlasSprite activeSprite) {
            this.normalSprite = normalSprite;
            this.activeSprite = activeSprite;
        }

        public TextureAtlasSprite getSprite(boolean active) {
            return active ? activeSprite : normalSprite;
        }
    }

    public OrientedOverlayRenderer(String basePath, OverlayFace... faces) {
        this.basePath = basePath;
        this.faces = faces;
        Textures.iconRegisters.add(this);
    }

    @Override
    public void registerIcons(AtlasRegistrar atlasRegistrar) {
        this.sprites = new HashMap<>();
        for (OverlayFace face : faces) {
            String faceName = face.name().toLowerCase();
            ResourceLocation normal = new ResourceLocation(AutoTech.modid, String.format("block/%s/overlay_%s", basePath, faceName));
            ResourceLocation active = new ResourceLocation(AutoTech.modid, String.format("block/%s/overlay_%s_active", basePath, faceName));
            atlasRegistrar.registerSprite(normal);
            atlasRegistrar.registerSprite(active);
        }
    }

    @Override
    public void populateSprites() {
        for (OverlayFace face : faces) {
            String faceName = face.name().toLowerCase();
            ResourceLocation normal = new ResourceLocation(AutoTech.modid, String.format("block/%s/overlay_%s", basePath, faceName));
            ResourceLocation active = new ResourceLocation(AutoTech.modid, String.format("block/%s/overlay_%s_active", basePath, faceName));
            sprites.put(face, new ActivePredicate(TextureUtils.getTexture(normal), TextureUtils.getTexture(active)));
        }
    }

    public void render(CCRenderState state, Cuboid6 bounds, Direction front, boolean isActive, IVertexOperation... ops) {
        for(Direction direction : Direction.values()) {
            OverlayFace face = OverlayFace.bySide(direction, front);
            if(sprites.containsKey(face)) {
                TextureAtlasSprite renderSprite = sprites.get(face).getSprite(isActive);
                Textures.renderFace(state, direction, bounds, renderSprite, ops);
            }
        }
    }

    public void render(CCRenderState state, Direction front, boolean isActive, IVertexOperation... ops) {
        render(state, Cuboid6.full, front, isActive, ops);
    }
}
