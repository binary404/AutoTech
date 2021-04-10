package binary404.autotech.data;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.item.ModItems;
import binary404.autotech.common.tags.ModTags;
import binary404.autotech.data.tag.BaseTagProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class AutoTechTagProvider extends BaseTagProvider {

    public AutoTechTagProvider(DataGenerator gen, @Nullable ExistingFileHelper existingFileHelper) {
        super(gen, AutoTech.modid, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        addBeaconTags();
        addPlates();
        addCircuits();
        addIngots();
        addDusts();
        addRawOres();
        addOres();
    }

    private void addIngots() {
        addToTag(ModTags.Items.INGOTS_RED_ALLOY, ModItems.red_alloy_ingot);

        getItemBuilder(Tags.Items.INGOTS).add(ModTags.Items.INGOTS_RED_ALLOY);
    }

    private void addDusts() {
        addToTag(ModTags.Items.DUSTS_RED_ALLOY, ModItems.red_alloy_dust);

        getItemBuilder(Tags.Items.DUSTS).add(ModTags.Items.DUSTS_RED_ALLOY);
    }

    private void addCircuits() {
        addToTag(ModTags.Items.MV_CIRCUITS_LOGIC, ModItems.mv_logic_circuit);
        addToTag(ModTags.Items.MV_CIRCUITS_RECEIVER, ModItems.mv_receiver_circuit);
        addToTag(ModTags.Items.MV_CIRCUITS_TRANSMITTER, ModItems.mv_transmitter_circuit);

        getItemBuilder(ModTags.Items.MV_CIRCUITS).add(ModTags.Items.MV_CIRCUITS_LOGIC, ModTags.Items.MV_CIRCUITS_RECEIVER, ModTags.Items.MV_CIRCUITS_TRANSMITTER);
    }

    private void addPlates() {
        addToTag(ModTags.Items.PLATES_COPPER, ModItems.copper_plate);
        addToTag(ModTags.Items.PLATES_TIN, ModItems.tin_plate);
        addToTag(ModTags.Items.PLATES_LEAD, ModItems.lead_plate);
        addToTag(ModTags.Items.PLATES_SILVER, ModItems.silver_plate);
        addToTag(ModTags.Items.PLATES_URANIUM, ModItems.uranium_plate);
        addToTag(ModTags.Items.PLATES_NICKEL, ModItems.nickel_plate);
        addToTag(ModTags.Items.PLATES_PLATINUM, ModItems.platinum_plate);
        addToTag(ModTags.Items.PLATES_TITANIUM, ModItems.titanium_plate);
        addToTag(ModTags.Items.PLATES_IRON, ModItems.iron_plate);
        addToTag(ModTags.Items.PLATES_BRONZE, ModItems.bronze_plate);
        addToTag(ModTags.Items.PLATES_STEEL, ModItems.steel_plate);
        getItemBuilder(ModTags.Items.PLATES).add(ModTags.Items.PLATES_COPPER, ModTags.Items.PLATES_TIN, ModTags.Items.PLATES_LEAD, ModTags.Items.PLATES_SILVER, ModTags.Items.PLATES_URANIUM, ModTags.Items.PLATES_NICKEL, ModTags.Items.PLATES_PLATINUM, ModTags.Items.PLATES_TITANIUM, ModTags.Items.PLATES_IRON, ModTags.Items.PLATES_BRONZE, ModTags.Items.PLATES_STEEL);
    }

    private void addBeaconTags() {
        addToTag(ItemTags.BEACON_PAYMENT_ITEMS, ModItems.copper_ingot, ModItems.tin_ingot, ModItems.lead_ingot, ModItems.silver_ingot, ModItems.uranium_ingot, ModItems.nickel_ingot, ModItems.platinum_ingot, ModItems.titanium_ingot, ModItems.bronze_ingot, ModItems.steel_ingot);
    }

    private void addRawOres() {
        addToTag(ModTags.Items.RAW_COPPER, ModItems.raw_copper);
        addToTag(ModTags.Items.RAW_TIN, ModItems.raw_tin);
        addToTag(ModTags.Items.RAW_LEAD, ModItems.raw_lead);
        addToTag(ModTags.Items.RAW_SILVER, ModItems.raw_silver);
        addToTag(ModTags.Items.RAW_URANIUM, ModItems.raw_uranium);
        addToTag(ModTags.Items.RAW_NICKEL, ModItems.raw_nickel);
        addToTag(ModTags.Items.RAW_PLATINUM, ModItems.raw_platinum);
        addToTag(ModTags.Items.RAW_TITANIUM, ModItems.raw_titanium);
        getItemBuilder(ModTags.Items.RAW).add(ModTags.Items.RAW_COPPER, ModTags.Items.RAW_TIN, ModTags.Items.RAW_LEAD, ModTags.Items.RAW_SILVER, ModTags.Items.RAW_URANIUM, ModTags.Items.RAW_NICKEL, ModTags.Items.RAW_PLATINUM, ModTags.Items.RAW_TITANIUM);
    }

    private void addOres() {
        addToTags(ModTags.Items.ORES_COPPER, ModTags.Blocks.ORE_COPPER, ModBlocks.copper_ore);
        addToTags(ModTags.Items.ORES_TIN, ModTags.Blocks.ORE_TIN, ModBlocks.tin_ore);
    }
}
