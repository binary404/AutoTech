package binary404.autotech.common.world;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.world.ore.LargeOreFeature;
import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = "autotech", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModFeatures {

    public static final Feature<NoFeatureConfig> largeOreFeature = new LargeOreFeature(NoFeatureConfig.field_236558_a_);

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().register(largeOreFeature);
    }

    public static void registerFeaturesToBiome() {
        for(Biome biome : ForgeRegistries.BIOMES) {
            biome.getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).clear();
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, largeOreFeature.withConfiguration(NoFeatureConfig.field_236559_b_).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 0, 0, 50))));
            DefaultBiomeFeatures.addStoneVariants(biome);
        }

        LargeOreFeature.configs = Lists.newArrayList(new LargeOreFeature.VeinConfig(6, 0, 50, ModBlocks.copper_ore, ModBlocks.tin_ore, Blocks.IRON_ORE), new LargeOreFeature.VeinConfig(5, 0, 32, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, ModBlocks.lead_ore, ModBlocks.silver_ore, ModBlocks.uranium_ore, ModBlocks.nickel_ore), new LargeOreFeature.VeinConfig(4, 0, 16, Blocks.DIAMOND_ORE, Blocks.LAPIS_ORE, ModBlocks.platinum_ore, ModBlocks.titanium_ore));
    }

}
