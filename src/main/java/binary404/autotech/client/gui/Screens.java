package binary404.autotech.client.gui;

import binary404.autotech.common.container.ModContainers;
import net.minecraft.client.gui.ScreenManager;

public class Screens {

    public static void register() {
        ScreenManager.registerFactory(ModContainers.smelter, GuiSmelter::new);
    }

}
