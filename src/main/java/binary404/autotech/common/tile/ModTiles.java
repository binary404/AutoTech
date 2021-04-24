package binary404.autotech.common.tile;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.block.machine.BlockMachine;
import binary404.autotech.common.tile.core.TileSimpleGenerator;
import binary404.autotech.common.tile.core.TileSimpleMachine;
import binary404.autotech.common.tile.multiblock.*;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import static binary404.autotech.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = AutoTech.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTiles {

    public static TileEntityType<TileItemHatch> item_hatch;
    public static TileEntityType<TileFluidHatch> fluid_hatch;
    public static TileEntityType<TileEnergyHatch> energy_hatch;

    public static TileEntityType<TileSimpleMachine> simple_machine;
    public static TileEntityType<TileSimpleGenerator> simple_generator;

    public static TileEntityType<TileDistillationTower> distillation_tower;

    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> r = event.getRegistry();

        item_hatch = (TileEntityType<TileItemHatch>) register(r, TileEntityType.Builder.create(TileItemHatch::new, ModBlocks.lv_item_input_hatch, ModBlocks.lv_item_output_hatch).build(null), "item_hatch");
        fluid_hatch = (TileEntityType<TileFluidHatch>) register(r, TileEntityType.Builder.create(TileFluidHatch::new, ModBlocks.lv_fluid_input_hatch, ModBlocks.lv_fluid_output_hatch).build(null), "fluid_hatch");
        energy_hatch = (TileEntityType<TileEnergyHatch>) register(r, TileEntityType.Builder.create(TileEnergyHatch::new, ModBlocks.lv_energy_input_hatch, ModBlocks.lv_energy_output_hatch).build(null), "energy_hatch");

        simple_machine = (TileEntityType<TileSimpleMachine>) register(r, TileEntityType.Builder.create(TileSimpleMachine::new, BlockMachine.machines.toArray(new Block[0])).build(null), "simple_machine");
        simple_generator = (TileEntityType<TileSimpleGenerator>) register(r, TileEntityType.Builder.create(TileSimpleGenerator::new, ModBlocks.test).build(null), "simple_generator");

        distillation_tower = (TileEntityType<TileDistillationTower>) register(r, TileEntityType.Builder.create(TileDistillationTower::new, ModBlocks.distillation_tower).build(null), "distillery");
    }

}
