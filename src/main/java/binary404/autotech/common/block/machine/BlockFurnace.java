package binary404.autotech.common.block.machine;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.core.TileFurnace;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockFurnace extends BlockTile {

    Tier tier;

    public BlockFurnace(Tier tier) {
        super();
        this.tier = tier;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileFurnace(this.tier);
    }

    public Tier getTier() {
        return tier;
    }
}
