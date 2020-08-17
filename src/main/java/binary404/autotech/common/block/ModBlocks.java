package binary404.autotech.common.block;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.generator.BlockBioGenerator;
import binary404.autotech.common.block.machine.BlockSmelter;
import binary404.autotech.common.core.GrinderManager;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
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

    @ObjectHolder("autotech:smelter")
    public static BlockTile smelter;

    public static Block lv_bio_generator;

    public static Block mv_bio_generator;

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

        register(r, new BlockSmelter(p), "smelter");
        lv_bio_generator = register(r, new BlockBioGenerator(p, Tier.LV), "lv_bio_generator");
        mv_bio_generator = register(r, new BlockBioGenerator(p, Tier.MV), "mv_bio_generator");
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

        register(r, new BlockItem(smelter, ModItems.properties), "smelter");
        register(r, new BlockItem(lv_bio_generator, ModItems.properties), "lv_bio_generator");
        register(r, new BlockItem(mv_bio_generator, ModItems.properties), "mv_bio_generator");

        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            if (block instanceof OreBlock) {
                Item item = Item.getItemFromBlock(block);
                if (item != null) {
                    GrinderManager.ores.add(item);
                }
            }
        }

        for (Item item : GrinderManager.ores) {
            if (item == Items.COAL_ORE || item == Items.LAPIS_ORE || item == Items.NETHER_QUARTZ_ORE || item == Items.DIAMOND_ORE || item == Items.NETHER_GOLD_ORE || item == Items.EMERALD_ORE)
                continue;
            Item register = register(r, new Item(ModItems.properties), item.getRegistryName().getPath() + "_dust");
            GrinderManager.dusts.add(register);
        }
    }

}
