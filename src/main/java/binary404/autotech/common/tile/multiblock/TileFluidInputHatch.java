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

public class TileFluidInputHatch extends MultiBlockPart<BlockMultiBlock> implements IMultiblockAbilityPart<Tank>, ITank {

    public TileFluidInputHatch() {
        super(ModTiles.fluid_input_hatch, Tier.LV);
    }

    @Override
    public void initializeInventory() {
        this.inv = new Inventory(0);
        this.energy = Energy.EMPTY;
        this.tank = new Tank(0);
    }

    @Override
    protected int postTick(World world) {
        if (isAttachedToMultiBlock() && ticks % 40 == 0) {
            getController().sync(4);
            this.tank = new TankProxy(getController().getTank());
        }
        return super.postTick(world);
    }

    @Override
    public void addToMultiBlock(MultiblockControllerBase controllerBase) {
        super.addToMultiBlock(controllerBase);
        this.tank = new TankProxy(controllerBase.getTank());
    }

    @Override
    public void removeFromMultiBlock(MultiblockControllerBase controllerBase) {
        super.removeFromMultiBlock(controllerBase);
        this.tank = new Tank(0);
    }

    @Override
    public MultiblockAbility<Tank> getAbility() {
        return MultiblockAbility.IMPORT_FLUIDS;
    }

    @Override
    public void registerAbilities(List<Tank> abilityList) {
        abilityList.add(this.tank);
    }
}
