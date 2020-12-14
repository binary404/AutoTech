package binary404.autotech.common.tile.core;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.energy.Energy;
import binary404.autotech.common.core.util.Util;
import binary404.autotech.common.tile.util.TransferType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TileEnergy<B extends BlockTile> extends TileTickable<B> {

    protected Energy energy = Energy.create(0);

    public TileEnergy(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void readSync(CompoundNBT nbt) {
        if (!keepEnergy()) {
            this.energy.read(nbt, true, false);
        }
        super.readSync(nbt);
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        if (!keepEnergy()) {
            this.energy.write(nbt, true, false);
        }
        return super.writeSync(nbt);
    }

    @Override
    public void readStorable(CompoundNBT nbt) {
        if (keepEnergy()) {
            this.energy.read(nbt, false, false);
        }
        super.readStorable(nbt);
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT nbt) {
        if (keepEnergy()) {
            this.energy.write(nbt, false, false);
        }
        return super.writeStorable(nbt);
    }

    public boolean keepEnergy() {
        return false;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY && isEnergyPresent(side)) {
            return LazyOptional.of(() -> new Energy(getEnergy()) {
                @Override
                public int extractEnergy(int maxExtract, boolean simulate) {
                    return Util.safeInt(TileEnergy.this.extractEnergy(maxExtract, simulate, side));
                }

                @Override
                public int receiveEnergy(int maxReceive, boolean simulate) {
                    return Util.safeInt(TileEnergy.this.receiveEnergy(maxReceive, simulate, side));
                }

                @Override
                public boolean canReceive() {
                    return TileEnergy.this.canReceiveEnergy(side);
                }

                @Override
                public boolean canExtract() {
                    return TileEnergy.this.canExtractEnergy(side);
                }
            }).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void onFirstTick(World world) {
        super.onFirstTick(world);
        this.energy.setCapacity(getEnergyCapacity());
        this.energy.setTransfer(getEnergyTransfer());
        sync();
    }

    protected long extractFromSides(World world) {
        long extracted = 0;
        if (!isRemote()) {
            for (Direction side : Direction.values()) {
                if (canExtractEnergy(side)) {
                    long amount = Math.min(getEnergyTransfer(), getEnergy().getStored());
                    long toExtract = Energy.receive(world.getTileEntity(this.pos.offset(side, getExtractSidesOffsets()[side.ordinal()])), side, Util.safeInt(amount), false);
                    extracted += extractEnergy(Util.safeInt(toExtract), false, side);
                }
            }
        }
        return extracted;
    }

    protected int[] getExtractSidesOffsets() {
        return new int[]{1, 1, 1, 1, 1, 1};
    }

    protected long chargeItems(int i) {
        return chargeItems(0, i);
    }

    protected long chargeItems(int i, int j) {
        long extracted = 0;
        if (!isRemote()) {
            for (ItemStack stack : getChargingInv(i, j)) {
                extracted += chargeItem(stack, getEnergyTransfer());
            }
        }
        return extracted;
    }

    public List<ItemStack> getChargingInv(int i, int j) {
        return IntStream.range(i, j)
                .mapToObj(value -> (ItemStack) this.inv.getStacks().get(value))
                .collect(Collectors.toList());
    }

    protected long chargeItem(ItemStack stack, long transfer) {
        if (!stack.isEmpty()) {
            long amount = Math.min(transfer, getEnergy().getStored());
            int received = Energy.receive(stack, amount, false);
            return extractEnergy(received, false, null);
        }
        return 0;
    }

    protected long extractEnergy(int maxExtract, boolean simulate, @Nullable Direction side) {
        if (!canExtractEnergy(side)) return 0;
        final Energy energy = getEnergy();
        long extracted = Math.min(energy.getStored(), Math.min(energy.getMaxExtract(), maxExtract));
        if (!simulate && extracted > 0) {
            energy.consume(extracted);
            sync(10);
        }
        return extracted;
    }

    protected long receiveEnergy(int maxReceive, boolean simulate, @Nullable Direction side) {
        if (!canReceiveEnergy(side)) return 0;
        final Energy energy = getEnergy();
        long received = Math.min(energy.getEmpty(), Math.min(energy.getMaxReceive(), maxReceive));
        if (!simulate && received > 0) {
            energy.produce(received);
            sync(10);
        }
        return received;
    }

    public boolean canExtractEnergy(@Nullable Direction side) {
        return side == null || isEnergyPresent(side);
    }

    public boolean canReceiveEnergy(@Nullable Direction side) {
        return side == null || isEnergyPresent(side);
    }

    public boolean isEnergyPresent(@Nullable Direction side) {
        return true;
    }

    protected long getEnergyCapacity() {
        return 100000l;
    }

    protected long getEnergyTransfer() {
        return 100l;
    }

    public long getGeneration() {
        return 20l;
    }

    public Energy getEnergy() {
        return this.energy;
    }

    public TransferType getTransferType() {
        return TransferType.ALL;
    }

}
