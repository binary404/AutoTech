package binary404.autotech.data;

import binary404.autotech.AutoTech;
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
        addToTag(ModTags.Items.PLATES_BRONZE, ModItems.bronze_plate);
        addToTag(ModTags.Items.PLATES_STEEL, ModItems.steel_plate);
        getItemBuilder(ModTags.Items.PLATES).add(ModTags.Items.PLATES_COPPER, ModTags.Items.PLATES_TIN, ModTags.Items.PLATES_LEAD, ModTags.Items.PLATES_SILVER, ModTags.Items.PLATES_URANIUM, ModTags.Items.PLATES_NICKEL, ModTags.Items.PLATES_PLATINUM, ModTags.Items.PLATES_TITANIUM, ModTags.Items.PLATES_BRONZE, ModTags.Items.PLATES_STEEL);
    }

    private void addBeaconTags() {
        addToTag(ItemTags.BEACON_PAYMENT_ITEMS, ModItems.copper_ingot, ModItems.tin_ingot, ModItems.lead_ingot, ModItems.silver_ingot, ModItems.uranium_ingot, ModItems.nickel_ingot, ModItems.platinum_ingot, ModItems.titanium_ingot, ModItems.bronze_ingot, ModItems.steel_ingot);
    }
}
