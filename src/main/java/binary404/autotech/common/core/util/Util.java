package binary404.autotech.common.core.util;

import binary404.autotech.common.core.logistics.IEnergyContainerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;

public class Util {

    public static int safeInt(long value) {
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }

    public static int safeInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int fluidHashCode(FluidStack stack) {
        return stack.getFluid().getRegistryName().toString().hashCode();
    }

    public static ItemStack setDefaultEnergyTag(ItemStack container, int energy) {

        if (!container.hasTag()) {
            container.setTag(new CompoundNBT());
        }
        container.getTag().putInt("Energy", energy);

        return container;
    }

    public static boolean isEnergyContainerItem(ItemStack container) {
        return !container.isEmpty() && container.getItem() instanceof IEnergyContainerItem;
    }

    public static boolean isEnergyHandler(ItemStack stack) {
        return !stack.isEmpty() && stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
    }

}
