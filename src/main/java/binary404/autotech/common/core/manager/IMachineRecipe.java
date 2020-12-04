package binary404.autotech.common.core.manager;

import binary404.autotech.common.core.logistics.Tier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;

public interface IMachineRecipe {

    ItemStack getInput();

    ItemStack getOutput();

    Tier getMinTier();

    int getEnergy();

    ITag.INamedTag<Item> getInputTag();

    int getInputCount();

}
