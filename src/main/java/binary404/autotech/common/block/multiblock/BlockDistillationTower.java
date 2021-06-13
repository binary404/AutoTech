package binary404.autotech.common.block.multiblock;

import binary404.autotech.client.renders.core.ICubeRenderer;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.tile.multiblock.TileDistillationTower;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockDistillationTower extends BlockMultiblock {

    public BlockDistillationTower() {
        super(Textures.STAINLESS_STEEL_CASING);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileDistillationTower();
    }
}
