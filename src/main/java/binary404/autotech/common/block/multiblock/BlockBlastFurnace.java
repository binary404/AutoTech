package binary404.autotech.common.block.multiblock;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.container.core.ContainerCore;
import binary404.autotech.common.container.multiblock.ContainerBlastFurnace;
import binary404.autotech.common.tile.core.TileCore;
import binary404.autotech.common.tile.multiblock.TileBlastFurnace;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockBlastFurnace extends BlockMultiBlock {

    public BlockBlastFurnace(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileBlastFurnace();
    }

    @Nullable
    @Override
    public <T extends TileCore> ContainerCore getContainer(int id, PlayerInventory inventory, TileCore te, BlockRayTraceResult result) {
        return new ContainerBlastFurnace(id, inventory, (TileBlastFurnace) te);
    }

    @Override
    protected Facing getFacing() {
        return Facing.HORIZONTAL;
    }
}
