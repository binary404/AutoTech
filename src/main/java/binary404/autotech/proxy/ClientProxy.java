package binary404.autotech.proxy;

import binary404.autotech.AutoTech;
import binary404.autotech.client.gui.Screens;
import binary404.autotech.client.render.ItemPipeRenderer;
import binary404.autotech.client.renders.core.CoreRenderer;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.JumpTestHandler;
import binary404.autotech.common.fluid.ModFluids;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.transfer.attachment.AttachmentFactory;
import binary404.autotech.common.tile.transfer.attachment.AttachmentRegistry;
import binary404.autotech.common.tile.transfer.pipe.PipeModelHandler;
import codechicken.lib.texture.SpriteRegistryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy implements IProxy {

    private static SpriteRegistryHelper helper = new SpriteRegistryHelper();

    @Override
    public void registerEventHandlers() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::populateSprites);
        helper.addIIconRegister(Textures::register);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        ModBlocks.initRenderLayers();
        ModFluids.initRenderLayers();
        JumpTestHandler.init();
        ClientRegistry.bindTileEntityRenderer(ModTiles.simple_machine, CoreRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTiles.simple_generator, CoreRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTiles.simple_furnace, CoreRenderer::new);

        ClientRegistry.bindTileEntityRenderer(ModTiles.fluid_hatch, CoreRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTiles.item_hatch, CoreRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTiles.energy_hatch, CoreRenderer::new);

        ClientRegistry.bindTileEntityRenderer(ModTiles.distillation_tower, CoreRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTiles.blast_furnace, CoreRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTiles.large_boiler, CoreRenderer::new);

        ClientRegistry.bindTileEntityRenderer(ModTiles.basic_item_pipe, ItemPipeRenderer::new);
    }

    private void populateSprites(ModelBakeEvent event) {
        Textures.populateSprites();
    }

    @Override
    public void init() {
        Screens.register();
        PipeModelHandler.init();
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

}
