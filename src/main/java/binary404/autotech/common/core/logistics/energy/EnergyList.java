package binary404.autotech.common.core.logistics.energy;

import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;

public class EnergyList implements IEnergyStorage {

    private List<IEnergyStorage> energyList;
    private long maxExtract;
    private long maxReceive;

    public EnergyList(List<IEnergyStorage> energyList, long maxExtract, long maxReceive) {
        this.energyList = energyList;
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
    }

    public EnergyList(List<IEnergyStorage> energyList) {
        this(energyList, energyList.stream().map(el -> (IEnergyStorage) el).mapToInt(IEnergyStorage::getMaxEnergyStored).sum() / 10, energyList.stream().map(el -> (IEnergyStorage) el).mapToInt(IEnergyStorage::getMaxEnergyStored).sum() / 10);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int amount = 0;
        for (IEnergyStorage storage : energyList) {
            amount += storage.receiveEnergy(maxReceive, simulate);
            if (amount == maxReceive)
                break;
        }
        return amount;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int amount = 0;
        for (IEnergyStorage storage : energyList) {
            amount += storage.extractEnergy(maxExtract, simulate);
            if (amount >= maxExtract)
                break;
        }
        return amount;
    }

    @Override
    public int getEnergyStored() {
        return energyList.stream().mapToInt(IEnergyStorage::getEnergyStored).sum();
    }

    @Override
    public int getMaxEnergyStored() {
        return energyList.stream().mapToInt(IEnergyStorage::getMaxEnergyStored).sum();
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    public long getMaxExtract() {
        return maxExtract;
    }

    public long getMaxReceive() {
        return maxReceive;
    }
}
