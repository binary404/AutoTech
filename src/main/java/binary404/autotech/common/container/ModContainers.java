package binary404.autotech.common.container;

import binary404.autotech.AutoTech;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.IContainerFactory;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = AutoTech.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainers {

    private static final List<ContainerType<?>> CONTAINER_TYPES = new ArrayList<>();
    public static final ContainerType<SmelterContainer> smelter = register("smelter", SmelterContainer::create);

    static <T extends Container> ContainerType<T> register(final String name, final IContainerFactory<T> factory) {
        final ContainerType<T> containerType = IForgeContainerType.create(factory);
        containerType.setRegistryName(name);
        CONTAINER_TYPES.add(containerType);
        return containerType;
    }

    @SubscribeEvent
    public static void onRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
        CONTAINER_TYPES.forEach(block -> event.getRegistry().register(block));
    }

}
