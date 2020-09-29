package binary404.autotech.common.fluid;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.item.ModItems;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.core.util.NameUtil;

import java.util.function.Function;
import java.util.function.Supplier;

import static binary404.autotech.client.util.RenderLayerHelper.setRenderLayer;
import static binary404.autotech.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = "autotech", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModFluids {

    public static ForgeFlowingFluid.Properties distilled_water;

    public static BasicFlowingFluid.Flowing distilled_water_flowing;
    public static BasicFlowingFluid.Source distilled_water_source;

    @SubscribeEvent
    public static void registerFluids(RegistryEvent.Register<Fluid> event) {
        IForgeRegistry<Fluid> r = event.getRegistry();
        makeProperties();

        distilled_water_source = (BasicFlowingFluid.Source) register(r, new BasicFlowingFluid.Source(distilled_water), "distilled_water_source");
        distilled_water_flowing = (BasicFlowingFluid.Flowing) register(r, new BasicFlowingFluid.Flowing(distilled_water), "distilled_water_flowing");
    }

    private static void makeProperties() {
        distilled_water = makeProperties("distilled_water", BasicFlowingFluid::addAttributes, () -> distilled_water_source, () -> distilled_water_flowing).block(() -> ModBlocks.distilled_water).bucket(() -> ModItems.distilled_water_bucket);
    }

    private static ForgeFlowingFluid.Properties makeProperties(String name, Function<FluidAttributes.Builder, FluidAttributes.Builder> postProcess, Supplier<ForgeFlowingFluid> stillFluidSupplier, Supplier<ForgeFlowingFluid> flowingFluidSupplier) {
        return new ForgeFlowingFluid.Properties(
                stillFluidSupplier,
                flowingFluidSupplier,
                postProcess.apply(builderFor(name)));
    }

    private static FluidAttributes.Builder builderFor(String fluidName) {
        ResourceLocation still = AutoTech.key("fluid/" + fluidName + "_still");
        ResourceLocation flowing = AutoTech.key("fluid/" + fluidName + "_flowing");
        return FluidAttributes.builder(still, flowing);
    }

    public static void initRenderLayers() {
        setRenderLayer(distilled_water_source, RenderType.getTranslucent());
        setRenderLayer(distilled_water_flowing, RenderType.getTranslucent());
    }

}
