package binary404.autotech.common.tile.transfer.item;

import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.transfer.TilePipe;
import binary404.autotech.common.tile.transfer.pipe.item.ItemTransport;
import binary404.autotech.common.tile.transfer.pipe.item.ItemTransportProps;
import binary404.autotech.common.tile.transfer.pipe.Pipe;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TileItemPipe extends TilePipe implements ITickableTileEntity {

    private List<ItemTransportProps> props = new ArrayList<>();

    private final ItemPipeType type;

    public TileItemPipe(ItemPipeType type) {
        super(type.getTileType());

        this.type = type;
    }

    @Override
    public void tick() {
        if (world.isRemote) {
            props.forEach(ItemTransportProps::tick);
        }
    }

    public List<ItemTransportProps> getProps() {
        return props;
    }

    public void setProps(List<ItemTransportProps> props) {
        this.props = props;
    }

    @Override
    protected void spawnDrops(Pipe pipe) {
        super.spawnDrops(pipe);

        for (ItemTransport transport : ((ItemPipe) pipe).getTransports()) {
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), transport.getValue());
        }
    }

    @Override
    protected Pipe createPipe(World world, BlockPos pos) {
        return new ItemPipe(world, pos, type);
    }
}
