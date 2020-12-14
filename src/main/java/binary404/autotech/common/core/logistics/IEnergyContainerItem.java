package binary404.autotech.common.core.logistics;

import net.minecraft.item.ItemStack;

public interface IEnergyContainerItem {

    int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate);

    int extractEnergy(ItemStack stack, int maxExtract, boolean simulate);

    int getEnergyStored(ItemStack stack);

    int getMaxEnergyStored(ItemStack stack);

}
