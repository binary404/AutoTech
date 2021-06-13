package binary404.autotech.common.block.multiblock;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.multiblock.TileItemHatch;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockItemHatch extends BlockTile {

    public static List<BlockItemHatch> itemHatches = new ArrayList<>();

    boolean isExportHatch;
    Tier tier;

    public BlockItemHatch(boolean isExportHatch, Tier tier) {
        super();
        this.isExportHatch = isExportHatch;
        this.tier = tier;
        itemHatches.add(this);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileItemHatch(isExportHatch);
    }

    public Tier getTier() {
        return tier;
    }
}
