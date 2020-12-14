package binary404.autotech.common.core.logistics;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyItemWrapper implements ICapabilityProvider {

    ItemStack stack;
    IEnergyContainerItem container;

    boolean canExtract;
    boolean canReceive;

    IEnergyStorage energyCap;

    public EnergyItemWrapper(ItemStack stackIn, IEnergyContainerItem containerIn, boolean extract, boolean receive) {
        this.stack = stackIn;
        this.container = containerIn;
        this.canExtract = extract;
        this.canReceive = receive;

        this.energyCap = new IEnergyStorage() {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                return container.receiveEnergy(stack, maxReceive, simulate);
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                return container.extractEnergy(stack, maxExtract, simulate);
            }

            @Override
            public int getEnergyStored() {
                return container.getEnergyStored(stack);
            }

            @Override
            public int getMaxEnergyStored() {
                return container.getMaxEnergyStored(stack);
            }

            @Override
            public boolean canExtract() {
                return canExtract;
            }

            @Override
            public boolean canReceive() {
                return canReceive;
            }
        };
    }

    public EnergyItemWrapper(ItemStack stackIn, IEnergyContainerItem containerIn) {
        this(stackIn, containerIn, true, true);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return LazyOptional.of(() -> energyCap).cast();
    }
}
