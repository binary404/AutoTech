package binary404.autotech.data.tag;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public final class TagType<TYPE extends IForgeRegistryEntry<TYPE>> {

    public static final TagType<Item> ITEM = new TagType<Item>("Item", () -> ForgeRegistries.ITEMS);
    public static final TagType<Block> BLOCK = new TagType<Block>("Block", () -> ForgeRegistries.BLOCKS);

    private final NonNullSupplier<IForgeRegistry<TYPE>> registry;
    private final String name;

    private TagType(String name, NonNullSupplier<IForgeRegistry<TYPE>> registry) {
        this.name = name;
        this.registry = registry;
    }

    public NonNullSupplier<IForgeRegistry<TYPE>> getRegistry() {
        return registry;
    }

    public String getName() {
        return name;
    }
}
