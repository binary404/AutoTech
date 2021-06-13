package binary404.autotech.common.block.multiblock;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.multiblock.TileEnergyHatch;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockEnergyHatch extends BlockTile {

    public static List<BlockEnergyHatch> energyHatches = new ArrayList<>();

    boolean isExportHatch;
    Tier tier;

    public BlockEnergyHatch(boolean isExportHatch, Tier tier) {
        super();
        this.isExportHatch = isExportHatch;
        this.tier = tier;
        energyHatches.add(this);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEnergyHatch(isExportHatch, tier);
    }

    public Tier getTier() {
        return tier;
    }
}
