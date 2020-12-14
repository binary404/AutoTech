package binary404.autotech.proxy;

import binary404.autotech.client.gui.Screens;

import binary404.autotech.client.renders.RenderDisplayStand;
import binary404.autotech.client.renders.RenderEmpowerer;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.JumpTestHandler;
import binary404.autotech.common.entity.ModEntities;
import binary404.autotech.common.fluid.ModFluids;
import binary404.autotech.common.tile.ModTiles;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy implements IProxy {

    @Override
    public void registerEventHandlers() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        ModBlocks.initRenderLayers();
        ModFluids.initRenderLayers();
        JumpTestHandler.init();
        ClientRegistry.bindTileEntityRenderer(ModTiles.display_stand, RenderDisplayStand::new);
        ClientRegistry.bindTileEntityRenderer(ModTiles.empowerer, RenderEmpowerer::new);
    }

    @Override
    public void init() {
        Screens.register();
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
