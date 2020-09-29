package binary404.autotech;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.manager.*;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.entity.ModEntities;
import binary404.autotech.common.network.PacketHandler;
import binary404.autotech.common.tags.ModTags;
import binary404.autotech.common.world.ModFeatures;
import binary404.autotech.proxy.ClientProxy;
import binary404.autotech.proxy.CommonProxy;
import binary404.autotech.proxy.IProxy;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static binary404.autotech.common.core.manager.GrinderManager.addRecipe;
import static binary404.autotech.common.core.manager.GrinderManager.recipeExists;

@Mod("autotech")
public class AutoTech {

    public static AutoTech instance;
    public static IProxy proxy;

    public static final Logger LOGGER = LogManager.getLogger("AUTOTECH");

    public static final String modid = "autotech";

    public static ItemGroup group = new ItemGroup("autotech") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.copper_ore);
        }
    };

    public AutoTech() {
        instance = this;
        proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        proxy.registerEventHandlers();
        proxy.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::onTagsUpdate);
        MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);
    }

    private void setup(FMLCommonSetupEvent event) {
        PacketHandler.init();

        DeferredWorkQueue.runLater(() -> {
            GrinderManager.init();
            SawMillManager.init();
            CompactorManager.init();
            DistilleryManager.init();
            CentrifugeManager.init();

            ModEntities.registerAttributes();
        });
    }

    private void onTagsUpdate(TagsUpdatedEvent event) {
        GrinderManager.initTags();
        SawMillManager.initTags();
    }

    private void serverAboutToStart(FMLServerAboutToStartEvent event) {
        ModFeatures.registerFeaturesToBiomes(event.getServer());
    }

    public static ResourceLocation key(String path) {
        return new ResourceLocation("autotech", path);
    }

}
