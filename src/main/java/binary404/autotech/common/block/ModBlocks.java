package binary404.autotech.common.block;

import binary404.autotech.AutoTech;
import binary404.autotech.common.item.ModItems;
import binary404.autotech.common.tile.TileSmelter;
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

    @ObjectHolder("autotech:smelter")
    public static BlockTile smelter;

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
    }

}
