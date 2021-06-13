package binary404.autotech.common.block.multiblock;

import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.tile.multiblock.TileLargeBoiler;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockLargeBoiler extends BlockMultiblock {

    TileLargeBoiler.BoilerType type;

    public BlockLargeBoiler(TileLargeBoiler.BoilerType type) {
        super(Textures.BRONZE_BRICKS);
        this.type = type;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileLargeBoiler(type);
    }

    public TileLargeBoiler.BoilerType getType() {
        return type;
    }
}
