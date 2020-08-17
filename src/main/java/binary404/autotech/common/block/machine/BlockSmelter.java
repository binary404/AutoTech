package binary404.autotech.common.block.machine;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.container.SmelterContainer;
import binary404.autotech.common.container.core.ContainerCore;
import binary404.autotech.common.tile.core.TileCore;
import binary404.autotech.common.tile.machine.TileSmelter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockSmelter extends BlockTile {

    public BlockSmelter(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public <T extends TileCore> ContainerCore getContainer(int id, PlayerInventory inventory, TileCore te, BlockRayTraceResult result) {
        return new SmelterContainer(id, inventory, (TileSmelter) te);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileSmelter();
    }
}
