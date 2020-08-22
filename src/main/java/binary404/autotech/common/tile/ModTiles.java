package binary404.autotech.common.tile;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.tile.generator.TileBioGenerator;
import binary404.autotech.common.tile.machine.TileGrinder;
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

    @ObjectHolder("autotech:grinder")
    public static TileEntityType<TileGrinder> grinder;

    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> r = event.getRegistry();

        register(r, TileEntityType.Builder.create(TileSmelter::new, ModBlocks.lv_smelter, ModBlocks.mv_smelter, ModBlocks.hv_smelter, ModBlocks.ev_smelter, ModBlocks.iv_smelter, ModBlocks.maxv_smelter).build(null), "smelter");
        register(r, TileEntityType.Builder.create(TileBioGenerator::new, ModBlocks.lv_bio_generator, ModBlocks.mv_bio_generator).build(null), "bio_generator");
        register(r, TileEntityType.Builder.create(TileGrinder::new, ModBlocks.lv_grinder, ModBlocks.mv_grinder, ModBlocks.hv_grinder, ModBlocks.ev_grinder, ModBlocks.iv_grinder, ModBlocks.maxv_grinder).build(null), "grinder");
    }

}
