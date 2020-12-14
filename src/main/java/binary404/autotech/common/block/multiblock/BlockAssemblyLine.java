package binary404.autotech.common.block.multiblock;

import binary404.autotech.common.container.core.ContainerCore;
import binary404.autotech.common.container.multiblock.AssemblyLineContainer;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.core.TileCore;
import binary404.autotech.common.tile.multiblock.TileAssemblyLine;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockAssemblyLine extends BlockMultiBlock {

    Tier tier;

    public BlockAssemblyLine(Tier tier) {
        super();
        this.tier = tier;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileAssemblyLine(tier);
    }

    @Nullable
    @Override
    public <T extends TileCore> ContainerCore getContainer(int id, PlayerInventory inventory, TileCore te, BlockRayTraceResult result) {
        return new AssemblyLineContainer(id, inventory, (TileAssemblyLine) te);
    }

    @Override
    protected Facing getFacing() {
        return Facing.HORIZONTAL;
    }
}
