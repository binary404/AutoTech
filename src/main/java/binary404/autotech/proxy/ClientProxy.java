package binary404.autotech.proxy;

import binary404.autotech.client.gui.Screens;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.entity.ModEntities;
import binary404.autotech.common.fluid.ModFluids;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
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
