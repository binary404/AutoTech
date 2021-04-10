package binary404.autotech.common.core.lib.multiblock;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.core.TileMachine;
import binary404.autotech.common.tile.util.ITank;
import com.sun.org.apache.xpath.internal.operations.Mult;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class MultiBlockPart extends TileMachine implements IMultiblockPart {

    private BlockPos controllerPos;
    private MultiblockControllerBase controllerTile;

    public MultiBlockPart(TileEntityType<?> type, Tier tier) {
        super(type, tier);
        initializeInventory();
    }

    @Override
    protected int postTick(World world) {
        return -1;
    }

    public abstract void initializeInventory();

    public MultiblockControllerBase getController() {
        if (getWorld() != null && getWorld().isRemote) {
            if (controllerTile == null && controllerPos != null) {
                setController((MultiblockControllerBase) world.getTileEntity(controllerPos));
            }
        }
        return controllerTile;
    }

    private void setController(MultiblockControllerBase controller1) {
        this.controllerTile = controller1;
        if (controller1 != null) {
            this.controllerPos = controller1.getPos();
        }
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        if (controllerPos != null) {
            nbt.putInt("controller_x", controllerPos.getX());
            nbt.putInt("controller_y", controllerPos.getY());
            nbt.putInt("controller_z", controllerPos.getZ());
        }
        return super.writeSync(nbt);
    }

    @Override
    public void readSync(CompoundNBT nbt) {
        super.readSync(nbt);
        int x = nbt.getInt("controller_x");
        int y = nbt.getInt("controller_y");
        int z = nbt.getInt("controller_z");
        BlockPos pos = new BlockPos(x, y, z);
        this.controllerPos = pos;
        this.controllerTile = null;
    }

    @Override
    public void addToMultiBlock(MultiblockControllerBase controllerBase) {
        setController(controllerBase);
    }

    @Override
    public void removeFromMultiBlock(MultiblockControllerBase controllerBase) {
        setController(null);
    }

    @Override
    public boolean isAttachedToMultiBlock() {
        return getController() != null;
    }

    @Override
    public boolean dropNbt() {
        return false;
    }
}
