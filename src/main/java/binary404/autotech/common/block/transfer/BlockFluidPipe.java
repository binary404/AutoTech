package binary404.autotech.common.block.transfer;

import binary404.autotech.common.block.transfer.shape.PipeShapeCache;
import binary404.autotech.common.tile.transfer.fluid.FluidPipeType;
import binary404.autotech.common.tile.transfer.fluid.TileFluidPipe;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class BlockFluidPipe extends BlockPipe {

    private final FluidPipeType type;

    public BlockFluidPipe(PipeShapeCache cache,  FluidPipeType type) {
        super(cache);

        this.type = type;
    }

    public FluidPipeType getType() {
        return type;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileFluidPipe(type);
    }

    @Override
    protected boolean hasConnection(IWorld world, BlockPos pos, Direction direction) {
        TileEntity currentTile = world.getTileEntity(pos);
        if (currentTile instanceof TileFluidPipe &&
                ((TileFluidPipe) currentTile).getAttachmentManager().hasAttachment(direction)) {
            return false;
        }

        BlockState facingState = world.getBlockState(pos.offset(direction));
        TileEntity facingTile = world.getTileEntity(pos.offset(direction));

        if (facingTile instanceof TileFluidPipe &&
                ((TileFluidPipe) facingTile).getAttachmentManager().hasAttachment(direction.getOpposite())) {
            return false;
        }

        return facingState.getBlock() instanceof BlockFluidPipe
                && ((BlockFluidPipe) facingState.getBlock()).getType() == type;
    }

    @Override
    protected boolean hasInvConnection(IWorld world, BlockPos pos, Direction direction) {
        TileEntity facingTile = world.getTileEntity(pos.offset(direction));

        return facingTile != null
                && facingTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).isPresent();
    }

}
