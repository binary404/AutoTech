package binary404.autotech.common.world.dungeon.structure;

import binary404.autotech.AutoTech;
import binary404.autotech.common.world.JigsawGenerator;
import binary404.autotech.common.world.dungeon.data.Dungeon;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.ProcessorLists;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.function.Supplier;

public class DungeonStructure extends Structure<DungeonStructure.Config> {

    public static final int START_Y = 128;

    public DungeonStructure(Codec<DungeonStructure.Config> config) {
        super(config);
    }

    @Override
    public GenerationStage.Decoration func_236396_f_() {
        return GenerationStage.Decoration.UNDERGROUND_STRUCTURES;
    }

    @Override
    public IStartFactory<Config> getStartFactory() {
        return (p_242778_1_, p_242778_2_, p_242778_3_, p_242778_4_, p_242778_5_, p_242778_6_) -> new Start(this, p_242778_2_, p_242778_3_, p_242778_4_, p_242778_5_, p_242778_6_);
    }

    public static class Start extends MarginedStructureStart<DungeonStructure.Config> {
        private final DungeonStructure structure;

        public Start(DungeonStructure structure, int chunkX, int chunkZ, MutableBoundingBox box, int references, long worldSeed) {
            super(structure, chunkX, chunkZ, box, references, worldSeed);
            this.structure = structure;
        }

        @Override
        public void func_230364_a_(DynamicRegistries registry, ChunkGenerator gen, TemplateManager manager, int chunkX, int chunkZ, Biome biome, Config config) {
            BlockPos pos = new BlockPos(chunkX * 16, START_Y, chunkZ * 16);
            DungeonStructure.Pools.init();
            JigsawGenerator.func_242837_a(registry, config.toVillageConfig(), AbstractVillagePiece::new, gen, manager, pos, this.components, this.rand, false, false);
            this.recalculateStructureSize();
        }
    }

    public static class Config implements IFeatureConfig {
        public static final Codec<DungeonStructure.Config> CODEC = RecordCodecBuilder.create(builder -> {
            return builder.group(JigsawPattern.field_244392_b_.fieldOf("start_pool").forGetter(DungeonStructure.Config::getStartPool),
                    Codec.intRange(0, Integer.MAX_VALUE).fieldOf("size").forGetter(DungeonStructure.Config::getSize))
                    .apply(builder, DungeonStructure.Config::new);
        });

        private final Supplier<JigsawPattern> startPool;
        private final int size;

        public Config(Supplier<JigsawPattern> startPool, int size) {
            this.startPool = startPool;
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public Supplier<JigsawPattern> getStartPool() {
            return startPool;
        }

        public VillageConfig toVillageConfig() {
            return new VillageConfig(this.startPool, this.getSize());
        }
    }

    public static class Pools {
        public static final JigsawPattern START = JigsawPatternRegistry.func_244094_a(new JigsawPattern(AutoTech.key("dungeon/starts"), new ResourceLocation("empty"), ImmutableList.of(
                Pair.of(JigsawPiece.func_242861_b(AutoTech.sId("dungeon/rooms/start1"), ProcessorLists.field_244101_a), 1),
                Pair.of(JigsawPiece.func_242861_b(AutoTech.sId("dungeon/rooms/start2"), ProcessorLists.field_244101_a), 1)
        ),JigsawPattern.PlacementBehaviour.RIGID));

        public static void init() {

        }
    }

}
