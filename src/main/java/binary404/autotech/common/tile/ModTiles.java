package binary404.autotech.common.tile;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.tile.generator.TileBioGenerator;
import binary404.autotech.common.tile.machine.TileSmelter;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.autotech.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = AutoTech.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTiles {

    @ObjectHolder("autotech:smelter")
    public static TileEntityType<TileSmelter> smelter;

    @ObjectHolder("autotech:bio_generator")
    public static TileEntityType<TileBioGenerator> bio_generator;

    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> r = event.getRegistry();

        register(r, TileEntityType.Builder.create(TileSmelter::new, ModBlocks.smelter).build(null), "smelter");
        register(r, TileEntityType.Builder.create(TileBioGenerator::new, ModBlocks.lv_bio_generator, ModBlocks.mv_bio_generator).build(null), "bio_generator");
    }

}
