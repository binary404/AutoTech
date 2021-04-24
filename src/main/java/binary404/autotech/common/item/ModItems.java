package binary404.autotech.common.item;

import binary404.autotech.AutoTech;
import binary404.autotech.common.fluid.ItemBasicFluidBucket;
import binary404.autotech.common.fluid.ModFluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import static binary404.autotech.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = AutoTech.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static Item raw_copper;
    public static Item raw_tin;
    public static Item raw_lead;
    public static Item raw_silver;
    public static Item raw_uranium;
    public static Item raw_nickel;
    public static Item raw_platinum;
    public static Item raw_titanium;
    public static Item raw_iron;
    public static Item raw_gold;

    public static Item copper_ingot;
    public static Item tin_ingot;
    public static Item lead_ingot;
    public static Item silver_ingot;
    public static Item uranium_ingot;
    public static Item nickel_ingot;
    public static Item platinum_ingot;
    public static Item titanium_ingot;

    public static Item red_alloy_ingot;
    public static Item bronze_ingot;
    public static Item steel_ingot;

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
    public static Item steel_plate;

    public static Item iron_rod;
    public static Item steel_rod;

    public static Item lv_machine_hull;
    public static Item mv_machine_hull;

    public static Item mortar;
    public static Item hammer;

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
    public static Item netherite_ore_dust;

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
    public static Item netherite_dust;
    public static Item coal_dust;
    public static Item charcoal_dust;

    public static Item red_alloy_dust;
    public static Item bronze_dust;

    public static Item saw_dust;
    public static Item plywood;
    public static Item basic_circuit_board;

    public static Item carbon_mesh;

    public static Item saw_blade;

    public static Item mv_logic_component;
    public static Item mv_receiver_component;
    public static Item mv_transmitter_component;

    public static Item mv_logic_circuit;
    public static Item mv_receiver_circuit;
    public static Item mv_transmitter_circuit;

    public static Item distilled_water_bucket;
    public static Item crude_oil_bucket;
    public static Item biomass_bucket;

    public static Item flour;

    public static Item apple_pie;
    public static Item golden_apple_pie;
    public static Item enchanted_golden_apple_pie;

    public static Item thermo_electric_component;

    public static Item mv_grinder_blade;
    public static Item mv_motor;
    public static Item mv_piston;
    public static Item mv_centrifugal_component;
    public static Item mv_vacuum_tube;
    public static Item mv_resistor;

    public static Item energy_leggings;
    public static Item energy_boots;
    public static Item energy_chestplate;
    public static Item energy_helmet;

    public static Item jetpack;

    public static Item rubber_drop;

    public static Item.Properties properties = new Item.Properties().group(AutoTech.group);

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        raw_copper = register(r, new Item(properties), "raw_copper");
        raw_tin = register(r, new Item(properties), "raw_tin");
        raw_lead = register(r, new Item(properties), "raw_lead");
        raw_silver = register(r, new Item(properties), "raw_silver");
        raw_uranium = register(r, new Item(properties), "raw_uranium");
        raw_nickel = register(r, new Item(properties), "raw_nickel");
        raw_platinum = register(r, new Item(properties), "raw_platinum");
        raw_titanium = register(r, new Item(properties), "raw_titanium");
        raw_iron = register(r, new Item(properties), "raw_iron");
        raw_gold = register(r, new Item(properties), "raw_gold");

        copper_ingot = register(r, new Item(properties), "copper_ingot");
        tin_ingot = register(r, new Item(properties), "tin_ingot");
        lead_ingot = register(r, new Item(properties), "lead_ingot");
        silver_ingot = register(r, new Item(properties), "silver_ingot");
        uranium_ingot = register(r, new Item(properties), "uranium_ingot");
        nickel_ingot = register(r, new Item(properties), "nickel_ingot");
        platinum_ingot = register(r, new Item(properties), "platinum_ingot");
        titanium_ingot = register(r, new Item(properties), "titanium_ingot");

        red_alloy_ingot = register(r, new Item(properties), "red_alloy_ingot");
        bronze_ingot = register(r, new Item(properties), "bronze_ingot");
        steel_ingot = register(r, new Item(properties), "steel_ingot");

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
        netherite_ore_dust = register(r, new Item(properties), "netherite_ore_dust");
        coal_dust = register(r, new Item(properties), "coal_dust");
        charcoal_dust = register(r, new Item(properties), "charcoal_dust");

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
        netherite_dust = register(r, new Item(properties), "netherite_dust");

        red_alloy_dust = register(r, new Item(properties), "red_alloy_dust");
        bronze_dust = register(r, new Item(properties), "bronze_dust");

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
        steel_plate = register(r, new Item(properties), "steel_plate");

        iron_rod = register(r, new Item(properties), "iron_rod");
        steel_rod = register(r, new Item(properties), "steel_rod");

        saw_dust = register(r, new Item(properties), "saw_dust");

        plywood = register(r, new Item(properties), "plywood");
        basic_circuit_board = register(r, new Item(properties), "basic_circuit_board");

        carbon_mesh = register(r, new Item(properties), "carbon_mesh");

        saw_blade = register(r, new Item(properties), "saw_blade");

        mv_logic_component = register(r, new Item(properties), "mv_logic_component");
        mv_receiver_component = register(r, new Item(properties), "mv_receiver_component");
        mv_transmitter_component = register(r, new Item(properties), "mv_transmitter_component");

        mv_logic_circuit = register(r, new Item(properties), "mv_logic_circuit");
        mv_transmitter_circuit = register(r, new Item(properties), "mv_transmitter_circuit");
        mv_receiver_circuit = register(r, new Item(properties), "mv_receiver_circuit");

        lv_machine_hull = register(r, new Item(properties), "lv_machine_hull");
        mv_machine_hull = register(r, new Item(properties), "mv_machine_hull");

        mortar = register(r, new ItemDamageable(properties.maxDamage(200), 5), "mortar");
        hammer = register(r, new ItemDamageable(properties.maxDamage(300), 10), "hammer");

        distilled_water_bucket = register(r, new ItemBasicFluidBucket(() -> ModFluids.distilled_water), "distilled_water_bucket");
        crude_oil_bucket = register(r, new ItemBasicFluidBucket(() -> ModFluids.crude_oil), "crude_oil_bucket");
        biomass_bucket = register(r, new ItemBasicFluidBucket(() -> ModFluids.biomass), "biomass_bucket");

        flour = register(r, new Item(properties.maxDamage(0).maxStackSize(64)), "flour");

        Item.Properties food = new Item.Properties().group(AutoTech.group);

        apple_pie = register(r, new Item(food.food(ModFoods.applePie).maxStackSize(16)), "apple_pie");
        golden_apple_pie = register(r, new Item(food.food(ModFoods.goldenApplePie).maxStackSize(16)), "golden_apple_pie");
        enchanted_golden_apple_pie = register(r, new EnchantedGoldenAppleItem(food.food(ModFoods.enchantedGoldenApplePie).maxStackSize(16).rarity(Rarity.EPIC)), "enchanted_golden_apple_pie");

        thermo_electric_component = register(r, new Item(properties), "thermal_electric_component");

        mv_grinder_blade = register(r, new Item(properties), "mv_grinder_blade");
        mv_motor = register(r, new Item(properties), "mv_motor");
        mv_piston = register(r, new Item(properties), "mv_piston");
        mv_centrifugal_component = register(r, new Item(properties), "mv_centrifugal_component");
        mv_vacuum_tube = register(r, new Item(properties), "mv_vacuum_tube");
        mv_resistor = register(r, new Item(properties), "mv_resistor");

        energy_leggings = register(r, new ItemEnergySuit(ArmorMaterial.DIAMOND, EquipmentSlotType.LEGS), "energy_leggings");
        energy_boots = register(r, new ItemEnergySuit(ArmorMaterial.DIAMOND, EquipmentSlotType.FEET), "energy_boots");
        energy_chestplate = register(r, new ItemEnergySuit(ArmorMaterial.DIAMOND, EquipmentSlotType.CHEST), "energy_chestplate");
        energy_helmet = register(r, new ItemEnergySuit(ArmorMaterial.DIAMOND, EquipmentSlotType.HEAD), "energy_helmet");

        jetpack = register(r, new ItemEnergySuit(ArmorMaterial.DIAMOND, EquipmentSlotType.CHEST), "jetpack");

        rubber_drop = register(r, new Item(properties), "rubber_drop");
    }

}
