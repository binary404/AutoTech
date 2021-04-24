package binary404.autotech.common.core.logistics.energy;

import binary404.autotech.common.core.util.PlayerUtil;
import binary404.autotech.common.core.util.Util;
import com.google.common.base.Predicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class Energy implements IEnergyStorage {

    public static final Energy EMPTY = Energy.create(0);
    public static final Long MAX = 9_000_000_000_000_000_000L;
    public static final Long MIN = 0L;

    private long capacity;
    private long stored;
    private long maxExtract;
    private long maxReceive;

    public Energy(Energy energy) {
        this(energy.capacity, energy.maxExtract, energy.maxReceive);
        setStored(energy.stored);
    }

    public Energy(long capacity, long maxExtract, long maxReceive) {
        this.capacity = capacity;
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
    }

    public static Energy create(long capacity) {
        return create(capacity, capacity, capacity);
    }

    public static Energy create(long capacity, long transfer) {
        return create(capacity, transfer, transfer);
    }

    public static Energy from(Energy energy) {
        return create(energy.capacity, energy.maxExtract, energy.maxReceive);
    }

    public static Energy create(long capacity, long maxExtract, long maxReceive) {
        return new Energy(capacity, maxExtract, maxReceive);
    }

    public boolean clone(Energy other) {
        boolean flag = false;
        if (this.capacity != other.capacity) {
            setCapacity(other.getCapacity());
            flag = true;
        }
        if (this.stored != other.stored) {
            setStored(other.getStored());
            flag = true;
        }
        if (this.getTransfer() != other.getTransfer()) {
            setTransfer(other.getTransfer());
            flag = true;
        }
        return flag;
    }

    public Energy read(CompoundNBT nbt, boolean capacity, boolean transfer) {
        return read(nbt, "main_energy", capacity, transfer);
    }

    public Energy read(CompoundNBT nbt, String key, boolean capacity, boolean transfer) {
        if (capacity) {
            this.capacity = nbt.getLong("energy_capacity_" + key);
        }
        this.stored = nbt.getLong("energy_stored_" + key);
        if (transfer) {
            this.maxExtract = nbt.getLong("max_extract_" + key);
            this.maxReceive = nbt.getLong("max_receive_" + key);
        }
        return this;
    }

    public CompoundNBT write(boolean capacity, boolean transfer) {
        return write("main_energy", capacity, transfer);
    }

    public CompoundNBT write(String key, boolean capacity, boolean transfer) {
        return write(new CompoundNBT(), key, capacity, transfer);
    }

    public CompoundNBT write(CompoundNBT nbt, boolean capacity, boolean transfer) {
        return write(nbt, "main_energy", capacity, transfer);
    }

    public CompoundNBT write(CompoundNBT nbt, String key, boolean capacity, boolean transfer) {
        if (capacity) {
            nbt.putLong("energy_capacity_" + key, this.capacity);
        }
        nbt.putLong("energy_stored_" + key, this.stored);
        if (transfer) {
            nbt.putLong("max_extract_" + key, this.maxExtract);
            nbt.putLong("max_receive_" + key, this.maxReceive);
        }
        return nbt;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;
        long energyReceived = Math.min(this.capacity - this.stored, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            this.stored += energyReceived;
        return (int) energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;
        long energyExtracted = Math.min(this.stored, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            this.stored -= energyExtracted;
        return (int) energyExtracted;
    }

    public void produce(long amount) {
        this.stored = Math.min(this.stored + amount, this.capacity);
    }

    public void consume(long amount) {
        this.stored = Math.max(this.stored - amount, 0);
    }

    public long getEmpty() {
        return getCapacity() - getStored();
    }

    public long getCapacity() {
        return this.capacity;
    }

    public Energy setCapacity(long capacity) {
        this.capacity = capacity;
        return this;
    }

    public long getStored() {
        return this.stored;
    }

    public Energy setStored(long stored) {
        this.stored = Math.max(0, Math.min(this.capacity, stored));
        return this;
    }

    public long getMaxExtract() {
        return this.maxExtract;
    }

    public Energy setMaxExtract(long maxExtract) {
        this.maxExtract = maxExtract;
        return this;
    }

    public long getMaxReceive() {
        return this.maxReceive;
    }

    public Energy setMaxReceive(long maxReceive) {
        this.maxReceive = maxReceive;
        return this;
    }

    public Energy setMaxTransfer() {
        this.maxReceive = MAX;
        this.maxExtract = MAX;
        return this;
    }

    public Energy setTransfer(long transfer) {
        this.maxReceive = transfer;
        this.maxExtract = transfer;
        return this;
    }

    public long getTransfer() {
        return Math.max(this.maxExtract, this.maxReceive);
    }

    @Override
    public int getEnergyStored() {
        return Util.safeInt(this.stored);
    }

    @Override
    public int getMaxEnergyStored() {
        return Util.safeInt(this.capacity);
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0 && !isEmpty();
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0 && !isFull();
    }

    public int toComparatorPower() {
        return (int) (((float) this.stored / this.capacity) * 15);
    }

    public float subSized() {
        return (float) this.stored / this.capacity;
    }

    public boolean hasEnergy() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return this.stored <= 0;
    }

    public boolean isFull() {
        return this.stored > 0 && this.stored >= this.capacity;
    }

    public long getPercent() {
        return (long) (((float) this.stored / this.capacity) * 100);
    }

    public static int extract(ItemStack stack, long energy, boolean simulate) {
        return get(stack).orElse(EMPTY).extractEnergy(Util.safeInt(energy), simulate);
    }

    public static int receive(ItemStack stack, long energy, boolean simulate) {
        return get(stack).orElse(EMPTY).receiveEnergy(Util.safeInt(energy), simulate);
    }

    public static int getStored(ItemStack stack) {
        return get(stack).orElse(EMPTY).getEnergyStored();
    }

    public static void ifPresent(ItemStack stack, NonNullConsumer<? super IEnergyStorage> consumer) {
        get(stack).ifPresent(consumer);
    }

    public static boolean isPresent(ItemStack stack) {
        return get(stack).isPresent();
    }

    public static LazyOptional<IEnergyStorage> get(ItemStack stack) {
        return !stack.isEmpty() ? stack.getCapability(CapabilityEnergy.ENERGY, null) : LazyOptional.empty();
    }

    public static int extract(@Nullable TileEntity tile, Direction direction, long energy, boolean simulate) {
        return tile == null ? 0 : get(tile, direction).orElse(EMPTY).extractEnergy(Util.safeInt(energy), simulate);
    }

    public static int receive(@Nullable TileEntity tile, Direction direction, long energy, boolean simulate) {
        return tile == null ? 0 : get(tile, direction).orElse(EMPTY).receiveEnergy(Util.safeInt(energy), simulate);
    }

    public static void ifPresent(@Nullable TileEntity tile, @Nullable Direction direction, NonNullConsumer<? super IEnergyStorage> consumer) {
        get(tile, direction).ifPresent(consumer);
    }

    public static boolean isPresent(@Nullable TileEntity tile, @Nullable Direction direction) {
        return get(tile, direction).isPresent();
    }

    public static LazyOptional<IEnergyStorage> get(@Nullable TileEntity tile, @Nullable Direction direction) {
        return tile == null ? LazyOptional.empty() : tile.getCapability(CapabilityEnergy.ENERGY, direction != null ? direction.getOpposite() : null);
    }

    public static boolean canExtract(@Nullable TileEntity tile, @Nullable Direction direction) {
        return tile != null && get(tile, direction).orElse(EMPTY).canExtract();

    }

    public static boolean canReceive(@Nullable TileEntity tile, @Nullable Direction direction) {
        return tile != null && get(tile, direction).orElse(EMPTY).canReceive();
    }

}
