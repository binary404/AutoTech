package binary404.autotech.common.core.logistics.fluid;

import com.google.common.collect.Lists;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class FluidHandlerProxy implements IFluidHandler {

    public final IFluidHandler input;
    public final IFluidHandler output;
    private IFluidHandler[] properties;

    public FluidHandlerProxy(IFluidHandler input, IFluidHandler output) {
        this.input = input;
        this.output = output;

        List<IFluidHandler> tanks = Lists.newArrayList();
        tanks.add(input);
        tanks.add(output);
        this.properties = tanks.toArray(new IFluidHandler[0]);
    }

    @Override
    public int getTanks() {
        return this.properties.length;
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.properties[tank].getTankCapacity(0);
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.properties[tank].getFluidInTank(0);
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return this.properties[tank].isFluidValid(0, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return input.fill(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return output.drain(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return output.drain(maxDrain, action);
    }
}
