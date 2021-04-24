package binary404.autotech.client.util;

import binary404.autotech.AutoTech;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ModRenderTypes extends RenderType {

    public static final ResourceLocation laserBeam = new ResourceLocation(AutoTech.modid, "textures/misc/laser.png");

    public ModRenderTypes(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static RenderType LASER_MAIN_BEAM = makeType("LaserMainBeam",
            DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256,
            RenderType.State.getBuilder().texture(new TextureState(laserBeam, false, false))
                    .layer(field_239235_M_)
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .lightmap(LIGHTMAP_DISABLED)
                    .writeMask(COLOR_WRITE)
                    .build(false));

    public static RenderType TILE_BLOCK = makeType("TileBlock", DefaultVertexFormats.POSITION_COLOR_TEX, 7, 2097152, true, false,
            RenderType.State.getBuilder().texture(new TextureState(AtlasTexture.LOCATION_BLOCKS_TEXTURE, false, false))
                    .lightmap(LIGHTMAP_ENABLED)
                    .shadeModel(RenderState.SHADE_ENABLED).build(true));
}
