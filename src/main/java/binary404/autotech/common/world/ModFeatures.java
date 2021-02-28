package binary404.autotech.common.world;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.util.RegistryUtil;
import binary404.autotech.common.world.dungeon.structure.DungeonChestFeature;
import binary404.autotech.common.world.dungeon.structure.DungeonStructure;
import binary404.autotech.common.world.ore.LargeOreFeature;
import binary404.autotech.common.world.ore.LargeOreFeatureConfig;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.lwjgl.system.CallbackI;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = "autotech", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModFeatures {

    public static StructureFeature<DungeonStructure.Config, ? extends Structure<DungeonStructure.Config>> DUNGEON_FEATURE;

    public static final Feature<LargeOreFeatureConfig> LARGE_ORE = new LargeOreFeature();
    public static final ConfiguredFeature<?, ?> LARGE_ORE_CONF = LARGE_ORE.withConfiguration(new LargeOreFeatureConfig(6, 5, 3, 5, 3, 0, 80, 56, 30)).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG));

    public static ConfiguredFeature<?, ?> DUNGEON_CHEST;

    public static void registerStructureFeatures() {
        DUNGEON_FEATURE = register("dungeon", ModStructures.DUNGEON.func_236391_a_(new DungeonStructure.Config(() -> DungeonStructure.Pools.START, 6)));
    }

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        IForgeRegistry<Feature<?>> r = event.getRegistry();

        DungeonChestFeature.register(event);
        RegistryUtil.register(r, LARGE_ORE, "large_ores");

        DUNGEON_CHEST = register("dungeon_chest", DungeonChestFeature.INSTANCE.withConfiguration(NoFeatureConfig.field_236559_b_));
    }

    public static void onBiomeLoad(BiomeLoadingEvent event) {
        LargeOreFeature.configs = Lists.newArrayList(new LargeOreFeature.VeinConfig(1, ModBlocks.copper_ore, ModBlocks.tin_ore, Blocks.IRON_ORE, Blocks.COAL_ORE), new LargeOreFeature.VeinConfig(2, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, ModBlocks.lead_ore, ModBlocks.silver_ore, ModBlocks.uranium_ore, ModBlocks.nickel_ore), new LargeOreFeature.VeinConfig(3, Blocks.DIAMOND_ORE, Blocks.LAPIS_ORE, ModBlocks.platinum_ore, ModBlocks.titanium_ore));
        event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, LARGE_ORE_CONF);

        if (event.getName().equals(AutoTech.key("dungeon"))) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, ModFeatures.DUNGEON_CHEST);
        }
    }

    private static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> register(String name, ConfiguredFeature<FC, F> feature) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, AutoTech.key(name), feature);
    }

    private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, F> register(String name, StructureFeature<FC, F> feature) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, AutoTech.key(name), feature);
    }

}