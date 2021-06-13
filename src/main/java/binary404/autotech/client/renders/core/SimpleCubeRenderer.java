package binary404.autotech.client.renders.core;

import binary404.autotech.AutoTech;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.texture.AtlasRegistrar;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Cuboid6;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import team.chisel.ctm.client.util.Dir;

public class SimpleCubeRenderer implements ICubeRenderer, ISpriteRegister {

    private final String basePath;

    private TextureAtlasSprite sprite;

    public SimpleCubeRenderer(String basePath) {
        this.basePath = basePath;
        Textures.iconRegisters.add(this);
    }

    @Override
    public void registerIcons(AtlasRegistrar atlasRegistrar) {
        atlasRegistrar.registerSprite(new ResourceLocation(AutoTech.modid, "block/" + basePath));
    }

    @Override
    public void populateSprites() {
        this.sprite = TextureUtils.getTexture(new ResourceLocation(AutoTech.modid, "block/" + basePath));
    }

    public ResourceLocation getParticleTexture() {
        return new ResourceLocation(AutoTech.modid, "block/" + basePath);
    }

    public void renderSided(Direction side, Cuboid6 bounds, CCRenderState renderState, IVertexOperation[] pipeline) {
        Textures.renderFace(renderState, side, bounds, this.sprite, pipeline);
    }

    public void renderSided(Direction side, CCRenderState renderState, IVertexOperation[] pipeline) {
        renderSided(side, Cuboid6.full, renderState, pipeline);
    }

    @Override
    public void render(CCRenderState renderState, Cuboid6 bounds, IVertexOperation... ops) {
        for (Direction direction : Direction.values()) {
            renderSided(direction, bounds, renderState, ops);
        }
    }
}
