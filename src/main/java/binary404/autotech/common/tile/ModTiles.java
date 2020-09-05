package binary404.autotech.common.tile;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.tile.device.TileWaterPump;
import binary404.autotech.common.tile.generator.TileSteamGenerator;
import binary404.autotech.common.tile.machine.TileCompactor;
import binary404.autotech.common.tile.machine.TileGrinder;
import binary404.autotech.common.tile.machine.TileSawMill;
import binary404.autotech.common.tile.machine.TileSmelter;
import binary404.autotech.common.tile.transfer.TileCable;
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
    public static TileEntityType<TileSteamGenerator> bio_generator;

    @ObjectHolder("autotech:grinder")
    public static TileEntityType<TileGrinder> grinder;

    @ObjectHolder("autotech:sawmill")
    public static TileEntityType<TileSawMill> sawmill;

    @ObjectHolder("autotech:cable")
    public static TileEntityType<TileCable> cable;

    @ObjectHolder("autotech:waterpump")
    public static TileEntityType<TileWaterPump> waterpump;

    @ObjectHolder("autotech:compactor")
    public static TileEntityType<TileCompactor> compactor;

    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> r = event.getRegistry();

        register(r, TileEntityType.Builder.create(TileSmelter::new, ModBlocks.lv_smelter, ModBlocks.mv_smelter, ModBlocks.hv_smelter, ModBlocks.ev_smelter, ModBlocks.iv_smelter, ModBlocks.maxv_smelter).build(null), "smelter");
        register(r, TileEntityType.Builder.create(TileSteamGenerator::new, ModBlocks.lv_bio_generator, ModBlocks.mv_bio_generator).build(null), "bio_generator");
        register(r, TileEntityType.Builder.create(TileGrinder::new, ModBlocks.lv_grinder, ModBlocks.mv_grinder, ModBlocks.hv_grinder, ModBlocks.ev_grinder, ModBlocks.iv_grinder, ModBlocks.maxv_grinder).build(null), "grinder");
        register(r, TileEntityType.Builder.create(TileSawMill::new, ModBlocks.lv_sawmill, ModBlocks.mv_sawmill, ModBlocks.hv_sawmill, ModBlocks.ev_sawmill, ModBlocks.iv_sawmill, ModBlocks.maxv_sawmill).build(null), "sawmill");
        register(r, TileEntityType.Builder.create(TileCable::new, ModBlocks.lv_cable, ModBlocks.mv_cable, ModBlocks.hv_cable, ModBlocks.ev_cable, ModBlocks.iv_cable, ModBlocks.maxv_cable).build(null), "cable");
        register(r, TileEntityType.Builder.create(TileWaterPump::new, ModBlocks.waterpump).build(null), "waterpump");
        register(r, TileEntityType.Builder.create(TileCompactor::new, ModBlocks.lv_compactor).build(null), "compactor");
    }

}
