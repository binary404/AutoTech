package binary404.autotech.common.core.util;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RegistryUtil {

    public static <V extends IForgeRegistryEntry<V>> V register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name) {
        thing.setRegistryName(name);
        reg.register((V) thing);
        return (V) thing;
    }

    public static <V extends IForgeRegistryEntry<V>> V register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name) {
        return register(reg, thing, new ResourceLocation("autotech", name));
    }

}
