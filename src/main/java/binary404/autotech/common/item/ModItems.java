package binary404.autotech.common.item;

import binary404.autotech.AutoTech;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import static binary404.autotech.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = AutoTech.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static Item copper_ingot;
    public static Item tin_ingot;
    public static Item lead_ingot;
    public static Item silver_ingot;
    public static Item uranium_ingot;
    public static Item nickel_ingot;
    public static Item platinum_ingot;
    public static Item titanium_ingot;

    public static Item bronze_ingot;

    public static Item copper_plate;
    public static Item tin_plate;
    public static Item lead_plate;
    public static Item silver_plate;
    public static Item uranium_plate;
    public static Item nickel_plate;
    public static Item platinum_plate;
    public static Item titanium_plate;
    public static Item iron_plate;
    public static Item gold_plate;
    public static Item bronze_plate;

    public static Item lv_machine_hull;
    public static Item mv_machine_hull;

    public static Item mortar;

    public static Item copper_ore_dust;
    public static Item tin_ore_dust;
    public static Item lead_ore_dust;
    public static Item silver_ore_dust;
    public static Item uranium_ore_dust;
    public static Item nickel_ore_dust;
    public static Item platinum_ore_dust;
    public static Item titanium_ore_dust;
    public static Item iron_ore_dust;
    public static Item gold_ore_dust;
    public static Item osmium_ore_dust;

    public static Item copper_dust;
    public static Item tin_dust;
    public static Item lead_dust;
    public static Item silver_dust;
    public static Item uranium_dust;
    public static Item nickel_dust;
    public static Item platinum_dust;
    public static Item titanium_dust;
    public static Item iron_dust;
    public static Item gold_dust;

    public static Item bronze_dust;

    public static Item saw_dust;

    public static Item mv_logic_circuit;
    public static Item mv_receiver_circuit;
    public static Item mv_transmitter_circuit;

    public static Item.Properties properties = new Item.Properties().group(AutoTech.group);

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        copper_ingot = register(r, new Item(properties), "copper_ingot");
        tin_ingot = register(r, new Item(properties), "tin_ingot");
        lead_ingot = register(r, new Item(properties), "lead_ingot");
        silver_ingot = register(r, new Item(properties), "silver_ingot");
        uranium_ingot = register(r, new Item(properties), "uranium_ingot");
        nickel_ingot = register(r, new Item(properties), "nickel_ingot");
        platinum_ingot = register(r, new Item(properties), "platinum_ingot");
        titanium_ingot = register(r, new Item(properties), "titanium_ingot");

        bronze_ingot = register(r, new Item(properties), "bronze_ingot");

        copper_ore_dust = register(r, new Item(properties), "copper_ore_dust");
        tin_ore_dust = register(r, new Item(properties), "tin_ore_dust");
        lead_ore_dust = register(r, new Item(properties), "lead_ore_dust");
        silver_ore_dust = register(r, new Item(properties), "silver_ore_dust");
        uranium_ore_dust = register(r, new Item(properties), "uranium_ore_dust");
        nickel_ore_dust = register(r, new Item(properties), "nickel_ore_dust");
        platinum_ore_dust = register(r, new Item(properties), "platinum_ore_dust");
        titanium_ore_dust = register(r, new Item(properties), "titanium_ore_dust");
        iron_ore_dust = register(r, new Item(properties), "iron_ore_dust");
        gold_ore_dust = register(r, new Item(properties), "gold_ore_dust");
        osmium_ore_dust = register(r, new Item(properties), "osmium_ore_dust");

        copper_dust = register(r, new Item(properties), "copper_dust");
        tin_dust = register(r, new Item(properties), "tin_dust");
        lead_dust = register(r, new Item(properties), "lead_dust");
        silver_dust = register(r, new Item(properties), "silver_dust");
        uranium_dust = register(r, new Item(properties), "uranium_dust");
        nickel_dust = register(r, new Item(properties), "nickel_dust");
        platinum_dust = register(r, new Item(properties), "platinum_dust");
        titanium_dust = register(r, new Item(properties), "titanium_dust");
        iron_dust = register(r, new Item(properties), "iron_dust");
        gold_dust = register(r, new Item(properties), "gold_dust");

        bronze_dust = register(r, new Item(properties), "bronze_dust");

        saw_dust = register(r, new Item(properties), "saw_dust");

        copper_plate = register(r, new Item(properties), "copper_plate");
        tin_plate = register(r, new Item(properties), "tin_plate");
        lead_plate = register(r, new Item(properties), "lead_plate");
        silver_plate = register(r, new Item(properties), "silver_plate");
        uranium_plate = register(r, new Item(properties), "uranium_plate");
        nickel_plate = register(r, new Item(properties), "nickel_plate");
        platinum_plate = register(r, new Item(properties), "platinum_plate");
        titanium_plate = register(r, new Item(properties), "titanium_plate");
        iron_plate = register(r, new Item(properties), "iron_plate");
        gold_plate = register(r, new Item(properties), "gold_plate");
        bronze_plate = register(r, new Item(properties), "bronze_plate");

        lv_machine_hull = register(r, new Item(properties), "lv_machine_hull");
        mv_machine_hull = register(r, new Item(properties), "mv_machine_hull");

        mv_logic_circuit = register(r, new Item(properties), "mv_logic_circuit");
        mv_transmitter_circuit = register(r, new Item(properties), "mv_transmitter_circuit");
        mv_receiver_circuit = register(r, new Item(properties), "mv_receiver_circuit");

        mortar = register(r, new ItemMortar(properties.maxStackSize(1)), "mortar");
    }

}
