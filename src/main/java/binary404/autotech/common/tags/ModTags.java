package binary404.autotech.common.tags;

import binary404.autotech.AutoTech;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {

    public static class Items {

        public static final ITag.INamedTag<Item> MV_CIRCUITS = tag("mv_circuits");
        public static final ITag.INamedTag<Item> MV_CIRCUITS_LOGIC = tag("mv_circuits/logic");
        public static final ITag.INamedTag<Item> MV_CIRCUITS_TRANSMITTER = tag("mv_circuits/transmitter");
        public static final ITag.INamedTag<Item> MV_CIRCUITS_RECEIVER = tag("mv_circuits/receiver");

        public static final ITag.INamedTag<Item> PLATES = forgeTag("plates");
        public static final ITag.INamedTag<Item> PLATES_COPPER = forgeTag("plates/copper");
        public static final ITag.INamedTag<Item> PLATES_TIN = forgeTag("plates/tin");
        public static final ITag.INamedTag<Item> PLATES_LEAD = forgeTag("plates/lead");
        public static final ITag.INamedTag<Item> PLATES_SILVER = forgeTag("plates/silver");
        public static final ITag.INamedTag<Item> PLATES_URANIUM = forgeTag("plates/uranium");
        public static final ITag.INamedTag<Item> PLATES_NICKEL = forgeTag("plates/nickel");
        public static final ITag.INamedTag<Item> PLATES_PLATINUM = forgeTag("plates/platinum");
        public static final ITag.INamedTag<Item> PLATES_TITANIUM = forgeTag("plates/titanium");
        public static final ITag.INamedTag<Item> PLATES_IRON = forgeTag("plates/iron");
        public static final ITag.INamedTag<Item> PLATES_BRONZE = forgeTag("plates/bronze");
        public static final ITag.INamedTag<Item> PLATES_STEEL = forgeTag("plates/steel");

        public static final ITag.INamedTag<Item> INGOTS_COPPER = forgeTag("ingots/copper");
        public static final ITag.INamedTag<Item> INGOTS_TIN = forgeTag("ingots/tin");
        public static final ITag.INamedTag<Item> INGOTS_LEAD = forgeTag("ingots/lead");
        public static final ITag.INamedTag<Item> INGOTS_SILVER = forgeTag("ingots/silver");
        public static final ITag.INamedTag<Item> INGOTS_URANIUM = forgeTag("ingots/uranium");
        public static final ITag.INamedTag<Item> INGOTS_NICKEL = forgeTag("ingots/nickel");
        public static final ITag.INamedTag<Item> INGOTS_PLATINUM = forgeTag("ingots/platinum");
        public static final ITag.INamedTag<Item> INGOTS_TITANIUM = forgeTag("ingots/titanium");
        public static final ITag.INamedTag<Item> INGOTS_RED_ALLOY = forgeTag("ingots/red_alloy");
        public static final ITag.INamedTag<Item> INGOTS_BRONZE = forgeTag("ingots/bronze");
        public static final ITag.INamedTag<Item> INGOTS_STEEL = forgeTag("ingots/steel");

        public static final ITag.INamedTag<Item> DUSTS_COPPER = forgeTag("dusts/copper");
        public static final ITag.INamedTag<Item> DUSTS_TIN = forgeTag("dusts/tin");
        public static final ITag.INamedTag<Item> DUSTS_LEAD = forgeTag("dusts/lead");
        public static final ITag.INamedTag<Item> DUSTS_SILVER = forgeTag("dusts/silver");
        public static final ITag.INamedTag<Item> DUSTS_URANIUM = forgeTag("dusts/uranium");
        public static final ITag.INamedTag<Item> DUSTS_NICKEL = forgeTag("dusts/nickel");
        public static final ITag.INamedTag<Item> DUSTS_PLATINUM = forgeTag("dusts/platinum");
        public static final ITag.INamedTag<Item> DUSTS_TITANIUM = forgeTag("dusts/titanium");
        public static final ITag.INamedTag<Item> DUSTS_IRON = forgeTag("dusts/iron");
        public static final ITag.INamedTag<Item> DUSTS_GOLD = forgeTag("dusts/gold");
        public static final ITag.INamedTag<Item> DUSTS_NETHERITE = forgeTag("dusts/netherite");
        public static final ITag.INamedTag<Item> DUSTS_RED_ALLOY = forgeTag("dusts/red_alloy");
        public static final ITag.INamedTag<Item> DUSTS_BRONZE = forgeTag("dusts/bronze");
        public static final ITag.INamedTag<Item> DUSTS_COAL = forgeTag("dusts/coal");
        public static final ITag.INamedTag<Item> DUSTS_CHARCOAL = forgeTag("dusts/charcoal");

        public static final ITag.INamedTag<Item> ORE_DUSTS = forgeTag("ore_dusts");
        public static final ITag.INamedTag<Item> ORE_DUSTS_COPPER = forgeTag("ore_dusts/copper");
        public static final ITag.INamedTag<Item> ORE_DUSTS_TIN = forgeTag("ore_dusts/tin");
        public static final ITag.INamedTag<Item> ORE_DUSTS_LEAD = forgeTag("ore_dusts/lead");
        public static final ITag.INamedTag<Item> ORE_DUSTS_SILVER = forgeTag("ore_dusts/silver");
        public static final ITag.INamedTag<Item> ORE_DUSTS_URANIUM = forgeTag("ore_dusts/uranium");
        public static final ITag.INamedTag<Item> ORE_DUSTS_NICKEL = forgeTag("ore_dusts/nickel");
        public static final ITag.INamedTag<Item> ORE_DUSTS_PLATINUM = forgeTag("ore_dusts/platinum");
        public static final ITag.INamedTag<Item> ORE_DUSTS_TITANIUM = forgeTag("ore_dusts/titanium");
        public static final ITag.INamedTag<Item> ORE_DUSTS_IRON = forgeTag("ore_dusts/iron");
        public static final ITag.INamedTag<Item> ORE_DUSTS_GOLD = forgeTag("ore_dusts/gold");
        public static final ITag.INamedTag<Item> ORE_DUSTS_OSMIUM = forgeTag("ore_dusts/osmium");
        public static final ITag.INamedTag<Item> ORE_DUSTS_NETHERITE = forgeTag("ore_dusts/netherite");

        public static final ITag.INamedTag<Item> ORES_COPPER = forgeTag("ores/copper");
        public static final ITag.INamedTag<Item> ORES_TIN = forgeTag("ores/tin");
        public static final ITag.INamedTag<Item> ORES_LEAD = forgeTag("ores/lead");
        public static final ITag.INamedTag<Item> ORES_SILVER = forgeTag("ores/silver");
        public static final ITag.INamedTag<Item> ORES_URANIUM = forgeTag("ores/uranium");
        public static final ITag.INamedTag<Item> ORES_NICKEL = forgeTag("ores/nickel");
        public static final ITag.INamedTag<Item> ORES_PLATINUM = forgeTag("ores/platinum");
        public static final ITag.INamedTag<Item> ORES_TITANIUM = forgeTag("ores/titanium");

        public static final ITag.INamedTag<Item> RAW = forgeTag("raw");
        public static final ITag.INamedTag<Item> RAW_COPPER = forgeTag("raw/copper");
        public static final ITag.INamedTag<Item> RAW_TIN = forgeTag("raw/tin");
        public static final ITag.INamedTag<Item> RAW_LEAD = forgeTag("raw/lead");
        public static final ITag.INamedTag<Item> RAW_SILVER = forgeTag("raw/silver");
        public static final ITag.INamedTag<Item> RAW_URANIUM = forgeTag("raw/uranium");
        public static final ITag.INamedTag<Item> RAW_NICKEL = forgeTag("raw/nickel");
        public static final ITag.INamedTag<Item> RAW_PLATINUM = forgeTag("raw/platinum");
        public static final ITag.INamedTag<Item> RAW_TITANIUM = forgeTag("raw/titanium");

        private static ITag.INamedTag<Item> tag(String name) {
            return ItemTags.makeWrapperTag(AutoTech.key(name).toString());
        }

        private static ITag.INamedTag<Item> forgeTag(String name) {
            return ItemTags.makeWrapperTag("forge:" + name);
        }
    }

    public static class Blocks {
        public static final ITag.INamedTag<Block> ORE_COPPER = forgeTag("ores/copper");
        public static final ITag.INamedTag<Block> ORE_TIN = forgeTag("ores/tin");
        public static final ITag.INamedTag<Block> ORE_LEAD = forgeTag("ores/lead");
        public static final ITag.INamedTag<Block> ORE_SILVER = forgeTag("ores/silver");
        public static final ITag.INamedTag<Block> ORE_URANIUM = forgeTag("ores/uranium");
        public static final ITag.INamedTag<Block> ORE_NICKEL = forgeTag("ores/nickel");
        public static final ITag.INamedTag<Block> ORE_PLATINUM = forgeTag("ores/platinum");
        public static final ITag.INamedTag<Block> ORE_TITANIUM = forgeTag("ores/titanium");

        private static ITag.INamedTag<Block> forgeTag(String name) {
            return BlockTags.makeWrapperTag("forge:" + name);
        }

        private static ITag.INamedTag<Block> tag(String name) {
            return BlockTags.makeWrapperTag(AutoTech.key(name).toString());
        }
    }

}
