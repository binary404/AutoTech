package binary404.autotech.client.renders.core;

import binary404.autotech.common.core.logistics.Tier;
import codechicken.lib.render.BlockRenderer;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.texture.AtlasRegistrar;
import codechicken.lib.texture.IIconRegister;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.uv.IconTransformation;
import codechicken.lib.vec.uv.UVTransformation;
import codechicken.lib.vec.uv.UVTransformationList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import static binary404.autotech.client.renders.core.OrientedOverlayRenderer.OverlayFace.*;

public class Textures {

    private static final ThreadLocal<BlockRenderer.BlockFace> blockFaces = ThreadLocal.withInitial(BlockRenderer.BlockFace::new);

    public static List<ISpriteRegister> iconRegisters = new ArrayList<>();

    public static OrientedOverlayRenderer MULTIBLOCK_WORKABLE_OVERLAY = new OrientedOverlayRenderer("machines/multiblock_workable", FRONT);

    public static SimpleCubeRenderer STAINLESS_STEEL_CASING = new SimpleCubeRenderer("casings/solid/machine_casing_stainless_steel");

    public static OrientedOverlayRenderer GRINDER = new OrientedOverlayRenderer("machines/grinder", FRONT, TOP);

    public static SimpleSidedCubeRenderer[] CASINGS = new SimpleSidedCubeRenderer[Tier.values().length];

    static {
        for (int i = 0; i < CASINGS.length; i++) {
            String tierName = Tier.values()[i].name().toLowerCase();
            CASINGS[i] = new SimpleSidedCubeRenderer("casings/" + tierName);
        }
    }

    public static void register(AtlasRegistrar textureMap) {
        for (IIconRegister iconRegister : iconRegisters) {
            iconRegister.registerIcons(textureMap);
        }
    }

    public static void populateSprites() {
        for (ISpriteRegister spriteRegister : iconRegisters) {
            spriteRegister.populateSprites();
        }
    }

    public static void renderFace(CCRenderState renderState, Direction direction, Cuboid6 bounds, TextureAtlasSprite sprite, IVertexOperation... ops) {
        BlockRenderer.BlockFace blockFace = new BlockRenderer.BlockFace();
        blockFace.loadCuboidFace(bounds, direction.getIndex());
        UVTransformation uvList = new UVTransformationList(new IconTransformation(sprite));
        renderState.setPipeline(blockFace, 0, blockFace.verts.length, ArrayUtils.addAll(ops, uvList));
        renderState.render();
    }

}
