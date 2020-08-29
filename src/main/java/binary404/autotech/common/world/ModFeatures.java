package binary404.autotech.common.world;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.util.RegistryUtil;
import binary404.autotech.common.world.ore.LargeOreFeature;
import binary404.autotech.common.world.ore.LargeOreFeatureConfig;
import binary404.autotech.mixin.AccessorBiomeGenerationSettings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = "autotech", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModFeatures {

    public static final Feature<LargeOreFeatureConfig> LARGE_ORE = new LargeOreFeature();
    public static final RegistryKey<ConfiguredFeature<?, ?>> LARGE_ORE_CONF = RegistryKey.func_240903_a_(Registry.field_243552_au, AutoTech.key("large_ores"));

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        IForgeRegistry<Feature<?>> r = event.getRegistry();

        RegistryUtil.register(r, LARGE_ORE, "large_ores");
    }

    public static void registerFeaturesToBiomes(MinecraftServer server) {
        LargeOreFeature.configs = Lists.newArrayList(new LargeOreFeature.VeinConfig(1, ModBlocks.copper_ore, ModBlocks.tin_ore, Blocks.IRON_ORE, Blocks.COAL_ORE), new LargeOreFeature.VeinConfig(2, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, ModBlocks.lead_ore, ModBlocks.silver_ore, ModBlocks.uranium_ore, ModBlocks.nickel_ore), new LargeOreFeature.VeinConfig(3, Blocks.DIAMOND_ORE, Blocks.LAPIS_ORE, ModBlocks.platinum_ore, ModBlocks.titanium_ore));
        AutoTech.LOGGER.debug("Injecting ores into generation");
        Registry<ConfiguredFeature<?, ?>> featReg = server.func_244267_aX().func_243612_b(Registry.field_243552_au);
        ConfiguredFeature<?, ?> ores = featReg.getValueForKey(LARGE_ORE_CONF);
        System.out.println(LARGE_ORE_CONF.toString());
        if (ores == null) {
            AutoTech.LOGGER.error("Ore configuration could not be found, skipping generation");
            return;
        }

        for(Biome biome : server.func_244267_aX().func_243612_b(Registry.BIOME_KEY)) {
            injectIntoBiome(biome, ores);
        }
    }

    public static void injectIntoBiome(Biome biome, ConfiguredFeature<?, ?> feature) {
        List<List<Supplier<ConfiguredFeature<?, ?>>>> features = biome.func_242440_e().func_242498_c();
        if (features instanceof ImmutableList) {
            List<List<Supplier<ConfiguredFeature<?, ?>>>> list = new ArrayList<>();
            for (List<Supplier<ConfiguredFeature<?, ?>>> featureList : features) {
                list.add(new ArrayList<>(featureList));
            }
            ((AccessorBiomeGenerationSettings) biome.func_242440_e()).setFeatures(list);
            features = list;
        }

        GenerationStage.Decoration stage = GenerationStage.Decoration.VEGETAL_DECORATION;
        while (features.size() <= stage.ordinal()) {
            features.add(new ArrayList<>());
        }
        features.get(stage.ordinal()).add(() -> feature);
    }

}