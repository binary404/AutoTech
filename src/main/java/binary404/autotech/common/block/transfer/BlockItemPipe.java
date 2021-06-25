package binary404.autotech.common.block.transfer;

import binary404.autotech.common.block.transfer.shape.PipeShapeCache;
import binary404.autotech.common.tile.transfer.item.ItemPipeType;
import binary404.autotech.common.tile.transfer.item.TileItemPipe;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class BlockItemPipe extends BlockPipe {

    private final ItemPipeType type;

    public BlockItemPipe(PipeShapeCache shapeCache, ItemPipeType type) {
        super(shapeCache);

        this.type = type;
    }

    public ItemPipeType getType() {
        return type;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileItemPipe(type);
    }

    @Override
    protected boolean hasConnection(IWorld world, BlockPos pos, Direction direction) {
        TileEntity currentTile = world.getTileEntity(pos);
        if (currentTile instanceof TileItemPipe &&
                ((TileItemPipe) currentTile).getAttachmentManager().hasAttachment(direction)) {
            return false;
        }

        BlockState facingState = world.getBlockState(pos.offset(direction));
        TileEntity facingTile = world.getTileEntity(pos.offset(direction));

        if (facingTile instanceof TileItemPipe &&
                ((TileItemPipe) facingTile).getAttachmentManager().hasAttachment(direction.getOpposite())) {
            return false;
        }

        return facingState.getBlock() instanceof BlockItemPipe;
    }

    @Override
    protected boolean hasInvConnection(IWorld world, BlockPos pos, Direction direction) {
        TileEntity facingTile = world.getTileEntity(pos.offset(direction));

        return facingTile != null
                && facingTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite()).isPresent();
    }

}
