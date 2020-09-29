package binary404.autotech.common.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Rarity;
import net.minecraft.state.StateContainer;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class BasicFlowingFluid extends ForgeFlowingFluid {

    protected BasicFlowingFluid(Properties properties) {
        super(properties);
    }

    public static FluidAttributes.Builder addAttributes(FluidAttributes.Builder builder) {
        return builder.density(20000).viscosity(600);
    }

    public static class Flowing extends BasicFlowingFluid {

        protected Flowing(Properties properties) {
            super(properties);
            setDefaultState(getStateContainer().getBaseState().with(LEVEL_1_8, 7));
        }

        @Override
        protected void fillStateContainer(StateContainer.Builder<Fluid, FluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        @Override
        public int getLevel(FluidState p_207192_1_) {
            return p_207192_1_.get(LEVEL_1_8);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends BasicFlowingFluid {
        protected Source(Properties properties) {
            super(properties);
        }

        @Override
        public int getLevel(FluidState p_207192_1_) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
