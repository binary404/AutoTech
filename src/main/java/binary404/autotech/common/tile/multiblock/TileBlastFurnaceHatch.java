package binary404.autotech.common.tile.multiblock;

import binary404.autotech.common.block.multiblock.BlockBlastFurnace;
import binary404.autotech.common.block.multiblock.BlockBlastFurnaceHatch;
import binary404.autotech.common.core.lib.multiblock.MultiBlockPart;
import binary404.autotech.common.core.lib.multiblock.MultiblockControllerBase;
import binary404.autotech.common.core.logistics.Energy;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.ModTiles;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;

public class TileBlastFurnaceHatch extends MultiBlockPart<BlockBlastFurnaceHatch> {

    public TileBlastFurnaceHatch() {
        super(ModTiles.blast_furnace_hatch, Tier.MV);
    }

    @Override
    protected int postTick(World world) {
        if (isAttachedToMultiBlock()) {
            MultiblockControllerBase controller = getController();
            Energy storage = controller.getEnergy();
            this.energy.consume(storage.receiveEnergy(this.energy.getEnergyStored(), false));
            controller.sync(4);
        }
        return -1;
    }

    @Override
    public void initializeInventory() {
        this.inv.set(0);
    }

    @Override
    public void addToMultiBlock(MultiblockControllerBase controllerBase) {
        super.addToMultiBlock(controllerBase);
        this.inv.set(1);
    }

    @Override
    public void removeFromMultiBlock(MultiblockControllerBase controllerBase) {
        super.removeFromMultiBlock(controllerBase);
        this.inv.set(0);
    }
}
