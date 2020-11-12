package binary404.autotech.client.gui;

import binary404.autotech.client.gui.machine.*;
import binary404.autotech.client.gui.multiblock.GuiBlastFurnace;
import binary404.autotech.common.container.ModContainers;
import javafx.stage.Screen;
import net.minecraft.client.gui.ScreenManager;

public class Screens {

    public static void register() {
        ScreenManager.registerFactory(ModContainers.smelter, GuiSmelter::new);
        ScreenManager.registerFactory(ModContainers.bio_generator, GuiBioGenerator::new);
        ScreenManager.registerFactory(ModContainers.grinder, GuiGrinder::new);
        ScreenManager.registerFactory(ModContainers.sawmill, GuiSawMill::new);
        ScreenManager.registerFactory(ModContainers.waterpump, GuiWaterPump::new);
        ScreenManager.registerFactory(ModContainers.compactor, GuiCompactor::new);
        ScreenManager.registerFactory(ModContainers.centrifuge, GuiCentrifuge::new);
        ScreenManager.registerFactory(ModContainers.distillery, GuiDistillery::new);
        ScreenManager.registerFactory(ModContainers.assembler, GuiAssembler::new);
        ScreenManager.registerFactory(ModContainers.blast_furnace, GuiBlastFurnace::new);
    }

}
