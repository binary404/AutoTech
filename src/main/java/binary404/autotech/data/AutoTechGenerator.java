package binary404.autotech.data;

import binary404.autotech.data.tag.TagProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AutoTechGenerator {

    @SubscribeEvent
    public static void gather(GatherDataEvent event) {
        net.minecraft.data.DataGenerator gen = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        gen.addProvider(new AutoTechBlockStateProvider(gen, fileHelper));
        gen.addProvider(new RecipeProvider(gen));
        gen.addProvider(new BlockLootProvider(gen));
        gen.addProvider(new TagProvider(gen));
    }

}
