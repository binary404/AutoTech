package binary404.autotech.common.block.machine;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.container.core.ContainerCore;
import binary404.autotech.common.container.machine.CompactorContainer;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.core.TileCore;
import binary404.autotech.common.tile.machine.TileCompactor;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockCompactor extends BlockTile {

    Tier tier;

    public BlockCompactor(Tier tier) {
        super();
        this.tier = tier;
        setDefaultState();
    }

    @Nullable
    @Override
    public <T extends TileCore> ContainerCore getContainer(int id, PlayerInventory inventory, TileCore te, BlockRayTraceResult result) {
        return new CompactorContainer(id, inventory, (TileCompactor) te);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileCompactor(tier);
    }

    @Override
    protected Facing getFacing() {
        return Facing.HORIZONTAL;
    }
}
