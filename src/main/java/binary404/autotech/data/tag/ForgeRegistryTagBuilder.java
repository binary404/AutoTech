package binary404.autotech.data.tag;

import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Arrays;
import java.util.Collection;

public class ForgeRegistryTagBuilder<TYPE extends IForgeRegistryEntry<TYPE>> {

    private final ITag.Builder builder;
    private final String modID;

    public ForgeRegistryTagBuilder(ITag.Builder builder, String modID) {
        this.builder = builder;
        this.modID = modID;
    }

    public ForgeRegistryTagBuilder<TYPE> add(TYPE element) {
        this.builder.addItemEntry(element.getRegistryName(), modID);
        return this;
    }

    @SafeVarargs
    public final ForgeRegistryTagBuilder<TYPE> add(TYPE... elements) {
        for (TYPE element : elements) {
            add(element);
        }
        return this;
    }

    public ForgeRegistryTagBuilder<TYPE> add(ITag.INamedTag<TYPE> tag) {
        this.builder.addTagEntry(tag.getName(), modID);
        return this;
    }

    @SafeVarargs
    public final ForgeRegistryTagBuilder<TYPE> add(ITag.INamedTag<TYPE>... tags) {
        for (ITag.INamedTag<TYPE> tag : tags) {
            add(tag);
        }
        return this;
    }

    public ForgeRegistryTagBuilder<TYPE> add(ITag.ITagEntry tag) {
        builder.addTag(tag, modID);
        return this;
    }

    public ForgeRegistryTagBuilder<TYPE> replace() {
        return replace(true);
    }

    public ForgeRegistryTagBuilder<TYPE> replace(boolean value) {
        builder.replace(value);
        return this;
    }

    public ForgeRegistryTagBuilder<TYPE> addOptional(final ResourceLocation... locations) {
        return addOptional(Arrays.asList(locations));
    }

    public ForgeRegistryTagBuilder<TYPE> addOptional(final Collection<ResourceLocation> locations) {
        return add(ForgeHooks.makeOptionalTag(true, locations));
    }

    public ForgeRegistryTagBuilder<TYPE> addOptionalTag(final ResourceLocation... locations) {
        return addOptionalTag(Arrays.asList(locations));
    }

    public ForgeRegistryTagBuilder<TYPE> addOptionalTag(final Collection<ResourceLocation> locations) {
        return add(ForgeHooks.makeOptionalTag(false, locations));
    }

}
