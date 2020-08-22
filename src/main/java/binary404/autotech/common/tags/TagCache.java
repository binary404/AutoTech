package binary404.autotech.common.tags;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.stream.Collectors;

public class TagCache {

    private static final Map<String, List<ItemStack>> blockTagStacks = new Object2ObjectOpenHashMap<>();
    private static final Map<String, List<ItemStack>> itemTagStacks = new Object2ObjectOpenHashMap<>();

    public static void resetTagCaches() {
        blockTagStacks.clear();
        itemTagStacks.clear();
    }

    public static List<String> getItemTags(ItemStack check) {
        return check == null || check.isEmpty() ? Collections.emptyList() : getTagsAsStrings(check.getItem().getTags());
    }

    public static List<String> getBlockTags(ItemStack check) {
        if (check == null || check.isEmpty()) {
            return Collections.emptyList();
        }
        Item item = check.getItem();
        if (item instanceof BlockItem) {
            return getTagsAsStrings(((BlockItem) item).getBlock().getTags());
        }
        return Collections.emptyList();
    }

    public static List<String> getTagsAsStrings(Set<ResourceLocation> tags) {
        return tags.stream().map(ResourceLocation::toString).collect(Collectors.toList());
    }

    public static List<ItemStack> getItemTagStacks(String oreName) {
        if (itemTagStacks.get(oreName) != null) {
            return itemTagStacks.get(oreName);
        }
        TagCollection<Item> tagCollection = ItemTags.getCollection();
        List<ResourceLocation> keys = tagCollection.getRegisteredTags().stream().filter(rl -> Matcher.matches(oreName, rl.toString())).collect(Collectors.toList());
        Set<Item> items = new HashSet<>();
        for (ResourceLocation key : keys) {
            ITag<Item> itemTag = tagCollection.get(key);
            if (itemTag != null) {
                items.addAll(itemTag.getAllElements());
            }
        }
        List<ItemStack> stacks = items.stream().map(ItemStack::new).collect(Collectors.toList());
        itemTagStacks.put(oreName, stacks);
        return stacks;
    }

    public static List<ItemStack> getBlockTagStacks(String oreName) {
        if (blockTagStacks.get(oreName) != null) {
            return blockTagStacks.get(oreName);
        }
        TagCollection<Block> tagCollection = BlockTags.getCollection();
        List<ResourceLocation> keys = tagCollection.getRegisteredTags().stream().filter(rl -> Matcher.matches(oreName, rl.toString())).collect(Collectors.toList());
        Set<Block> blocks = new HashSet<>();
        for (ResourceLocation key : keys) {
            ITag<Block> blockTag = tagCollection.get(key);
            if (blockTag != null) {
                blocks.addAll(blockTag.getAllElements());
            }
        }
        List<ItemStack> stacks = blocks.stream().map(ItemStack::new).collect(Collectors.toList());
        blockTagStacks.put(oreName, stacks);
        return stacks;
    }

}
