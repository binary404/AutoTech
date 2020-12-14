package binary404.autotech.common.core.logistics.fluid;

import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class InputOutputFluidWrapper extends Tank {

    Tank tank;
    Tank tank2;

    public InputOutputFluidWrapper(Tank tank, Tank tank2) {
        super(tank.getCapacity());
        this.tank = tank;
        this.tank2 = tank2;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return tank.fill(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (!tank2.isEmpty())
            return tank2.drain(resource, action);
        else
            return tank.drain(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (!tank2.isEmpty())
            return tank2.drain(maxDrain, action);
        else
            return tank.drain(maxDrain, action);
    }

}
