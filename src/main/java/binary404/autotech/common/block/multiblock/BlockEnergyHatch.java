package binary404.autotech.common.block.multiblock;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.multiblock.TileEnergyHatch;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockEnergyHatch extends BlockTile {

    boolean isExportHatch;
    Tier tier;

    public BlockEnergyHatch(boolean isExportHatch, Tier tier) {
        super();
        this.isExportHatch = isExportHatch;
        this.tier = tier;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEnergyHatch(isExportHatch, tier);
    }
}
