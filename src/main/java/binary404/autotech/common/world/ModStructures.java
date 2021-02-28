package binary404.autotech.common.world;

import binary404.autotech.AutoTech;
import binary404.autotech.common.world.dungeon.data.Dungeon;
import binary404.autotech.common.world.dungeon.structure.DungeonStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModStructures {

    public static Structure<DungeonStructure.Config> DUNGEON;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Structure<?>> event) {
        AutoTech.LOGGER.info("Registering Dungeon");
        DUNGEON = register(event.getRegistry(), "dungeon", new DungeonStructure(DungeonStructure.Config.CODEC));
        ModFeatures.registerStructureFeatures();
    }

    private static <T extends Structure<?>> T register(IForgeRegistry<Structure<?>> registry, String name, T structure) {
        Structure.field_236365_a_.put("name", structure);
        structure.setRegistryName(AutoTech.key(name));
        registry.register(structure);
        return structure;
    }

}
