package binary404.autotech.data.tag;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.item.ModItems;
import binary404.autotech.common.tags.ModTags;
import binary404.autotech.data.util.IItemProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;

public class TagProvider extends BaseTagProvider {

    public TagProvider(DataGenerator gen) {
        super(gen, AutoTech.modid);
    }

    @Override
    protected void registerTags() {
        addIngots();
        addOres();
        addDusts();
        addOreDusts();
    }

    private void addDusts() {
        addToTag(ModTags.Items.DUSTS_COPPER, ModItems.copper_dust);
        addToTag(ModTags.Items.DUSTS_TIN, ModItems.tin_dust);
        addToTag(ModTags.Items.DUSTS_LEAD, ModItems.lead_dust);
        addToTag(ModTags.Items.DUSTS_SILVER, ModItems.silver_dust);
        addToTag(ModTags.Items.DUSTS_URANIUM, ModItems.uranium_dust);
        addToTag(ModTags.Items.DUSTS_NICKEL, ModItems.nickel_dust);
        addToTag(ModTags.Items.DUSTS_PLATINUM, ModItems.platinum_dust);
        addToTag(ModTags.Items.DUSTS_TITANIUM, ModItems.titanium_dust);
        addToTag(ModTags.Items.DUSTS_IRON, ModItems.iron_dust);
        addToTag(ModTags.Items.DUSTS_GOLD, ModItems.gold_dust);
        getItemBuilder(Tags.Items.DUSTS).add(ModTags.Items.DUSTS_COPPER, ModTags.Items.DUSTS_TIN, ModTags.Items.DUSTS_LEAD, ModTags.Items.DUSTS_SILVER, ModTags.Items.DUSTS_URANIUM, ModTags.Items.DUSTS_NICKEL, ModTags.Items.DUSTS_PLATINUM, ModTags.Items.DUSTS_TITANIUM, ModTags.Items.DUSTS_IRON, ModTags.Items.DUSTS_GOLD);
    }

    private void addOreDusts() {
        addToTag(ModTags.Items.ORE_DUSTS_COPPER, ModItems.copper_ore_dust);
        addToTag(ModTags.Items.ORE_DUSTS_TIN, ModItems.tin_ore_dust);
        addToTag(ModTags.Items.ORE_DUSTS_LEAD, ModItems.lead_ore_dust);
        addToTag(ModTags.Items.ORE_DUSTS_SILVER, ModItems.silver_ore_dust);
        addToTag(ModTags.Items.ORE_DUSTS_URANIUM, ModItems.uranium_ore_dust);
        addToTag(ModTags.Items.ORE_DUSTS_NICKEL, ModItems.nickel_ore_dust);
        addToTag(ModTags.Items.ORE_DUSTS_PLATINUM, ModItems.platinum_ore_dust);
        addToTag(ModTags.Items.ORE_DUSTS_TITANIUM, ModItems.titanium_ore_dust);
        addToTag(ModTags.Items.ORE_DUSTS_IRON, ModItems.iron_ore_dust);
        addToTag(ModTags.Items.ORE_DUSTS_GOLD, ModItems.gold_ore_dust);
        addToTag(ModTags.Items.ORE_DUSTS_OSMIUM, ModItems.osmium_ore_dust);
        getItemBuilder(ModTags.Items.ORE_DUSTS).add(ModTags.Items.ORE_DUSTS_COPPER, ModTags.Items.ORE_DUSTS_TIN, ModTags.Items.ORE_DUSTS_LEAD, ModTags.Items.ORE_DUSTS_SILVER, ModTags.Items.ORE_DUSTS_URANIUM, ModTags.Items.ORE_DUSTS_NICKEL, ModTags.Items.ORE_DUSTS_PLATINUM, ModTags.Items.ORE_DUSTS_TITANIUM, ModTags.Items.ORE_DUSTS_IRON, ModTags.Items.ORE_DUSTS_GOLD, ModTags.Items.ORE_DUSTS_OSMIUM);
    }
    
    private void addIngots() {
        getItemBuilder(Tags.Items.INGOTS).add(ModTags.Items.INGOTS_COPPER, ModTags.Items.INGOTS_TIN, ModTags.Items.INGOTS_LEAD, ModTags.Items.INGOTS_SILVER, ModTags.Items.INGOTS_URANIUM, ModTags.Items.INGOTS_NICKEL, ModTags.Items.INGOTS_PLATINUM, ModTags.Items.INGOTS_TITANIUM);
    }

    private void addOres() {
        addToTag(ModTags.Blocks.ORE_COPPER, ModBlocks.copper_ore);
        addToTag(ModTags.Blocks.ORE_TIN, ModBlocks.tin_ore);
        addToTag(ModTags.Blocks.ORE_LEAD, ModBlocks.lead_ore);
        addToTag(ModTags.Blocks.ORE_SILVER, ModBlocks.silver_ore);
        addToTag(ModTags.Blocks.ORE_URANIUM, ModBlocks.uranium_ore);
        addToTag(ModTags.Blocks.ORE_NICKEL, ModBlocks.nickel_ore);
        addToTag(ModTags.Blocks.ORE_PLATINUM, ModBlocks.platinum_ore);
        addToTag(ModTags.Blocks.ORE_TITANIUM, ModBlocks.titanium_ore);
        getBlockBuilder(Tags.Blocks.ORES).add(ModTags.Blocks.ORE_COPPER, ModTags.Blocks.ORE_TIN, ModTags.Blocks.ORE_LEAD, ModTags.Blocks.ORE_SILVER, ModTags.Blocks.ORE_URANIUM, ModTags.Blocks.ORE_NICKEL, ModTags.Blocks.ORE_PLATINUM, ModTags.Blocks.ORE_TITANIUM);
    }

}
