package binary404.autotech.data.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public interface IBaseProvider {

    ResourceLocation getRegistryName();

    default String getName() {
        return getRegistryName().getPath();
    }

}
