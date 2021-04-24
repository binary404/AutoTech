package binary404.autotech.client.renders.core;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

public interface ICubeRenderer {

    void render(CCRenderState renderState, Cuboid6 bounds, IVertexOperation... ops);

}
