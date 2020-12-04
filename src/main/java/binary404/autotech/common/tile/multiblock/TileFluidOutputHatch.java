package binary404.autotech.common.tile.multiblock;

import binary404.autotech.common.block.multiblock.BlockMultiBlock;
import binary404.autotech.common.core.lib.multiblock.IMultiblockAbilityPart;
import binary404.autotech.common.core.lib.multiblock.MultiBlockPart;
import binary404.autotech.common.core.lib.multiblock.MultiblockAbility;
import binary404.autotech.common.core.lib.multiblock.MultiblockControllerBase;
import binary404.autotech.common.core.logistics.*;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.util.ITank;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;

import java.util.List;

public class TileFluidOutputHatch extends MultiBlockPart<BlockMultiBlock> implements IMultiblockAbilityPart<Tank>, ITank {

    public TileFluidOutputHatch() {
        super(ModTiles.fluid_output_hatch, Tier.LV);
    }

    @Override
    protected int postTick(World world) {
        if (isAttachedToMultiBlock() && ticks % 40 == 0) {
            getController().sync(4);
            if (getController() instanceof IOutputTank)
                this.tank = new TankProxy(((IOutputTank) getController()).getOutputTank());

        }
        return -1;
    }


    @Override
    public void initializeInventory() {
        this.inv = new Inventory(0);
        this.energy = Energy.EMPTY;
        this.tank = new Tank(0);
    }

    @Override
    public void addToMultiBlock(MultiblockControllerBase controllerBase) {
        super.addToMultiBlock(controllerBase);
        if (controllerBase instanceof IOutputTank) {
            this.tank = new TankProxy(((IOutputTank) controllerBase).getOutputTank());
        }
    }

    @Override
    public void removeFromMultiBlock(MultiblockControllerBase controllerBase) {
        super.removeFromMultiBlock(controllerBase);
        this.tank = new Tank(0);
    }

    @Override
    public MultiblockAbility<Tank> getAbility() {
        return MultiblockAbility.EXPORT_FLUIDS;
    }

    @Override
    public void registerAbilities(List<Tank> abilityList) {
        abilityList.add(this.tank);
    }
}
