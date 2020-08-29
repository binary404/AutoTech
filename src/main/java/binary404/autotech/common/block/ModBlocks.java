package binary404.autotech.common.block;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.generator.BlockBioGenerator;
import binary404.autotech.common.block.machine.BlockGrinder;
import binary404.autotech.common.block.machine.BlockSawMill;
import binary404.autotech.common.block.machine.BlockSmelter;
import binary404.autotech.common.block.transfer.BlockCable;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

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

    public static Block lv_bio_generator;
    public static Block mv_bio_generator;

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

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> r = event.getRegistry();
        AbstractBlock.Properties p = AbstractBlock.Properties.create(Material.ROCK).harvestLevel(2).harvestTool(ToolType.PICKAXE).hardnessAndResistance(3.0f, 6.0f);

        register(r, new OreBlock(p), "copper_ore");
        register(r, new OreBlock(p), "tin_ore");
        register(r, new OreBlock(p), "lead_ore");
        register(r, new OreBlock(p), "silver_ore");
        register(r, new OreBlock(p), "uranium_ore");
        register(r, new OreBlock(p), "nickel_ore");
        register(r, new OreBlock(p), "platinum_ore");
        register(r, new OreBlock(p), "titanium_ore");

        lv_smelter = register(r, new BlockSmelter(p, Tier.LV), "lv_smelter");
        mv_smelter = register(r, new BlockSmelter(p, Tier.MV), "mv_smelter");
        hv_smelter = register(r, new BlockSmelter(p, Tier.HV), "hv_smelter");
        ev_smelter = register(r, new BlockSmelter(p, Tier.EV), "ev_smelter");
        iv_smelter = register(r, new BlockSmelter(p, Tier.IV), "iv_smelter");
        maxv_smelter = register(r, new BlockSmelter(p, Tier.MaxV), "maxv_smelter");

        lv_bio_generator = register(r, new BlockBioGenerator(p, Tier.LV), "lv_bio_generator");
        mv_bio_generator = register(r, new BlockBioGenerator(p, Tier.MV), "mv_bio_generator");

        lv_sawmill = register(r, new BlockSawMill(p, Tier.LV), "lv_sawmill");
        mv_sawmill = register(r, new BlockSawMill(p, Tier.MV), "mv_sawmill");
        hv_sawmill = register(r, new BlockSawMill(p, Tier.HV), "hv_sawmill");
        ev_sawmill = register(r, new BlockSawMill(p, Tier.EV), "ev_sawmill");
        iv_sawmill = register(r, new BlockSawMill(p, Tier.IV), "iv_sawmill");
        maxv_sawmill = register(r, new BlockSawMill(p, Tier.MaxV), "maxv_sawmill");

        lv_grinder = register(r, new BlockGrinder(p, Tier.LV), "lv_grinder");
        mv_grinder = register(r, new BlockGrinder(p, Tier.MV), "mv_grinder");
        hv_grinder = register(r, new BlockGrinder(p, Tier.HV), "hv_grinder");
        ev_grinder = register(r, new BlockGrinder(p, Tier.EV), "ev_grinder");
        iv_grinder = register(r, new BlockGrinder(p, Tier.IV), "iv_grinder");
        maxv_grinder = register(r, new BlockGrinder(p, Tier.MaxV), "maxv_grinder");

        lv_cable = register(r, new BlockCable(p, Tier.LV), "lv_cable");
        mv_cable = register(r, new BlockCable(p, Tier.MV), "mv_cable");
        hv_cable = register(r, new BlockCable(p, Tier.HV), "hv_cable");
        ev_cable = register(r, new BlockCable(p, Tier.EV), "ev_cable");
        iv_cable = register(r, new BlockCable(p, Tier.IV), "iv_cable");
        maxv_cable = register(r, new BlockCable(p, Tier.MaxV), "maxv_cable");
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        register(r, new BlockItem(copper_ore, ModItems.properties), "copper_ore");
        register(r, new BlockItem(tin_ore, ModItems.properties), "tin_ore");
        register(r, new BlockItem(lead_ore, ModItems.properties), "lead_ore");
        register(r, new BlockItem(silver_ore, ModItems.properties), "silver_ore");
        register(r, new BlockItem(uranium_ore, ModItems.properties), "uranium_ore");
        register(r, new BlockItem(nickel_ore, ModItems.properties), "nickel_ore");
        register(r, new BlockItem(platinum_ore, ModItems.properties), "platinum_ore");
        register(r, new BlockItem(titanium_ore, ModItems.properties), "titanium_ore");

        register(r, new BlockItem(lv_smelter, ModItems.properties), "lv_smelter");
        register(r, new BlockItem(mv_smelter, ModItems.properties), "mv_smelter");
        register(r, new BlockItem(hv_smelter, ModItems.properties), "hv_smelter");
        register(r, new BlockItem(ev_smelter, ModItems.properties), "ev_smelter");
        register(r, new BlockItem(iv_smelter, ModItems.properties), "iv_smelter");
        register(r, new BlockItem(maxv_smelter, ModItems.properties), "maxv_smelter");

        register(r, new BlockItem(lv_bio_generator, ModItems.properties), "lv_bio_generator");
        register(r, new BlockItem(mv_bio_generator, ModItems.properties), "mv_bio_generator");

        register(r, new BlockItem(lv_sawmill, ModItems.properties), "lv_sawmill");
        register(r, new BlockItem(mv_sawmill, ModItems.properties), "mv_sawmill");
        register(r, new BlockItem(hv_sawmill, ModItems.properties), "hv_sawmill");
        register(r, new BlockItem(ev_sawmill, ModItems.properties), "ev_sawmill");
        register(r, new BlockItem(iv_sawmill, ModItems.properties), "iv_sawmill");
        register(r, new BlockItem(maxv_sawmill, ModItems.properties), "maxv_sawmill");

        register(r, new BlockItem(lv_grinder, ModItems.properties), "lv_grinder");
        register(r, new BlockItem(mv_grinder, ModItems.properties), "mv_grinder");
        register(r, new BlockItem(hv_grinder, ModItems.properties), "hv_grinder");
        register(r, new BlockItem(ev_grinder, ModItems.properties), "ev_grinder");
        register(r, new BlockItem(iv_grinder, ModItems.properties), "iv_grinder");
        register(r, new BlockItem(maxv_grinder, ModItems.properties), "maxv_grinder");

        register(r, new BlockItem(lv_cable, ModItems.properties), "lv_cable");
        register(r, new BlockItem(mv_cable, ModItems.properties), "mv_cable");
        register(r, new BlockItem(hv_cable, ModItems.properties), "hv_cable");
        register(r, new BlockItem(ev_cable, ModItems.properties), "ev_cable");
        register(r, new BlockItem(iv_cable, ModItems.properties), "iv_cable");
        register(r, new BlockItem(maxv_cable, ModItems.properties), "maxv_cable");
    }

}
