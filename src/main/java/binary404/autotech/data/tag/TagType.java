package binary404.autotech.data.tag;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.*;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class TagType<TYPE extends IForgeRegistryEntry<TYPE>> {

    public static final TagType<Item> ITEM = new TagType<>("Item", "items", () -> ForgeRegistries.ITEMS, ItemTags::setCollection);
    public static final TagType<Block> BLOCK = new TagType<>("Block", "blocks", () -> ForgeRegistries.BLOCKS, BlockTags::setCollection);
    public static final TagType<EntityType<?>> ENTITY_TYPE = new TagType<>("Entity Type", "entity_types", () -> ForgeRegistries.ENTITIES, EntityTypeTags::setCollection);
    public static final TagType<Fluid> FLUID = new TagType<>("Fluid", "fluids", () -> ForgeRegistries.FLUIDS, FluidTags::setCollection);
    private final Consumer<TagCollection<TYPE>> collectionSetter;
    private final NonNullSupplier<IForgeRegistry<TYPE>> registry;
    private final String name;
    private final String path;

    private TagType(String name, String path, NonNullSupplier<IForgeRegistry<TYPE>> registry, Consumer<TagCollection<TYPE>> collectionSetter) {
        this.name = name;
        this.path = path;
        this.collectionSetter = collectionSetter;
        this.registry = registry;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setCollection(@Nonnull TagCollection<TYPE> tagCollection) {
        collectionSetter.accept(tagCollection);
    }

    public IForgeRegistry<TYPE> getRegistry() {
        return registry.get();
    }

}
