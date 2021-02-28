package binary404.autotech;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.manager.*;
import binary404.autotech.common.entity.ModEntities;
import binary404.autotech.common.network.PacketHandler;
import binary404.autotech.common.world.ModFeatures;
import binary404.autotech.proxy.ClientProxy;
import binary404.autotech.proxy.CommonProxy;
import binary404.autotech.proxy.IProxy;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, ModFeatures::onBiomeLoad);
    }

    private void setup(FMLCommonSetupEvent event) {
        PacketHandler.init();

        DeferredWorkQueue.runLater(() -> {
            GrinderManager.init();
            SawMillManager.init();
            CompactorManager.init();
            DistilleryManager.init();
            CentrifugeManager.init();
            AssemblerManager.init();
            EmpowererManager.init();

            ModEntities.registerAttributes();
        });
    }

    private void onTagsUpdate(TagsUpdatedEvent event) {
        GrinderManager.initTags();
        SawMillManager.initTags();
        ArcFurnaceManager.initTags();
    }

    public static String sId(String name) {
        return modid + ":" + name;
    }

    public static ResourceLocation key(String path) {
        return new ResourceLocation("autotech", path);
    }

}
