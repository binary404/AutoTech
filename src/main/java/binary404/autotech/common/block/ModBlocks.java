package binary404.autotech.common.block;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.device.BlockWaterPump;
import binary404.autotech.common.block.generator.BlockSteamGenerator;
import binary404.autotech.common.block.machine.*;
import binary404.autotech.common.block.misc.BlockDisplayStand;
import binary404.autotech.common.block.multiblock.BlockAssemblyLine;
import binary404.autotech.common.block.multiblock.BlockBlastFurnace;
import binary404.autotech.common.block.multiblock.BlockHatch;
import binary404.autotech.common.block.multiblock.BlockDistillery;
import binary404.autotech.common.block.transfer.BlockCable;
import binary404.autotech.common.block.transfer.BlockConveyor;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.fluid.BlockBasicFlowingFluid;
import binary404.autotech.common.fluid.ModFluids;
import binary404.autotech.common.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.autotech.client.util.RenderLayerHelper.setRenderLayer;
import static binary404.autotech.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = AutoTech.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    @ObjectHolder("autotech:copper_ore")
    public static OreBlock copper_ore;

    @ObjectHolder("autotech:tin_ore")
    public static OreBlock tin_ore;

    @ObjectHolder("autotech:lead_ore")
    public static OreBlock lead_ore;

    @ObjectHolder("autotech:silver_ore")
    public static OreBlock silver_ore;

    @ObjectHolder("autotech:uranium_ore")
    public static OreBlock uranium_ore;

    @ObjectHolder("autotech:nickel_ore")
    public static OreBlock nickel_ore;

    @ObjectHolder("autotech:platinum_ore")
    public static OreBlock platinum_ore;

    @ObjectHolder("autotech:titanium_ore")
    public static OreBlock titanium_ore;

    public static Block lv_smelter;
    public static Block mv_smelter;
    public static Block hv_smelter;
    public static Block ev_smelter;
    public static Block iv_smelter;
    public static Block maxv_smelter;

    public static Block lv_steam_generator;
    public static Block mv_steam_generator;
    public static Block hv_steam_generator;

    public static Block lv_sawmill;
    public static Block mv_sawmill;
    public static Block hv_sawmill;
    public static Block ev_sawmill;
    public static Block iv_sawmill;
    public static Block maxv_sawmill;

    public static Block lv_grinder;
    public static Block mv_grinder;
    public static Block hv_grinder;
    public static Block ev_grinder;
    public static Block iv_grinder;
    public static Block maxv_grinder;

    public static Block lv_cable;
    public static Block mv_cable;
    public static Block hv_cable;
    public static Block ev_cable;
    public static Block iv_cable;
    public static Block maxv_cable;

    public static Block conveyor;

    public static Block waterpump;

    public static Block lv_compactor;
    public static Block mv_compactor;

    public static Block mv_centrifuge;
    public static Block hv_centrifuge;

    public static Block mv_distillery;

    public static Block mv_assembler;

    public static Block mv_charger;

    public static Block hv_assembly_line;

    public static BlockBasicFlowingFluid distilled_water;

    public static Block iron_plating;

    public static Block lv_arc_furnace;

    public static Block item_input_hatch;
    public static Block item_output_hatch;
    public static Block energy_input_hatch;
    public static Block fluid_input_hatch;
    public static Block fluid_output_hatch;

    public static Block heat_proof_casing;
    public static Block mechanical_casing;
    public static Block basic_coil;

    public static Block reinforced_glass;

    public static Block display_stand;

    public static Block empowerer;

    public static Block rust;
    public static Block rust2;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> r = event.getRegistry();
        AbstractBlock.Properties p = AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().harvestLevel(1).harvestTool(ToolType.PICKAXE).hardnessAndResistance(3.0f, 3.0f);

        register(r, new OreBlock(p), "copper_ore");
        register(r, new OreBlock(p), "tin_ore");
        p.harvestLevel(2);
        register(r, new OreBlock(p), "lead_ore");
        register(r, new OreBlock(p), "silver_ore");
        register(r, new OreBlock(p), "uranium_ore");
        register(r, new OreBlock(p), "nickel_ore");
        p.harvestLevel(3);
        register(r, new OreBlock(p), "platinum_ore");
        register(r, new OreBlock(p), "titanium_ore");

        lv_smelter = register(r, new BlockSmelter(Tier.LV), "lv_smelter");
        mv_smelter = register(r, new BlockSmelter(Tier.MV), "mv_smelter");
        hv_smelter = register(r, new BlockSmelter(Tier.HV), "hv_smelter");
        ev_smelter = register(r, new BlockSmelter(Tier.EV), "ev_smelter");
        iv_smelter = register(r, new BlockSmelter(Tier.IV), "iv_smelter");
        maxv_smelter = register(r, new BlockSmelter(Tier.MaxV), "maxv_smelter");

        lv_steam_generator = register(r, new BlockSteamGenerator(Tier.LV), "lv_steam_generator");
        mv_steam_generator = register(r, new BlockSteamGenerator(Tier.MV), "mv_steam_generator");
        hv_steam_generator = register(r, new BlockSteamGenerator(Tier.HV), "hv_steam_generator");

        lv_sawmill = register(r, new BlockSawMill(Tier.LV), "lv_sawmill");
        mv_sawmill = register(r, new BlockSawMill(Tier.MV), "mv_sawmill");
        hv_sawmill = register(r, new BlockSawMill(Tier.HV), "hv_sawmill");
        ev_sawmill = register(r, new BlockSawMill(Tier.EV), "ev_sawmill");
        iv_sawmill = register(r, new BlockSawMill(Tier.IV), "iv_sawmill");
        maxv_sawmill = register(r, new BlockSawMill(Tier.MaxV), "maxv_sawmill");

        lv_grinder = register(r, new BlockGrinder(Tier.LV), "lv_grinder");
        mv_grinder = register(r, new BlockGrinder(Tier.MV), "mv_grinder");
        hv_grinder = register(r, new BlockGrinder(Tier.HV), "hv_grinder");
        ev_grinder = register(r, new BlockGrinder(Tier.EV), "ev_grinder");
        iv_grinder = register(r, new BlockGrinder(Tier.IV), "iv_grinder");
        maxv_grinder = register(r, new BlockGrinder(Tier.MaxV), "maxv_grinder");

        lv_cable = register(r, new BlockCable(Tier.LV), "lv_cable");
        mv_cable = register(r, new BlockCable(Tier.MV), "mv_cable");
        hv_cable = register(r, new BlockCable(Tier.HV), "hv_cable");
        ev_cable = register(r, new BlockCable(Tier.EV), "ev_cable");
        iv_cable = register(r, new BlockCable(Tier.IV), "iv_cable");
        maxv_cable = register(r, new BlockCable(Tier.MaxV), "maxv_cable");

        conveyor = register(r, new BlockConveyor(), "conveyor");

        waterpump = register(r, new BlockWaterPump(), "waterpump");

        lv_compactor = register(r, new BlockCompactor(Tier.LV), "lv_compactor");
        mv_compactor = register(r, new BlockCompactor(Tier.MV), "mv_compactor");

        mv_centrifuge = register(r, new BlockCentrifuge(Tier.MV), "mv_centrifuge");
        hv_centrifuge = register(r, new BlockCentrifuge(Tier.HV), "hv_centrifuge");

        mv_distillery = register(r, new BlockDistillery(Tier.MV), "mv_distillery");

        mv_assembler = register(r, new BlockAssembler(Tier.MV), "mv_assembler");

        mv_charger = register(r, new BlockCharger(Tier.MV), "mv_charger");

        hv_assembly_line = register(r, new BlockAssemblyLine(Tier.HV), "hv_assembly_line");

        distilled_water = (BlockBasicFlowingFluid) register(r, new BlockBasicFlowingFluid(() -> ModFluids.distilled_water), "distilled_water");

        p = AbstractBlock.Properties.create(Material.IRON).setRequiresTool().harvestTool(ToolType.PICKAXE).hardnessAndResistance(4.0F, 8.0F);

        iron_plating = register(r, new Block(p), "iron_plating");

        lv_arc_furnace = register(r, new BlockBlastFurnace(Tier.LV), "lv_arc_furnace");

        item_input_hatch = register(r, new BlockHatch(), "item_input_hatch");
        item_output_hatch = register(r, new BlockHatch(), "item_output_hatch");
        energy_input_hatch = register(r, new BlockHatch(), "energy_input_hatch");
        fluid_input_hatch = register(r, new BlockHatch(), "fluid_input_hatch");
        fluid_output_hatch = register(r, new BlockHatch(), "fluid_output_hatch");

        heat_proof_casing = register(r, new Block(p), "heat_proof_casing");
        mechanical_casing = register(r, new Block(p), "mechanical_casing");
        basic_coil = register(r, new Block(p), "basic_coil");

        reinforced_glass = register(r, new GlassBlock(AbstractBlock.Properties.create(Material.IRON).notSolid().setRequiresTool().harvestTool(ToolType.PICKAXE)), "reinforced_glass");

        display_stand = register(r, new BlockDisplayStand(), "display_stand");

        empowerer = register(r, new BlockEmpowerer(), "empowerer");

        rust = register(r, new Block(p), "rust");
        rust2 = register(r, new Block(p), "rust2");
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        Item.Properties p = ModItems.properties.maxStackSize(64);

        register(r, new BlockItem(copper_ore, p), "copper_ore");
        register(r, new BlockItem(tin_ore, p), "tin_ore");
        register(r, new BlockItem(lead_ore, p), "lead_ore");
        register(r, new BlockItem(silver_ore, p), "silver_ore");
        register(r, new BlockItem(uranium_ore, p), "uranium_ore");
        register(r, new BlockItem(nickel_ore, p), "nickel_ore");
        register(r, new BlockItem(platinum_ore, p), "platinum_ore");
        register(r, new BlockItem(titanium_ore, p), "titanium_ore");

        register(r, new BlockItem(lv_smelter, p), "lv_smelter");
        register(r, new BlockItem(mv_smelter, p), "mv_smelter");
        register(r, new BlockItem(hv_smelter, p), "hv_smelter");
        register(r, new BlockItem(ev_smelter, p), "ev_smelter");
        register(r, new BlockItem(iv_smelter, p), "iv_smelter");
        register(r, new BlockItem(maxv_smelter, p), "maxv_smelter");

        register(r, new BlockItem(lv_steam_generator, p), "lv_steam_generator");
        register(r, new BlockItem(mv_steam_generator, p), "mv_steam_generator");
        register(r, new BlockItem(hv_steam_generator, p), "hv_steam_generator");

        register(r, new BlockItem(lv_sawmill, p), "lv_sawmill");
        register(r, new BlockItem(mv_sawmill, p), "mv_sawmill");
        register(r, new BlockItem(hv_sawmill, p), "hv_sawmill");
        register(r, new BlockItem(ev_sawmill, p), "ev_sawmill");
        register(r, new BlockItem(iv_sawmill, p), "iv_sawmill");
        register(r, new BlockItem(maxv_sawmill, p), "maxv_sawmill");

        register(r, new BlockItem(lv_grinder, p), "lv_grinder");
        register(r, new BlockItem(mv_grinder, p), "mv_grinder");
        register(r, new BlockItem(hv_grinder, p), "hv_grinder");
        register(r, new BlockItem(ev_grinder, p), "ev_grinder");
        register(r, new BlockItem(iv_grinder, p), "iv_grinder");
        register(r, new BlockItem(maxv_grinder, p), "maxv_grinder");

        register(r, new BlockItem(lv_cable, p), "lv_cable");
        register(r, new BlockItem(mv_cable, p), "mv_cable");
        register(r, new BlockItem(hv_cable, p), "hv_cable");
        register(r, new BlockItem(ev_cable, p), "ev_cable");
        register(r, new BlockItem(iv_cable, p), "iv_cable");
        register(r, new BlockItem(maxv_cable, p), "maxv_cable");

        register(r, new BlockItem(conveyor, p), "conveyor");

        register(r, new BlockItem(waterpump, p), "waterpump");

        register(r, new BlockItem(lv_compactor, p), "lv_compactor");
        register(r, new BlockItem(mv_compactor, p), "mv_compactor");

        register(r, new BlockItem(mv_centrifuge, p), "mv_centrifuge");

        register(r, new BlockItem(mv_distillery, p), "mv_distillery");

        register(r, new BlockItem(mv_assembler, p), "mv_assembler");

        register(r, new BlockItem(mv_charger, p), "mv_charger");

        register(r, new BlockItem(hv_assembly_line, p), "hv_assembly_line");

        register(r, new BlockItem(iron_plating, p), "iron_plating");

        register(r, new BlockItem(lv_arc_furnace, p), "lv_arc_furnace");

        register(r, new BlockItem(item_input_hatch, p), "item_input_hatch");
        register(r, new BlockItem(item_output_hatch, p), "item_output_hatch");
        register(r, new BlockItem(energy_input_hatch, p), "energy_input_hatch");
        register(r, new BlockItem(fluid_input_hatch, p), "fluid_input_hatch");
        register(r, new BlockItem(fluid_output_hatch, p), "fluid_output_hatch");

        register(r, new BlockItem(heat_proof_casing, p), "heat_proof_casing");
        register(r, new BlockItem(mechanical_casing, p), "mechanical_casing");
        register(r, new BlockItem(basic_coil, p), "basic_coil");

        register(r, new BlockItem(reinforced_glass, p), "reinforced_glass");

        register(r, new BlockItem(display_stand, p), "display_stand");

        register(r, new BlockItem(empowerer, p), "empowerer");

        register(r, new BlockItem(rust, p), "rust");
        register(r, new BlockItem(rust2, p), "rust2");
    }

    public static void initRenderLayers() {
        setRenderLayer(distilled_water, RenderType.getTranslucent());
        setRenderLayer(reinforced_glass, RenderType.getTranslucent());
    }

}
