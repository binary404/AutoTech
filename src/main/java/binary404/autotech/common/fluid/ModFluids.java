package binary404.autotech.common.fluid;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.item.ModItems;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Function;
import java.util.function.Supplier;

import static binary404.autotech.client.util.RenderLayerHelper.setRenderLayer;
import static binary404.autotech.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = "autotech", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModFluids {

    public static ForgeFlowingFluid.Properties distilled_water_properties;
    public static BasicFlowingFluid.Flowing distilled_water_flowing;
    public static BasicFlowingFluid.Source distilled_water;

    public static ForgeFlowingFluid.Properties crude_oil_properties;
    public static BasicFlowingFluid.Flowing crude_oil_flowing;
    public static BasicFlowingFluid.Source crude_oil;

    public static ForgeFlowingFluid.Properties biomass_properties;
    public static BasicFlowingFluid.Flowing biomass_flowing;
    public static BasicFlowingFluid.Source biomass;

    @SubscribeEvent
    public static void registerFluids(RegistryEvent.Register<Fluid> event) {
        IForgeRegistry<Fluid> r = event.getRegistry();
        makeProperties();

        distilled_water = (BasicFlowingFluid.Source) register(r, new BasicFlowingFluid.Source(distilled_water_properties), "distilled_water");
        distilled_water_flowing = (BasicFlowingFluid.Flowing) register(r, new BasicFlowingFluid.Flowing(distilled_water_properties), "distilled_water_flowing");

        crude_oil = (BasicFlowingFluid.Source) register(r, new BasicFlowingFluid.Source(crude_oil_properties), "crude_oil");
        crude_oil_flowing = (BasicFlowingFluid.Flowing) register(r, new BasicFlowingFluid.Flowing(crude_oil_properties), "crude_oil_flowing");

        biomass = (BasicFlowingFluid.Source) register(r, new BasicFlowingFluid.Source(biomass_properties), "biomass");
        biomass_flowing = (BasicFlowingFluid.Flowing) register(r, new BasicFlowingFluid.Flowing(biomass_properties), "biomass_flowing");
    }

    private static void makeProperties() {
        distilled_water_properties = makeProperties("distilled_water", FluidAttributeHolder::distilledWater, () -> distilled_water, () -> distilled_water_flowing).block(() -> ModBlocks.distilled_water).bucket(() -> ModItems.distilled_water_bucket);
        crude_oil_properties = makeProperties("crude_oil", FluidAttributeHolder::crudeOil, () -> crude_oil, () -> crude_oil_flowing).block(() -> ModBlocks.crude_oil).bucket(() -> ModItems.crude_oil_bucket);
        biomass_properties = makeProperties("biomass", FluidAttributeHolder::biomass, () -> biomass, () -> biomass_flowing).block(() -> ModBlocks.biomass).bucket(() -> ModItems.biomass_bucket);
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
        setRenderLayer(distilled_water, RenderType.getTranslucent());
        setRenderLayer(distilled_water_flowing, RenderType.getTranslucent());
    }

}
