package binary404.autotech.common.container;

import binary404.autotech.AutoTech;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.container.device.ContainerWaterPump;
import binary404.autotech.common.container.generator.BioGeneratorContainer;
import binary404.autotech.common.container.machine.*;
import binary404.autotech.common.container.multiblock.ContainerBlastFurnace;
import net.minecraft.inventory.container.BlastFurnaceContainer;
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
    public static final ContainerType<GrinderContainer> grinder = register("grinder", GrinderContainer::create);
    public static final ContainerType<BioGeneratorContainer> bio_generator = register("bio_generator", BioGeneratorContainer::create);
    public static final ContainerType<SawMillContainer> sawmill = register("sawmill", SawMillContainer::new);
    public static final ContainerType<ContainerWaterPump> waterpump = register("waterpump", ContainerWaterPump::new);
    public static final ContainerType<CompactorContainer> compactor = register("compactor", CompactorContainer::new);
    public static final ContainerType<CentrifugeContainer> centrifuge = register("centrifuge", CentrifugeContainer::new);
    public static final ContainerType<ContainerDistillery> distillery = register("distillery", ContainerDistillery::new);
    public static final ContainerType<AssemblerContainer> assembler = register("assembler", AssemblerContainer::new);
    public static final ContainerType<ContainerBlastFurnace> blast_furnace = register("blast_furnace", ContainerBlastFurnace::new);

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
