package binary404.autotech.common.tile.core;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.util.ServerUtil;
import binary404.autotech.common.tile.core.TileCore;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;

public class TileTickable extends TileCore implements ITickableTileEntity {

    private int syncTicks;
    public int ticks;

    public TileTickable(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void tick() {
        final World world = this.world;
        if (world != null) {
            if (this.ticks == 0) {
                onFirstTick(world);
            }
            if (doPostTicks(world)) {
                int i = postTick(world);
                if (i > -1 && !isRemote()) {
                    sync(i);
                }
            }
            this.ticks++;
            if (!isRemote()) {
                if (this.syncTicks > -1)
                    this.syncTicks--;
                if (this.syncTicks == 0)
                    sync();
            } else {
                clientTick(world);
            }
        }
    }

    protected void onFirstTick(World world) {
    }

    protected boolean doPostTicks(World world) {
        return true;
    }

    protected int postTick(World world) {
        return -1;
    }

    protected void clientTick(World world) {
    }

    public void sync(int delay) {
        if (!isRemote()) {
            if (this.syncTicks <= 0 || delay < this.syncTicks) {
                this.syncTicks = ServerUtil.isSinglePlayer() ? 2 : delay;
            }
        }
        this.sync();
    }

}
