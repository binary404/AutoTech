package binary404.autotech;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.GrinderManager;
import binary404.autotech.common.network.PacketHandler;
import binary404.autotech.common.world.ModFeatures;
import binary404.autotech.proxy.ClientProxy;
import binary404.autotech.proxy.CommonProxy;
import binary404.autotech.proxy.IProxy;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
    }

    private void setup(FMLCommonSetupEvent event) {
        ModFeatures.registerFeaturesToBiome();

        PacketHandler.init();

        GrinderManager.init();

        DeferredWorkQueue.runLater(() -> {
        });
    }

    public static ResourceLocation key(String path) {
        return new ResourceLocation("autotech", path);
    }

}
