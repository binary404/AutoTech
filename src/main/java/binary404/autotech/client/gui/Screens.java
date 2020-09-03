package binary404.autotech.client.gui;

import binary404.autotech.common.container.ModContainers;
import net.minecraft.client.gui.ScreenManager;

public class Screens {

    public static void register() {
        ScreenManager.registerFactory(ModContainers.smelter, GuiSmelter::new);
        ScreenManager.registerFactory(ModContainers.bio_generator, GuiBioGenerator::new);
        ScreenManager.registerFactory(ModContainers.grinder, GuiGrinder::new);
        ScreenManager.registerFactory(ModContainers.sawmill, GuiSawMill::new);
        ScreenManager.registerFactory(ModContainers.waterpump, GuiWaterPump::new);
    }

}
