package binary404.autotech.common.core;

import binary404.autotech.AutoTech;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;

public class GrinderManager {

    public static ArrayList<Item> ores = new ArrayList<>();
    public static ArrayList<Item> dusts = new ArrayList<>();

    public static final HashMap<Item, Item> basicGrinding = new HashMap<>();

    public static void init() {
        AutoTech.LOGGER.error(dusts);
        AutoTech.LOGGER.error(ores);

        for (Item ore : ores) {
            for (Item dust : dusts) {
                String dustName = dust.getRegistryName().getPath();
                dustName = dustName.replace("_dust", "");
                String oreName = ore.getRegistryName().getPath();
                if (oreName.equals(dustName)) {
                    basicGrinding.put(ore, dust);
                }
            }
        }

        AutoTech.LOGGER.error(basicGrinding);
    }

}
