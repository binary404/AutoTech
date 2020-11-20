package binary404.autotech.data.tag;

import binary404.autotech.data.util.DataGenJsonConstants;
import binary404.autotech.data.util.IBlockProvider;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeRegistryTagsProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class BaseTagProvider implements IDataProvider {

    private final Map<TagType<?>, Map<ITag.INamedTag<?>, ITag.Builder>> supportedTagTypes = new Object2ObjectOpenHashMap<>();
    private final ExistingFileHelper existingFileHelper;
    private final DataGenerator gen;
    private final String modid;

    protected BaseTagProvider(DataGenerator gen, String modid, @Nullable ExistingFileHelper existingFileHelper) {
        this.gen = gen;
        this.modid = modid;
        this.existingFileHelper = existingFileHelper;
        addTagType(TagType.ITEM);
        addTagType(TagType.BLOCK);
    }

    @Override
    public String getName() {
        return "Tags: " + modid;
    }

    protected <TYPE extends IForgeRegistryEntry<TYPE>> void addTagType(TagType<TYPE> tagType) {
        supportedTagTypes.computeIfAbsent(tagType, type -> new Object2ObjectOpenHashMap<>());
    }

    protected abstract void registerTags();

    @Override
    public void act(DirectoryCache cache) throws IOException {
        supportedTagTypes.values().forEach(Map::clear);
        registerTags();
        supportedTagTypes.forEach(((tagType, tagTypeMap) -> act(cache, tagType, tagTypeMap)));
    }

    private <TYPE extends IForgeRegistryEntry<TYPE>> void act(@Nonnull DirectoryCache cache, TagType<TYPE> tagType, Map<ITag.INamedTag<?>, ITag.Builder> tagTypeMap) {
        if (!tagTypeMap.isEmpty()) {
            //Create a dummy forge registry tags provider and pass all our collected data through to it
            new ForgeRegistryTagsProvider<TYPE>(gen, tagType.getRegistry().get(), modid, existingFileHelper) {
                @Override
                protected void registerTags() {
                    //Add each tag builder to the wrapped provider's builder, but wrap the builder so that we
                    // make sure to first cleanup and remove excess/unused json components
                    // Note: We only override the methods used by the TagsProvider rather than proxying everything back to the original tag builder
                    tagTypeMap.forEach((tag, tagBuilder) -> tagToBuilder.put(tag.getName(), new ITag.Builder() {
                        @Nonnull
                        @Override
                        public JsonObject serialize() {
                            return cleanJsonTag(tagBuilder.serialize());
                        }

                        @Nonnull
                        @Override
                        public <T> Stream<ITag.Proxy> getProxyTags(@Nonnull Function<ResourceLocation, ITag<T>> resourceTagFunction,
                                                                   @Nonnull Function<ResourceLocation, T> resourceElementFunction) {
                            return tagBuilder.getProxyTags(resourceTagFunction, resourceElementFunction);
                        }
                    }));
                }

                @Nonnull
                @Override
                public String getName() {
                    return tagType.getName() + " Tags: " + modid;
                }
            }.act(cache);
        }
    }

    private JsonObject cleanJsonTag(JsonObject tagAsJson) {
        if (tagAsJson.has(DataGenJsonConstants.REPLACE)) {
            //Strip out the optional "replace" entry from the tag if it is the default value
            JsonPrimitive replace = tagAsJson.getAsJsonPrimitive(DataGenJsonConstants.REPLACE);
            if (replace.isBoolean() && !replace.getAsBoolean()) {
                tagAsJson.remove(DataGenJsonConstants.REPLACE);
            }
        }
        return tagAsJson;
    }

    //Protected to allow for extensions to add retrieve their own supported types if they have any
    protected <TYPE extends IForgeRegistryEntry<TYPE>> ForgeRegistryTagBuilder<TYPE> getBuilder(TagType<TYPE> tagType, ITag.INamedTag<TYPE> tag) {
        return new ForgeRegistryTagBuilder<>(supportedTagTypes.get(tagType).computeIfAbsent(tag, ignored -> ITag.Builder.create()), modid);
    }

    protected ForgeRegistryTagBuilder<Item> getItemBuilder(ITag.INamedTag<Item> tag) {
        return getBuilder(TagType.ITEM, tag);
    }

    protected ForgeRegistryTagBuilder<Block> getBlockBuilder(ITag.INamedTag<Block> tag) {
        return getBuilder(TagType.BLOCK, tag);
    }

    protected void addToTag(ITag.INamedTag<Item> tag, IItemProvider... itemProviders) {
        ForgeRegistryTagBuilder<Item> tagBuilder = getItemBuilder(tag);
        for (IItemProvider itemProvider : itemProviders) {
            tagBuilder.add(itemProvider.asItem());
        }
    }

    protected void addToTag(ITag.INamedTag<Block> tag, IBlockProvider... blockProviders) {
        ForgeRegistryTagBuilder<Block> tagBuilder = getBlockBuilder(tag);
        for (IBlockProvider blockProvider : blockProviders) {
            tagBuilder.add(blockProvider.getBlock());
        }
    }

    protected void addToTags(ITag.INamedTag<Item> itemTag, ITag.INamedTag<Block> blockTag, IBlockProvider... blockProviders) {
        ForgeRegistryTagBuilder<Item> itemTagBuilder = getItemBuilder(itemTag);
        ForgeRegistryTagBuilder<Block> blockTagBuilder = getBlockBuilder(blockTag);
        for (IBlockProvider blockProvider : blockProviders) {
            itemTagBuilder.add(blockProvider.getItem());
            blockTagBuilder.add(blockProvider.getBlock());
        }
    }
}
