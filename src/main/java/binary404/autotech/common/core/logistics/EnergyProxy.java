package binary404.autotech.common.core.logistics;

import binary404.autotech.common.tile.core.TileCore;

public class EnergyProxy extends Energy {

    private Energy insertHandler;

    public EnergyProxy(Energy energy) {
        super(energy);
        this.insertHandler = energy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return insertHandler.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return insertHandler.extractEnergy(maxExtract, simulate);
    }

    @Override
    public void produce(long amount) {
        insertHandler.produce(amount);
    }

    @Override
    public void consume(long amount) {
        insertHandler.consume(amount);
    }
}
