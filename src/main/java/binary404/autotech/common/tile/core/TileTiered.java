package binary404.autotech.common.tile.core;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.Tier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public class TileTiered<B extends BlockTile> extends TileEnergy<B> {

    public Tier tier;
    int minimumTier = 0;

    public TileTiered(TileEntityType<?> type) {
        super(type);
        this.minimumTier = 0;
        this.tier = Tier.values()[minimumTier];
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

    public int getBaseSpeed() {
        return tier.speed;
    }

    @Override
    protected long getEnergyTransfer() {
        return this.tier.use;
    }
}
