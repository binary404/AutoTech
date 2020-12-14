package binary404.autotech.common.tile.machine;

import binary404.autotech.common.block.machine.BlockCharger;
import binary404.autotech.common.core.logistics.IEnergyContainerItem;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.logistics.energy.Energy;
import binary404.autotech.common.core.util.Util;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileMachine;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileCharger extends TileMachine<BlockCharger> {

    private IEnergyContainerItem containerItem = null;

    private IEnergyStorage handler = null;

    public TileCharger() {
        this(Tier.MV);
    }

    public TileCharger(Tier tier) {
        super(ModTiles.charger, tier);
        this.inv.set(1);
    }

    private boolean canStartContainerItem() {
        if (Util.isEnergyContainerItem(this.inv.getStackInSlot(0))) {
            this.containerItem = (IEnergyContainerItem) inv.getStackInSlot(0).getItem();
            return true;
        }
        containerItem = null;
        return false;
    }

    private boolean canStartEnergyHandler() {
        if (Util.isEnergyHandler(inv.getStackInSlot(0))) {
            this.handler = inv.getStackInSlot(0).getCapability(CapabilityEnergy.ENERGY).orElse(Energy.EMPTY);
            return true;
        }
        handler = null;
        return false;
    }

    private int processTickContainerItem() {
        int energy = containerItem.receiveEnergy(inv.getStackInSlot(0), calcEnergyItem(), false);
        this.energy.extractEnergy(energy, false);
        return energy;
    }

    private int processTickEnergyHandler() {
        int energy = handler.receiveEnergy(calcEnergyItem(), false);
        this.energy.extractEnergy(energy, false);
        return energy;
    }

    private int calcEnergyItem() {
        return Math.min(this.energy.getEnergyStored(), this.getEnergyPerUse());
    }

    @Override
    protected int postTick(World world) {
        if (canStartContainerItem()) {
            processTickContainerItem();
        } else if (canStartEnergyHandler()) {
            processTickEnergyHandler();
        }
        return -1;
    }
}
