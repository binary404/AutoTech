package binary404.autotech.data.util;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface IBlockProvider extends IItemProvider {

    @Nonnull
    Block getBlock();

}
