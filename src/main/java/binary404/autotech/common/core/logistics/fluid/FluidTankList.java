package binary404.autotech.common.core.logistics.fluid;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class FluidTankList implements IFluidHandler, IMultipleTankHandler, INBTSerializable<CompoundNBT> {

    protected final List<IFluidTank> fluidTanks;
    protected final boolean allowSameFluidFill;

    public FluidTankList(boolean allowSameFluidFill, IFluidTank... fluidTanks) {
        this.fluidTanks = Arrays.asList(fluidTanks);
        this.allowSameFluidFill = allowSameFluidFill;
    }

    public FluidTankList(boolean allowSameFluidFill, List<? extends IFluidTank> fluidTanks) {
        this.fluidTanks = new ArrayList<>(fluidTanks);
        this.allowSameFluidFill = allowSameFluidFill;
    }

    public FluidTankList(boolean allowSameFluidFill, FluidTankList parent, IFluidTank... additionalTanks) {
        this.fluidTanks = new ArrayList<>();
        this.fluidTanks.addAll(parent.fluidTanks);
        this.fluidTanks.addAll(Arrays.asList(additionalTanks));
        this.allowSameFluidFill = allowSameFluidFill;
    }

    public List<IFluidTank> getFluidTanks() {
        return Collections.unmodifiableList(fluidTanks);
    }

    @Override
    public Iterator<IFluidTank> iterator() {
        return getFluidTanks().iterator();
    }

    @Override
    public int getTanks() {
        return fluidTanks.size();
    }

    @Override
    public IFluidTank getTankAt(int index) {
        return fluidTanks.get(index);
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return fluidTanks.get(tank).getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return fluidTanks.get(tank).getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        IFluidTank fluidTank = fluidTanks.get(tank);
        return fluidTank.isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource == null || resource.getAmount() <= 0) {
            return 0;
        }
        return fillTanksImpl(resource.copy(), action);
    }

    //fills exactly one tank if multi-filling is not allowed
    //and as much tanks as possible otherwise
    //note that it will always try to fill tanks with same fluid first
    private int fillTanksImpl(FluidStack resource, FluidAction action) {
        int totalFilled = 0;
        //first, try to fill tanks that already have same fluid type
        for (IFluidTank handler : fluidTanks) {
            if (resource.isFluidEqual(handler.getFluid())) {
                int filledAmount = handler.fill(resource, action);
                totalFilled += filledAmount;
                resource.shrink(filledAmount);
                //if filling multiple tanks is not allowed, or resource is empty, return now
                if (!allowSameFluidFill || resource.getAmount() == 0)
                    return totalFilled;
            }
        }
        //otherwise, try to fill empty tanks
        for (IFluidTank handler : fluidTanks) {
            if (handler.getFluidAmount() == 0) {
                int filledAmount = handler.fill(resource, action);
                totalFilled += filledAmount;
                resource.shrink(filledAmount);
                if (!allowSameFluidFill || resource.getAmount() == 0)
                    return totalFilled;
            }
        }
        return totalFilled;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource == null || resource.getAmount() <= 0) {
            return null;
        }
        resource = resource.copy();
        FluidStack totalDrained = null;
        for (IFluidTank handler : fluidTanks) {
            if (!resource.isFluidEqual(handler.getFluid())) {
                continue;
            }
            FluidStack drain = handler.drain(resource.getAmount(), action);
            if (drain == null) {
                continue;
            }
            if (totalDrained == null) {
                totalDrained = drain;
            } else totalDrained.grow(drain.getAmount());

            resource.shrink(drain.getAmount());
            if (resource.getAmount() == 0) break;
        }
        return totalDrained;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (maxDrain == 0) {
            return null;
        }
        FluidStack totalDrained = null;
        for (IFluidTank handler : fluidTanks) {
            if (totalDrained == null) {
                totalDrained = handler.drain(maxDrain, action);
                if (totalDrained != null)
                    maxDrain -= totalDrained.getAmount();
            } else {
                FluidStack copy = totalDrained.copy();
                copy.setAmount(maxDrain);
                if (!copy.isFluidEqual(handler.getFluid())) continue;
                FluidStack drain = handler.drain(copy.getAmount(), action);
                if (drain != null) {
                    totalDrained.grow(drain.getAmount());
                    maxDrain -= drain.getAmount();
                }
            }
            if (maxDrain <= 0) break;
        }
        return totalDrained;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT fluidInventory = new CompoundNBT();
        fluidInventory.putInt("TankAmount", this.getTanks());

        ListNBT tanks = new ListNBT();
        for (int i = 0; i < this.getTanks(); i++) {
            INBT writeTag;
            IFluidTank fluidTank = fluidTanks.get(i);
            if (fluidTank instanceof FluidTank) {
                writeTag = ((FluidTank) fluidTank).writeToNBT(new CompoundNBT());
            } else if (fluidTank instanceof INBTSerializable) {
                writeTag = ((INBTSerializable) fluidTank).serializeNBT();
            } else writeTag = new CompoundNBT();

            tanks.add(writeTag);
        }
        fluidInventory.put("Tanks", tanks);
        return fluidInventory;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT tanks = nbt.getList("Tanks", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < Math.min(fluidTanks.size(), nbt.getInt("TankAmount")); i++) {
            INBT nbtTag = tanks.get(i);
            IFluidTank fluidTank = fluidTanks.get(i);
            if (fluidTank instanceof FluidTank) {
                ((FluidTank) fluidTank).readFromNBT((CompoundNBT) nbtTag);
            } else if (fluidTank instanceof INBTSerializable) {
                ((INBTSerializable) fluidTank).deserializeNBT(nbtTag);
            }
        }
    }

    protected void validateTankIndex(int tank) {
        if (tank < 0 || tank >= fluidTanks.size())
            throw new RuntimeException("Tank " + tank + " not in valid range - (0," + fluidTanks.size() + "]");
    }

}
