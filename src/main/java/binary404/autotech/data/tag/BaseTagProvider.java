package binary404.autotech.data.tag;

import binary404.autotech.data.util.DataGenJsonConstants;
import binary404.autotech.data.util.IBlockProvider;
import binary404.autotech.data.util.IItemProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public abstract class BaseTagProvider implements IDataProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Map<TagType<?>, TagTypeMap<?>> supportedTagTypes = new Object2ObjectLinkedOpenHashMap<>();
    private final DataGenerator gen;
    private final String modid;

    protected BaseTagProvider(DataGenerator gen, String modid) {
        this.gen = gen;
        this.modid = modid;
        addTagType(TagType.ITEM);
        addTagType(TagType.BLOCK);
        addTagType(TagType.ENTITY_TYPE);
        addTagType(TagType.FLUID);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Tags: " + modid;
    }

    //Protected to allow for extensions to add their own supported types if they have one
    protected <TYPE extends IForgeRegistryEntry<TYPE>> void addTagType(TagType<TYPE> tagType) {
        supportedTagTypes.putIfAbsent(tagType, new TagTypeMap<>(tagType));
    }

    protected abstract void registerTags();

    @Override
    public void act(@Nonnull DirectoryCache cache) {
        supportedTagTypes.values().forEach(TagTypeMap::clear);
        registerTags();
        supportedTagTypes.values().forEach(tagTypeMap -> act(cache, tagTypeMap));
    }

    @SuppressWarnings("UnstableApiUsage")
    private <TYPE extends IForgeRegistryEntry<TYPE>> void act(@Nonnull DirectoryCache cache, TagTypeMap<TYPE> tagTypeMap) {
        if (!tagTypeMap.isEmpty()) {
            String tagTypePath = tagTypeMap.getTagType().getPath();
            tagTypeMap.getBuilders().forEach((id, tagBuilder) -> {
                Path path = gen.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/" + tagTypePath + "/" + id.getPath() + ".json");
                try {
                    String json = GSON.toJson(cleanJsonTag(tagBuilder.serialize()));
                    String hash = HASH_FUNCTION.hashUnencodedChars(json).toString();
                    if (!Objects.equals(cache.getPreviousHash(path), hash) || !Files.exists(path)) {
                        Files.createDirectories(path.getParent());
                        try (BufferedWriter bufferedwriter = Files.newBufferedWriter(path)) {
                            bufferedwriter.write(json);
                        }
                    }
                    cache.recordHash(path, hash);
                } catch (IOException exception) {
                    LOGGER.error("Couldn't save tags to {}", path, exception);
                }
            });
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
    protected <TYPE extends IForgeRegistryEntry<TYPE>> TagTypeMap<TYPE> getTagTypeMap(TagType<TYPE> tagType) {
        return (TagTypeMap<TYPE>) supportedTagTypes.get(tagType);
    }

    protected <TYPE extends IForgeRegistryEntry<TYPE>> ForgeRegistryTagBuilder<TYPE> getBuilder(TagType<TYPE> tagType, ITag.INamedTag<TYPE> tag) {
        return getTagTypeMap(tagType).getBuilder(tag, modid);
    }

    protected ForgeRegistryTagBuilder<Item> getItemBuilder(ITag.INamedTag<Item> tag) {
        return getBuilder(TagType.ITEM, tag);
    }

    protected ForgeRegistryTagBuilder<Block> getBlockBuilder(ITag.INamedTag<Block> tag) {
        return getBuilder(TagType.BLOCK, tag);
    }

    protected void addToTag(ITag.INamedTag<Item> tag, Item... itemProviders) {
        ForgeRegistryTagBuilder<Item> tagBuilder = getItemBuilder(tag);
        for (Item itemProvider : itemProviders) {
            tagBuilder.add(itemProvider);
        }
    }

    protected void addToTag(ITag.INamedTag<Block> tag, Block... blockProviders) {
        ForgeRegistryTagBuilder<Block> tagBuilder = getBlockBuilder(tag);
        for (Block blockProvider : blockProviders) {
            tagBuilder.add(blockProvider);
        }
    }

    protected void addToTags(ITag.INamedTag<Item> itemTag, ITag.INamedTag<Block> blockTag, Block... blockProviders) {
        ForgeRegistryTagBuilder<Item> itemTagBuilder = getItemBuilder(itemTag);
        ForgeRegistryTagBuilder<Block> blockTagBuilder = getBlockBuilder(blockTag);
        for (Block blockProvider : blockProviders) {
            itemTagBuilder.add(Item.getItemFromBlock(blockProvider));
            blockTagBuilder.add(blockProvider);
        }
    }
}
