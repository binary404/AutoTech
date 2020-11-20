package binary404.autotech.common.tile;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.tile.device.TileWaterPump;
import binary404.autotech.common.tile.generator.TileSteamGenerator;
import binary404.autotech.common.tile.machine.*;
import binary404.autotech.common.tile.multiblock.TileArcFurnace;
import binary404.autotech.common.tile.multiblock.TileArcFurnaceHatch;
import binary404.autotech.common.tile.transfer.TileCable;
import binary404.autotech.common.tile.transfer.TileConveyor;
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

    @ObjectHolder("autotech:centrifuge")
    public static TileEntityType<TileCentrifuge> centrifuge;

    public static TileEntityType<TileDistillery> distillery;

    public static TileEntityType<TileConveyor> conveyor;

    public static TileEntityType<TileAssembler> assembler;

    public static TileEntityType<TileArcFurnace> blast_furnace;

    public static TileEntityType<TileArcFurnaceHatch> blast_furnace_hatch;

    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> r = event.getRegistry();

        register(r, TileEntityType.Builder.create(TileSmelter::new, ModBlocks.lv_smelter, ModBlocks.mv_smelter, ModBlocks.hv_smelter, ModBlocks.ev_smelter, ModBlocks.iv_smelter, ModBlocks.maxv_smelter).build(null), "smelter");
        register(r, TileEntityType.Builder.create(TileSteamGenerator::new, ModBlocks.lv_steam_generator, ModBlocks.mv_steam_generator).build(null), "bio_generator");
        register(r, TileEntityType.Builder.create(TileGrinder::new, ModBlocks.lv_grinder, ModBlocks.mv_grinder, ModBlocks.hv_grinder, ModBlocks.ev_grinder, ModBlocks.iv_grinder, ModBlocks.maxv_grinder).build(null), "grinder");
        register(r, TileEntityType.Builder.create(TileSawMill::new, ModBlocks.lv_sawmill, ModBlocks.mv_sawmill, ModBlocks.hv_sawmill, ModBlocks.ev_sawmill, ModBlocks.iv_sawmill, ModBlocks.maxv_sawmill).build(null), "sawmill");
        register(r, TileEntityType.Builder.create(TileCable::new, ModBlocks.lv_cable, ModBlocks.mv_cable, ModBlocks.hv_cable, ModBlocks.ev_cable, ModBlocks.iv_cable, ModBlocks.maxv_cable).build(null), "cable");
        register(r, TileEntityType.Builder.create(TileWaterPump::new, ModBlocks.waterpump).build(null), "waterpump");
        register(r, TileEntityType.Builder.create(TileCompactor::new, ModBlocks.lv_compactor).build(null), "compactor");
        register(r, TileEntityType.Builder.create(TileCentrifuge::new, ModBlocks.mv_centrifuge).build(null), "centrifuge");
        distillery = (TileEntityType<TileDistillery>) register(r, TileEntityType.Builder.create(TileDistillery::new, ModBlocks.mv_distillery).build(null), "distillery");
        conveyor = (TileEntityType<TileConveyor>) register(r, TileEntityType.Builder.create(TileConveyor::new, ModBlocks.conveyor).build(null), "conveyor");
        assembler = (TileEntityType<TileAssembler>) register(r, TileEntityType.Builder.create(TileAssembler::new, ModBlocks.mv_assembler).build(null), "assembler");
        blast_furnace = (TileEntityType<TileArcFurnace>) register(r, TileEntityType.Builder.create(TileArcFurnace::new, ModBlocks.lv_arc_furnace).build(null), "blast_furnace");
        blast_furnace_hatch = (TileEntityType<TileArcFurnaceHatch>) register(r, TileEntityType.Builder.create(TileArcFurnaceHatch::new, ModBlocks.arc_furnace_hatch).build(null), "blast_furnace_hatch");
    }

}
