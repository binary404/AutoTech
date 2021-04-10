package binary404.autotech.common.tile.multiblock;

import binary404.autotech.common.block.multiblock.BlockMultiBlock;
import binary404.autotech.common.core.lib.multiblock.IMultiblockAbilityPart;
import binary404.autotech.common.core.lib.multiblock.MultiBlockPart;
import binary404.autotech.common.core.lib.multiblock.MultiblockAbility;
import binary404.autotech.common.core.lib.multiblock.MultiblockControllerBase;
import binary404.autotech.common.core.logistics.*;
import binary404.autotech.common.core.logistics.energy.Energy;
import binary404.autotech.common.core.logistics.energy.EnergyProxy;
import binary404.autotech.common.core.logistics.fluid.Tank;
import binary404.autotech.common.core.logistics.item.Inventory;
import binary404.autotech.common.tile.ModTiles;
import net.minecraft.world.World;

import java.util.List;

public class TileEnergyInputHatch extends MultiBlockPart implements IMultiblockAbilityPart<Energy> {

    public TileEnergyInputHatch() {
        super(ModTiles.energy_input_hatch, Tier.LV);
    }

    @Override
    protected int postTick(World world) {
        if (isAttachedToMultiBlock() && ticks % 40 == 0) {
            getController().sync(4);
            this.energy = new EnergyProxy(getController().getEnergy());
        }
        return super.postTick(world);
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
        this.energy = new EnergyProxy(controllerBase.getEnergy());
    }

    @Override
    public void removeFromMultiBlock(MultiblockControllerBase controllerBase) {
        super.removeFromMultiBlock(controllerBase);
        this.energy = Energy.EMPTY;
    }

    @Override
    public MultiblockAbility<Energy> getAbility() {
        return MultiblockAbility.INPUT_ENERGY;
    }

    @Override
    public void registerAbilities(List<Energy> abilityList) {
        abilityList.add(this.energy);
    }
}
