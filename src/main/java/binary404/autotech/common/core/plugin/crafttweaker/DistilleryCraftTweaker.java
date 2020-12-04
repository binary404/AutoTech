package binary404.autotech.common.core.plugin.crafttweaker;

import binary404.autotech.common.core.manager.DistilleryManager;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import net.minecraftforge.fluids.FluidStack;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.autotech.distillery")
public class DistilleryCraftTweaker {

    @ZenCodeType.Method
    public static void addRecipe(IFluidStack input, IFluidStack output, int energy) {
        CraftTweakerAPI.apply(new Add(input.getInternal(), output.getInternal(), energy));
    }

    private static class Add implements IRuntimeAction {
        private FluidStack input, output;
        private int energy;

        public Add(FluidStack input, FluidStack output, int energy) {
            this.input = input;
            this.output = output;
            this.energy = energy;
        }

        @Override
        public void apply() {
            DistilleryManager.addRecipe(energy, input, output);
        }

        @Override
        public String describe() {
            return "Adding distillery recipe for " + output;
        }
    }

}
