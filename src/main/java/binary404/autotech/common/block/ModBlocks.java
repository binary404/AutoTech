package binary404.autotech.common.block;

import binary404.autotech.AutoTech;
import binary404.autotech.client.renders.core.CoreItemRenderer;
import binary404.autotech.client.renders.core.GeneratorItemRenderer;
import binary404.autotech.client.renders.core.MachineItemRenderer;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.block.generator.BlockGenerator;
import binary404.autotech.common.block.machine.BlockFurnace;
import binary404.autotech.common.block.machine.BlockMachine;
import binary404.autotech.common.block.misc.BlockBoilerCasing;
import binary404.autotech.common.block.misc.BlockCasing;
import binary404.autotech.common.block.misc.BlockCoil;
import binary404.autotech.common.block.multiblock.*;
import binary404.autotech.common.block.transfer.BlockFluidPipe;
import binary404.autotech.common.block.transfer.CableBlock;
import binary404.autotech.common.block.transfer.shape.PipeShapeCache;
import binary404.autotech.common.block.transfer.shape.PipeShapeFactory;
import binary404.autotech.common.block.world.BlockAutoTechOre;
import binary404.autotech.common.block.world.BlockRubberSapling;
import binary404.autotech.common.block.world.BlockRubberWood;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.recipe.RecipeMaps;
import binary404.autotech.common.fluid.BlockBasicFlowingFluid;
import binary404.autotech.common.fluid.ModFluids;
import binary404.autotech.common.item.ModItems;
import binary404.autotech.common.tile.multiblock.TileLargeBoiler;
import binary404.autotech.common.tile.transfer.fluid.FluidPipeType;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.LazyValue;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import static binary404.autotech.client.util.RenderLayerHelper.setRenderLayer;
import static binary404.autotech.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = AutoTech.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    private static final PipeShapeCache pipeShapeCache = new PipeShapeCache(new PipeShapeFactory());

    //Modded Ores
    public static Block copper_ore;
    public static Block tin_ore;
    public static Block lead_ore;
    public static Block silver_ore;
    public static Block uranium_ore;
    public static Block nickel_ore;
    public static Block platinum_ore;
    public static Block titanium_ore;

    //Vanilla ore replacements
    public static Block iron_ore;
    public static Block gold_ore;
    public static Block redstone_ore;
    public static Block diamond_ore;
    public static Block coal_ore;

    //Fluids
    public static BlockBasicFlowingFluid steam;
    public static BlockBasicFlowingFluid distilled_water;
    public static BlockBasicFlowingFluid crude_oil;
    public static BlockBasicFlowingFluid biomass;

    //Machines
    public static Block lv_grinder;

    public static Block mv_brewer;

    public static Block distillation_tower;
    public static Block electric_blast_furnace;
    public static Block bronze_large_boiler;

    public static Block lv_steam_turbine;

    public static Block lv_furnace;

    //Multiblock i/o
    public static Block lv_item_input_hatch;
    public static Block lv_item_output_hatch;
    public static Block lv_fluid_input_hatch;
    public static Block lv_fluid_output_hatch;
    public static Block lv_energy_input_hatch;
    public static Block lv_energy_output_hatch;

    public static Block mv_energy_input_hatch;

    //Cables
    public static Block lv_cable;
    public static Block mv_cable;
    public static Block hv_cable;
    public static Block ev_cable;

    //Fluid Pipes
    public static Block bronze_pipe;

    //Misc blocks
    public static Block heat_proof_casing;
    public static Block stainless_steel_casing;
    public static Block bronze_bricks;
    public static Block bronze_boiler_casing;

    public static Block cupronickel_coil;

    public static Block reinforced_glass;

    //Rubber
    public static Block rubber_log;
    public static Block rubber_sapling;
    public static Block rubber_leaves;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> r = event.getRegistry();
        AbstractBlock.Properties p = AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().harvestLevel(1).harvestTool(ToolType.PICKAXE).hardnessAndResistance(3.0f, 3.0f);

        copper_ore = register(r, new BlockAutoTechOre(p), "copper_ore");
        tin_ore = register(r, new BlockAutoTechOre(p), "tin_ore");
        iron_ore = register(r, new BlockAutoTechOre(p), "iron_ore");
        coal_ore = register(r, new BlockAutoTechOre(p), "coal_ore");
        p.harvestLevel(2);
        lead_ore = register(r, new BlockAutoTechOre(p), "lead_ore");
        silver_ore = register(r, new BlockAutoTechOre(p), "silver_ore");
        uranium_ore = register(r, new BlockAutoTechOre(p), "uranium_ore");
        nickel_ore = register(r, new BlockAutoTechOre(p), "nickel_ore");
        gold_ore = register(r, new BlockAutoTechOre(p), "gold_ore");
        redstone_ore = register(r, new BlockAutoTechOre(p), "redstone_ore");
        p.harvestLevel(3);
        platinum_ore = register(r, new BlockAutoTechOre(p), "platinum_ore");
        titanium_ore = register(r, new BlockAutoTechOre(p), "titanium_ore");
        diamond_ore = register(r, new BlockAutoTechOre(p), "diamond_ore");

        steam = (BlockBasicFlowingFluid) register(r, new BlockBasicFlowingFluid(() -> ModFluids.steam), "steam");
        distilled_water = (BlockBasicFlowingFluid) register(r, new BlockBasicFlowingFluid(() -> ModFluids.distilled_water), "distilled_water");
        crude_oil = (BlockBasicFlowingFluid) register(r, new BlockBasicFlowingFluid(() -> ModFluids.crude_oil), "crude_oil");
        biomass = (BlockBasicFlowingFluid) register(r, new BlockBasicFlowingFluid(() -> ModFluids.biomass), "biomass");

        heat_proof_casing = register(r, new BlockCasing(p), "heat_proof_casing");
        stainless_steel_casing = register(r, new BlockCasing(p), "stainless_steel_casing");
        bronze_bricks = register(r, new BlockCasing(p), "bronze_bricks");
        bronze_boiler_casing = register(r, new BlockBoilerCasing(), "bronze_boiler_casing");

        lv_grinder = register(r, new BlockMachine(RecipeMaps.GRINDER_RECIPES, Textures.GRINDER, Tier.LV), "lv_grinder");

        mv_brewer = register(r, new BlockMachine(RecipeMaps.BREWING_RECIPES, Textures.GRINDER, Tier.MV), "mv_brewer");

        lv_furnace = register(r, new BlockFurnace(Tier.LV), "lv_furnace");

        lv_steam_turbine = register(r, new BlockGenerator(RecipeMaps.STEAM_TURBINE_FUELS, Textures.STEAM_TURBINE, Tier.LV), "lv_steam_turbine");

        distillation_tower = register(r, new BlockDistillationTower(), "distillation_tower");
        electric_blast_furnace = register(r, new BlockElectricBlastFurnace(), "electric_blast_furnace");
        bronze_large_boiler = register(r, new BlockLargeBoiler(TileLargeBoiler.BoilerType.BRONZE), "bronze_large_boiler");

        p = AbstractBlock.Properties.create(Material.IRON).setRequiresTool().harvestTool(ToolType.PICKAXE).hardnessAndResistance(4.0F, 8.0F);

        lv_fluid_input_hatch = register(r, new BlockFluidHatch(false, Tier.LV), "lv_fluid_input_hatch");
        lv_fluid_output_hatch = register(r, new BlockFluidHatch(true, Tier.LV), "lv_fluid_output_hatch");
        lv_item_input_hatch = register(r, new BlockItemHatch(false, Tier.LV), "lv_item_input_hatch");
        lv_item_output_hatch = register(r, new BlockItemHatch(true, Tier.LV), "lv_item_output_hatch");
        lv_energy_input_hatch = register(r, new BlockEnergyHatch(false, Tier.LV), "lv_energy_input_hatch");
        lv_energy_output_hatch = register(r, new BlockEnergyHatch(true, Tier.LV), "lv_energy_output_hatch");

        mv_energy_input_hatch = register(r, new BlockEnergyHatch(false, Tier.MV), "mv_energy_input_hatch");

        lv_cable = register(r, new CableBlock(p, Tier.LV, 0.125F), "lv_cable");
        mv_cable = register(r, new CableBlock(p, Tier.MV, 0.1875F), "mv_cable");
        hv_cable = register(r, new CableBlock(p, Tier.HV, 0.25F), "hv_cable");
        ev_cable = register(r, new CableBlock(p, Tier.EV, 0.3125F), "ev_cable");

        bronze_pipe = register(r, new BlockFluidPipe(pipeShapeCache, FluidPipeType.BASIC), "bronze_pipe");

        cupronickel_coil = register(r, new BlockCoil(CoilType.CUPRONICKEL), "cupronickel_coil");

        reinforced_glass = register(r, new GlassBlock(AbstractBlock.Properties.create(Material.IRON).notSolid().setRequiresTool().harvestTool(ToolType.PICKAXE)), "reinforced_glass");

        rubber_log = register(r, new BlockRubberWood(), "rubber_log");
        rubber_sapling = register(r, new BlockRubberSapling(), "rubber_sapling");
        rubber_leaves = register(r, new LeavesBlock(AbstractBlock.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()), "rubber_leaves");
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        Item.Properties p = ModItems.properties.maxStackSize(64);

        Item.Properties tile = ModItems.properties.maxStackSize(64);

        register(r, new BlockItem(copper_ore, p), "copper_ore");
        register(r, new BlockItem(tin_ore, p), "tin_ore");
        register(r, new BlockItem(lead_ore, p), "lead_ore");
        register(r, new BlockItem(silver_ore, p), "silver_ore");
        register(r, new BlockItem(uranium_ore, p), "uranium_ore");
        register(r, new BlockItem(nickel_ore, p), "nickel_ore");
        register(r, new BlockItem(platinum_ore, p), "platinum_ore");
        register(r, new BlockItem(titanium_ore, p), "titanium_ore");
        register(r, new BlockItem(iron_ore, p), "iron_ore");
        register(r, new BlockItem(coal_ore, p), "coal_ore");
        register(r, new BlockItem(gold_ore, p), "gold_ore");
        register(r, new BlockItem(redstone_ore, p), "redstone_ore");
        register(r, new BlockItem(diamond_ore, p), "diamond_ore");

        register(r, new BlockItem(heat_proof_casing, p), "heat_proof_casing");
        register(r, new BlockItem(stainless_steel_casing, p), "stainless_steel_casing");
        register(r, new BlockItem(bronze_bricks, p), "bronze_bricks");
        register(r, new BlockItem(bronze_boiler_casing, p), "bronze_boiler_casing");

        register(r, new BlockItem(lv_grinder, propertiesWithRenderer(tile, lv_grinder)), "lv_grinder");

        register(r, new BlockItem(mv_brewer, propertiesWithRenderer(tile, mv_brewer)), "mv_brewer");

        register(r, new BlockItem(lv_furnace, propertiesWithRenderer(tile, lv_furnace)), "lv_furnace");

        register(r, new BlockItem(lv_steam_turbine, propertiesWithRenderer(tile, lv_steam_turbine)), "lv_steam_turbine");

        register(r, new BlockItem(distillation_tower, propertiesWithRenderer(tile, distillation_tower)), "distillation_tower");
        register(r, new BlockItem(electric_blast_furnace, propertiesWithRenderer(tile, electric_blast_furnace)), "electric_blast_furnace");
        register(r, new BlockItem(bronze_large_boiler, propertiesWithRenderer(tile, bronze_large_boiler)), "large_boiler");

        register(r, new BlockItem(lv_fluid_input_hatch, propertiesWithRenderer(tile, lv_fluid_input_hatch)), "lv_fluid_input_hatch");
        register(r, new BlockItem(lv_fluid_output_hatch, propertiesWithRenderer(tile, lv_fluid_output_hatch)), "lv_fluid_output_hatch");
        register(r, new BlockItem(lv_item_input_hatch, propertiesWithRenderer(tile, lv_item_input_hatch)), "lv_item_input_hatch");
        register(r, new BlockItem(lv_item_output_hatch, propertiesWithRenderer(tile, lv_item_output_hatch)), "lv_item_output_hatch");
        register(r, new BlockItem(lv_energy_input_hatch, propertiesWithRenderer(tile, lv_energy_input_hatch)), "lv_energy_input_hatch");
        register(r, new BlockItem(lv_energy_output_hatch, propertiesWithRenderer(tile, lv_energy_output_hatch)), "lv_energy_output_hatch");

        register(r, new BlockItem(mv_energy_input_hatch, propertiesWithRenderer(tile, mv_energy_input_hatch)), "mv_energy_input_hatch");

        register(r, new BlockItem(lv_cable, p), "lv_cable");
        register(r, new BlockItem(mv_cable, p), "mv_cable");
        register(r, new BlockItem(hv_cable, p), "hv_cable");
        register(r, new BlockItem(ev_cable, p), "ev_cable");

        register(r, new BlockItem(bronze_pipe, p), "bronze_pipe");

        register(r, new BlockItem(cupronickel_coil, p), "cupronickel_coil");

        register(r, new BlockItem(reinforced_glass, p), "reinforced_glass");

        register(r, new BlockItem(rubber_log, p), "rubber_log");
        register(r, new BlockItem(rubber_sapling, p), "rubber_sapling");
        register(r, new BlockItem(rubber_leaves, p), "rubber_leaves");
    }

    public static void initRenderLayers() {
        setRenderLayer(distilled_water, RenderType.getTranslucent());
        setRenderLayer(crude_oil, RenderType.getTranslucent());
        setRenderLayer(reinforced_glass, RenderType.getTranslucent());
        setRenderLayer(rubber_sapling, RenderType.getCutout());
        setRenderLayer(bronze_pipe, RenderType.getCutout());
    }

    public static Item.Properties propertiesWithRenderer(Item.Properties properties, Block block) {
        if (block instanceof BlockMachine) {
            final MachineItemRenderer renderer = new MachineItemRenderer(((BlockMachine) block).getRenderer(), ((BlockMachine) block).getTier());
            return properties.setISTER(() -> renderer::call);
        } else if (block instanceof BlockGenerator) {
            final GeneratorItemRenderer renderer = new GeneratorItemRenderer(((BlockGenerator) block).getRenderer(), ((BlockGenerator) block).getTier());
            return properties.setISTER(() -> renderer::call);
        } else if (block instanceof BlockTile) {
            final CoreItemRenderer renderer = new CoreItemRenderer(new LazyValue<>(() -> block.createTileEntity(null, null)));
            return properties.setISTER(() -> renderer::call);
        } else {
            return properties;
        }
    }

}
