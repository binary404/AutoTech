package binary404.autotech.mixin;

import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.Supplier;

@Mixin(BiomeGenerationSettings.class)
public interface AccessorBiomeGenerationSettings {
    @Accessor("field_242484_f")
    void setFeatures(List<List<Supplier<ConfiguredFeature<?, ?>>>> list);
}
