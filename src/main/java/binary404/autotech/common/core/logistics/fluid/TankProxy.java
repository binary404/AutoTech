package binary404.autotech.common.core.logistics.fluid;

import binary404.autotech.common.core.logistics.fluid.Tank;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class TankProxy extends Tank {

    private Tank insertHandler;

    public TankProxy(Tank insertHandler) {
        super(insertHandler.getCapacity());
        this.insertHandler = insertHandler;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return insertHandler.fill(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return insertHandler.drain(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return insertHandler.drain(maxDrain, action);
    }
}
