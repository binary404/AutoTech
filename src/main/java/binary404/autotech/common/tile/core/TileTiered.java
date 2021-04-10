package binary404.autotech.common.tile.core;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.Tier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;

public class TileTiered extends TileEnergy {

    public Tier tier;
    int minimumTier = 0;
    protected int lastEnergy = 0;
    protected int currentEnergy = 0;
    protected int transfer = 0;

    public TileTiered(TileEntityType<?> type, Tier tier) {
        super(type);
        this.tier = tier;
    }

    @Override
    protected int postTick(World world) {
        this.currentEnergy = this.energy.getEnergyStored();
        transfer = currentEnergy - lastEnergy;
        lastEnergy = this.energy.getEnergyStored();
        return super.postTick(world);
    }

    @Override
    public void readSync(CompoundNBT nbt) {
        super.readSync(nbt);
        this.tier = Tier.values()[nbt.getInt("tier")];
        if (this.tier.ordinal() < this.minimumTier) {
            this.tier = Tier.values()[this.minimumTier];
        }
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        nbt.putInt("tier", tier.ordinal());
        return super.writeSync(nbt);
    }

    @Override
    protected long getEnergyCapacity() {
        return tier.maxPower;
    }

    public int getEnergyPerUse() {
        return tier.use;
    }

    @Override
    public long getGeneration() {
        return this.tier.gen;
    }

    @Override
    protected long getEnergyTransfer() {
        return this.tier.use;
    }
}
