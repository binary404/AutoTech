package binary404.autotech.common.tile.core;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.Tier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;

public class TileMachine<T extends BlockTile> extends TileTiered<T> {

    public int processMax;
    public int processRem;
    protected boolean isActive;

    public TileMachine(TileEntityType<?> type, Tier tier) {
        super(type, tier);
    }

    @Override
    protected int postTick(World world) {
        if (isActive) {
            processTick();

            if (canFinish()) {
                processFinish();
                if (!canStart()) {
                    processOff();
                } else {
                    processStart();
                }
                sync(4);
            } else if (energy.getEnergyStored() <= 0) {
                processOff();
            }
        } else {
            if (canStart()) {
                processStart();
                processTick();
                isActive = true;
                sync(4);
            }
        }

        return super.postTick(world);
    }

    protected boolean canStart() {
        return false;
    }

    protected void processStart() {

    }

    protected boolean canFinish() {
        return processRem <= 0 && hasValidInput();
    }

    protected boolean hasValidInput() {
        return true;
    }

    protected void processFinish() {

    }

    protected void processOff() {
        processRem = 0;
        isActive = false;
    }

    public int getScaledProgress() {
        if (!isActive || processMax <= 0 || processRem <= 0)
            return 0;
        return 24 * (processMax - processRem) / processMax;
    }

    protected int processTick() {
        if (processRem <= 0) {
            return 0;
        }
        this.energy.consume(this.tier.use);
        processRem -= this.tier.use;
        return this.tier.use;
    }

    @Override
    public void readSync(CompoundNBT nbt) {
        this.processMax = nbt.getInt("max");
        this.processRem = nbt.getInt("rem");
        this.isActive = nbt.getBoolean("active");
        super.readSync(nbt);
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        nbt.putInt("max", processMax);
        nbt.putInt("rem", processRem);
        nbt.putBoolean("active", isActive);
        return super.writeSync(nbt);
    }

    @Override
    public long getGeneration() {
        return 0L;
    }
}
