package binary404.autotech.common.core.lib.multiblock;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.core.TileMachine;
import com.sun.org.apache.xpath.internal.operations.Mult;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public abstract class MultiBlockPart<T extends BlockTile> extends TileMachine<T> implements IMultiblockPart {

    private BlockPos controllerPos;
    private MultiblockControllerBase controllerTile;

    public MultiBlockPart(TileEntityType<?> type, Tier tier) {
        super(type, tier);
        initializeInventory();
    }

    public abstract void initializeInventory();

    public MultiblockControllerBase getController() {
        if (getWorld() != null && getWorld().isRemote) {
            if (controllerTile == null && controllerPos != null) {
                this.controllerTile = (MultiblockControllerBase) world.getTileEntity(controllerPos);
            }
        }
        return controllerTile;
    }

    private void setController(MultiblockControllerBase controller1) {
        this.controllerTile = controller1;
        if (controller1 != null)
            this.controllerPos = controller1.getPos();
        System.out.println(this.controllerTile);
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
}
