package binary404.autotech.common.core.recipe.map;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public class FluidKey {

    public final String fluid;
    public final CompoundNBT tag;

    public FluidKey(FluidStack fluidStack) {
        this.fluid = fluidStack.getFluid().getRegistryName().toString();
        this.tag = fluidStack.getTag();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof FluidKey))
            return false;
        FluidKey fluidKey = (FluidKey) obj;
        return Objects.equals(fluid, fluidKey.fluid) && Objects.equals(tag, fluidKey.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fluid, tag);
    }

    @Override
    public String toString() {
        return "FluidKey{" + "fluid=" + fluid + ", tag=" + tag + '}';
    }
}
