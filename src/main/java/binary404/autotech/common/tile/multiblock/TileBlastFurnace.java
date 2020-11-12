package binary404.autotech.common.tile.multiblock;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.block.multiblock.BlockBlastFurnace;
import binary404.autotech.common.core.lib.multiblock.BlockPattern;
import binary404.autotech.common.core.lib.multiblock.FactoryBlockPattern;
import binary404.autotech.common.core.lib.multiblock.MultiblockControllerBase;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;

public class TileBlastFurnace extends MultiblockControllerBase<BlockBlastFurnace> {

    public TileBlastFurnace() {
        super(ModTiles.blast_furnace, Tier.MV);
        this.inv.set(2);
    }

    @Override
    protected void updateFormedValid() {

    }

    protected BlockState getCasingState() {
        return ModBlocks.iron_plating.getDefaultState();
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXX", "XXX", "XXX")
                .aisle("XXX", "XXX", "XXX")
                .aisle("XXX", "XYX", "XXX")
                .where('X', tilePredicate((state, tile) -> tile instanceof TileBlastFurnaceHatch).or(statePredicate(getCasingState())))
                .where('Y', selfPredicate())
                .build();
    }
}
