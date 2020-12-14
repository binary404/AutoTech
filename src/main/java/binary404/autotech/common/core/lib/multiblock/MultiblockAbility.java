package binary404.autotech.common.core.lib.multiblock;

import binary404.autotech.common.core.logistics.energy.Energy;
import binary404.autotech.common.core.logistics.item.Inventory;
import binary404.autotech.common.core.logistics.fluid.Tank;

public class MultiblockAbility<T> {

    public static final MultiblockAbility<Inventory> EXPORT_ITEMS = new MultiblockAbility<>();
    public static final MultiblockAbility<Inventory> IMPORT_ITEMS = new MultiblockAbility<>();

    public static final MultiblockAbility<Tank> EXPORT_FLUIDS = new MultiblockAbility<>();
    public static final MultiblockAbility<Tank> IMPORT_FLUIDS = new MultiblockAbility<>();

    public static final MultiblockAbility<Energy> INPUT_ENERGY = new MultiblockAbility<>();
    public static final MultiblockAbility<Energy> OUTPUT_ENERGY = new MultiblockAbility<>();

}
