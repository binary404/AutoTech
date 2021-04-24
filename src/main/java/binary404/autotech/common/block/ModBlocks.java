package binary404.autotech.common.block;

import binary404.autotech.AutoTech;
import binary404.autotech.client.renders.core.GeneratorItemRenderer;
import binary404.autotech.client.renders.core.MachineItemRenderer;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.block.generator.BlockGenerator;
import binary404.autotech.common.block.machine.BlockMachine;
import binary404.autotech.common.block.multiblock.BlockDistillationTower;
import binary404.autotech.common.block.multiblock.BlockEnergyHatch;
import binary404.autotech.common.block.multiblock.BlockFluidHatch;
import binary404.autotech.common.block.multiblock.BlockItemHatch;
import binary404.autotech.common.block.world.BlockAutoTechOre;
import binary404.autotech.common.block.world.BlockRubberSapling;
import binary404.autotech.common.block.world.BlockRubberWood;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.recipe.RecipeMaps;
import binary404.autotech.common.fluid.BlockBasicFlowingFluid;
import binary404.autotech.common.fluid.ModFluids;
import binary404.autotech.common.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import static binary404.autotech.client.util.RenderLayerHelper.setRenderLayer;
import static binary404.autotech.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = AutoTech.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

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
    public static BlockBasicFlowingFluid distilled_water;
    public static BlockBasicFlowingFluid crude_oil;
    public static BlockBasicFlowingFluid biomass;

    //Machines
    public static Block lv_grinder;
    public static Block extreme_grinder;

    public static Block mv_brewer;

    public static Block distillation_tower;

    public static Block test;

    //Multiblock i/o
    public static Block lv_item_input_hatch;
    public static Block lv_item_output_hatch;
    public static Block lv_fluid_input_hatch;
    public static Block lv_fluid_output_hatch;
    public static Block lv_energy_input_hatch;
    public static Block lv_energy_output_hatch;

    //Misc blocks
    public static Block heat_proof_casing;
    public static Block stainless_steel_casing;
    public static Block basic_coil;
    public static Block iron_plating;
    public static Block reinforced_glass;
    public static Block rust;
    public static Block rust2;

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

        distilled_water = (BlockBasicFlowingFluid) register(r, new BlockBasicFlowingFluid(() -> ModFluids.distilled_water), "distilled_water");
        crude_oil = (BlockBasicFlowingFluid) register(r, new BlockBasicFlowingFluid(() -> ModFluids.crude_oil), "crude_oil");
        biomass = (BlockBasicFlowingFluid) register(r, new BlockBasicFlowingFluid(() -> ModFluids.biomass), "biomass");

        lv_grinder = register(r, new BlockMachine(RecipeMaps.GRINDER_RECIPES, Textures.GRINDER, Tier.LV), "lv_grinder");
        extreme_grinder = register(r, new BlockMachine(RecipeMaps.GRINDER_RECIPES, Textures.GRINDER, Tier.EV), "ev_grinder");

        mv_brewer = register(r, new BlockMachine(RecipeMaps.BREWING_RECIPES, Textures.GRINDER, Tier.MV), "mv_brewer");

        distillation_tower = register(r, new BlockDistillationTower(), "distillation_tower");

        test = register(r, new BlockGenerator(RecipeMaps.STEAM_TURBINE_FUELS, Textures.GRINDER, Tier.LV), "test");

        p = AbstractBlock.Properties.create(Material.IRON).setRequiresTool().harvestTool(ToolType.PICKAXE).hardnessAndResistance(4.0F, 8.0F);

        lv_fluid_input_hatch = register(r, new BlockFluidHatch(false, Tier.LV), "lv_fluid_input_hatch");
        lv_fluid_output_hatch = register(r, new BlockFluidHatch(true, Tier.LV), "lv_fluid_output_hatch");
        lv_item_input_hatch = register(r, new BlockItemHatch(false, Tier.LV), "lv_item_input_hatch");
        lv_item_output_hatch = register(r, new BlockItemHatch(true, Tier.LV), "lv_item_output_hatch");
        lv_energy_input_hatch = register(r, new BlockEnergyHatch(false, Tier.LV), "lv_energy_input_hatch");
        lv_energy_output_hatch = register(r, new BlockEnergyHatch(true, Tier.LV), "lv_energy_output_hatch");

        heat_proof_casing = register(r, new Block(p), "heat_proof_casing");
        stainless_steel_casing = register(r, new Block(p), "stainless_steel_casing");
        basic_coil = register(r, new Block(p), "basic_coil");
        iron_plating = register(r, new Block(p), "iron_plating");
        reinforced_glass = register(r, new GlassBlock(AbstractBlock.Properties.create(Material.IRON).notSolid().setRequiresTool().harvestTool(ToolType.PICKAXE)), "reinforced_glass");

        rust = register(r, new Block(p), "rust");
        rust2 = register(r, new Block(p), "rust2");

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

        register(r, new BlockItem(lv_grinder, propertiesWithRenderer(tile, lv_grinder)), "lv_grinder");
        register(r, new BlockItem(extreme_grinder, propertiesWithRenderer(tile, extreme_grinder)), "extreme_grinder");

        register(r, new BlockItem(mv_brewer, propertiesWithRenderer(tile, mv_brewer)), "mv_brewer");

        register(r, new BlockItem(distillation_tower, propertiesWithRenderer(tile, distillation_tower)), "distillation_tower");

        register(r, new BlockItem(test, propertiesWithRenderer(tile, test)), "test");

        register(r, new BlockItem(lv_fluid_input_hatch, propertiesWithRenderer(tile, lv_fluid_input_hatch)), "lv_fluid_input_hatch");
        register(r, new BlockItem(lv_fluid_output_hatch, propertiesWithRenderer(tile, lv_fluid_output_hatch)), "lv_fluid_output_hatch");
        register(r, new BlockItem(lv_item_input_hatch, propertiesWithRenderer(tile, lv_item_input_hatch)), "lv_item_input_hatch");
        register(r, new BlockItem(lv_item_output_hatch, propertiesWithRenderer(tile, lv_item_output_hatch)), "lv_item_output_hatch");
        register(r, new BlockItem(lv_energy_input_hatch, propertiesWithRenderer(tile, lv_energy_input_hatch)), "lv_energy_input_hatch");
        register(r, new BlockItem(lv_energy_output_hatch, propertiesWithRenderer(tile, lv_energy_output_hatch)), "lv_energy_output_hatch");

        register(r, new BlockItem(heat_proof_casing, p), "heat_proof_casing");
        register(r, new BlockItem(stainless_steel_casing, p), "stainless_steel_casing");
        register(r, new BlockItem(basic_coil, p), "basic_coil");
        register(r, new BlockItem(iron_plating, p), "iron_plating");
        register(r, new BlockItem(reinforced_glass, p), "reinforced_glass");

        register(r, new BlockItem(rust, p), "rust");
        register(r, new BlockItem(rust2, p), "rust2");

        register(r, new BlockItem(rubber_log, p), "rubber_log");
        register(r, new BlockItem(rubber_sapling, p), "rubber_sapling");
        register(r, new BlockItem(rubber_leaves, p), "rubber_leaves");
    }

    public static void initRenderLayers() {
        setRenderLayer(distilled_water, RenderType.getTranslucent());
        setRenderLayer(crude_oil, RenderType.getTranslucent());
        setRenderLayer(reinforced_glass, RenderType.getTranslucent());
        setRenderLayer(rubber_sapling, RenderType.getCutout());
    }

    public static Item.Properties propertiesWithRenderer(Item.Properties properties, Block block) {
        if (block instanceof BlockMachine) {
            return properties.setISTER(() -> MachineItemRenderer::new);
        } else if (block instanceof BlockGenerator) {
            return properties.setISTER(() -> GeneratorItemRenderer::new);
        } else {
            return properties;
        }
    }

}
