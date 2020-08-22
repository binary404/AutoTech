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

    default boolean blockMatches(ItemStack otherStack) {
        Item item = otherStack.getItem();
        return item instanceof BlockItem && blockMatches(((BlockItem) item).getBlock());
    }

    default boolean blockMatches(Block other) {
        return getBlock() == other;
    }

}
