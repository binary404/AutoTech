package binary404.autotech.client.renders.core;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.block.machine.BlockMachine;
import binary404.autotech.common.tile.core.TileCore;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.block.BlockRenderingRegistry;
import codechicken.lib.render.block.ICCBlockRenderer;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.TransformUtils;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Random;

public class TileCoreRenderer implements ICCBlockRenderer, IItemRenderer {

    public static ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation(new ResourceLocation(AutoTech.modid, "machine"), "");

    public static TileCoreRenderer INSTANCE = new TileCoreRenderer();

    public static void init() {
        BlockRenderingRegistry.registerRenderer(INSTANCE);
        FMLJavaModLoadingContext.get().getModEventBus().register(INSTANCE);
    }

    @SubscribeEvent
    public void onModelsBake(ModelBakeEvent event) {
        for (BlockMachine machine : BlockMachine.machines) {
            ModelResourceLocation model = new ModelResourceLocation(machine.getRegistryName(), "");
            event.getModelRegistry().put(model, this);
        }
    }

    @Override
    public void renderItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, int i1) {

    }

    @Override
    public boolean renderBlock(BlockState blockState, BlockPos blockPos, IBlockDisplayReader iBlockDisplayReader, MatrixStack matrixStack, IVertexBuilder iVertexBuilder, Random random, IModelData iModelData) {
        TileEntity tileEntity = iBlockDisplayReader.getTileEntity(blockPos);
        if (!(tileEntity instanceof TileCore))
            return false;
        CCRenderState renderState = CCRenderState.instance();
        renderState.reset();
        renderState.bind(iVertexBuilder, DefaultVertexFormats.BLOCK);
        Matrix4 translation = new Matrix4().translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        RenderType type = MinecraftForgeClient.getRenderLayer();

        renderState.lightMatrix.locate(iBlockDisplayReader, blockPos);
        IVertexOperation[] pipeline = new IVertexOperation[]{renderState.lightMatrix};

        return true;
    }

    @Override
    public boolean canHandleBlock(IBlockDisplayReader iBlockDisplayReader, BlockPos blockPos, BlockState blockState) {
        return blockState.getBlock() instanceof BlockTile;
    }

    @Override
    public IModelTransform getModelTransform() {
        return TransformUtils.DEFAULT_BLOCK;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        //TODO
        return TextureUtils.getMissingSprite();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }
}
