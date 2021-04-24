package binary404.autotech.common.world;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.block.world.BlockRubberWood;
import binary404.autotech.common.core.util.RegistryUtil;
import binary404.autotech.common.world.ore.LargeOreFeature;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.foliageplacer.PineFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.lwjgl.system.CallbackI;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = "autotech", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModFeatures {

    public static final Feature<NoFeatureConfig> LARGE_ORE = new LargeOreFeature();
    public static final ConfiguredFeature<?, ?> LARGE_ORE_CONF = LARGE_ORE.withConfiguration(new NoFeatureConfig()).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG));

    public static final OilLakeFeature OIL_LAKE = new OilLakeFeature(BlockStateFeatureConfig.field_236455_a_);
    private static ConfiguredFeature<?, ?> OIL_LAKE_CONF;

    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> RUBBER_TREE;

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        IForgeRegistry<Feature<?>> r = event.getRegistry();

        RegistryUtil.register(r, LARGE_ORE, "large_ores");
        RegistryUtil.register(r, OIL_LAKE, "oil_lakes");

        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, AutoTech.key("large_ores"), LARGE_ORE_CONF);

        OIL_LAKE_CONF = OilLakeFeature.createOilLakeFeature(0.7F);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, AutoTech.key("oil_lakes"), OIL_LAKE_CONF);

        RUBBER_TREE = Feature.TREE.withConfiguration(new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(ModBlocks.rubber_log.getDefaultState().with(BlockRubberWood.NATURAL, true)), new SimpleBlockStateProvider(ModBlocks.rubber_leaves.getDefaultState()), new PineFoliagePlacer(FeatureSpread.func_242252_a(1), FeatureSpread.func_242252_a(1), FeatureSpread.func_242253_a(3, 1)), new StraightTrunkPlacer(6, 4, 0), new TwoLayerFeature(2, 0, 2)).setIgnoreVines().build());
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, AutoTech.key("rubber_tree"), RUBBER_TREE);
    }

    public static void onBiomeLoad(BiomeLoadingEvent event) {
        List<LargeOreFeature.VeinConfig> configs = new ArrayList<>();

        configs.add(new LargeOreFeature.VeinConfig(16, 40, 80, 4, ModBlocks.copper_ore));
        configs.add(new LargeOreFeature.VeinConfig(16, 30, 100, 5, ModBlocks.tin_ore));
        configs.add(new LargeOreFeature.VeinConfig(20, 10, 50, 3, ModBlocks.iron_ore));
        configs.add(new LargeOreFeature.VeinConfig(20, 50, 130, 8, ModBlocks.coal_ore));
        configs.add(new LargeOreFeature.VeinConfig(12, 10, 30, 3, ModBlocks.gold_ore, ModBlocks.iron_ore));
        configs.add(new LargeOreFeature.VeinConfig(16, 10, 40, 4, ModBlocks.redstone_ore));
        configs.add(new LargeOreFeature.VeinConfig(16, 40, 70, 6, ModBlocks.lead_ore));
        configs.add(new LargeOreFeature.VeinConfig(16, 20, 50, 6, ModBlocks.silver_ore));
        configs.add(new LargeOreFeature.VeinConfig(12, 30, 60, 5, ModBlocks.lead_ore, ModBlocks.silver_ore));
        configs.add(new LargeOreFeature.VeinConfig(10, 20, 30, 3, ModBlocks.uranium_ore));
        configs.add(new LargeOreFeature.VeinConfig(12, 10, 40, 3, ModBlocks.nickel_ore));
        configs.add(new LargeOreFeature.VeinConfig(10, 5, 20, 2, ModBlocks.diamond_ore));

        LargeOreFeature.configs = configs;

        event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, LARGE_ORE_CONF);
        event.getGeneration().withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, OIL_LAKE_CONF);
        event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, RUBBER_TREE.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, 0.02F, 1))));
    }

}