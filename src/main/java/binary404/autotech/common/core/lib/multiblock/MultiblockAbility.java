package binary404.autotech.common.core.lib.multiblock;

import binary404.autotech.common.core.logistics.energy.Energy;
import binary404.autotech.common.core.logistics.item.Inventory;
import binary404.autotech.common.core.logistics.fluid.Tank;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class MultiblockAbility<T> {

    public static final MultiblockAbility<IItemHandlerModifiable> EXPORT_ITEMS = new MultiblockAbility<>();
    public static final MultiblockAbility<IItemHandlerModifiable> IMPORT_ITEMS = new MultiblockAbility<>();

    public static final MultiblockAbility<IFluidTank> EXPORT_FLUIDS = new MultiblockAbility<>();
    public static final MultiblockAbility<IFluidTank> IMPORT_FLUIDS = new MultiblockAbility<>();

    public static final MultiblockAbility<IEnergyStorage> INPUT_ENERGY = new MultiblockAbility<>();
    public static final MultiblockAbility<IEnergyStorage> OUTPUT_ENERGY = new MultiblockAbility<>();

}
