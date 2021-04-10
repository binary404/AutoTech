package binary404.autotech.common.world.ore;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;
import org.lwjgl.system.CallbackI;

import java.util.List;
import java.util.Random;

public class LargeOreFeature extends Feature<NoFeatureConfig> {

    public static List<VeinConfig> configs;

    public LargeOreFeature() {
        super(NoFeatureConfig.field_236558_a_);
    }

    public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
        List<Block> blocks;

        VeinConfig configOre = configs.get(random.nextInt(configs.size()));
        blocks = configOre.blocks;

        int range = configOre.radius;
        int yMin = configOre.yMin;
        int yMax = configOre.yMax;

        Random veinRandom = new Random(random.nextLong());

        ChunkPos chunkPos = new ChunkPos(pos);

        int xGrid = chunkPos.x;
        int zGrid = chunkPos.z;

        if (xGrid % 3 != 0 || zGrid % 3 != 0)
            return true;

        Random gridRandom = new Random(xGrid * veinRandom.nextInt() + zGrid * veinRandom.nextInt() ^ veinRandom.nextLong());

        int x = pos.getX() + gridRandom.nextInt(16);
        int z = pos.getZ() + gridRandom.nextInt(16);

        int xMax = x + range, xMin = x - range, zMax = z + range, zMin = z - range;

        int yStart = MathHelper.nextInt(gridRandom, yMin, yMax);

        for (int currentX = xMin; currentX <= xMax; currentX++) {
            for (int currentZ = zMin; currentZ <= zMax; currentZ++) {
                for (int y = yStart - range; y < yStart + range; y++) {
                    if (random.nextInt(10) > configOre.density)
                        continue;
                    if (world.getBlockState(new BlockPos(currentX, y, currentZ)).getMaterial() == Material.ROCK)
                        world.setBlockState(new BlockPos(currentX, y, currentZ), blocks.get(veinRandom.nextInt(blocks.size())).getDefaultState(), 18);
                }
            }
        }
        return true;
    }

    private static boolean inChunk(int pos, int chunkPos) {
        return pos >> 4 == chunkPos;
    }

    private static long getGridCoordinate(long coordinate, int offset, int distance) {
        return (coordinate - offset) / distance * distance + offset;
    }

    public static class VeinConfig {

        public List<Block> blocks;
        public int radius;
        public int yMin;
        public int yMax;
        public int density;

        public VeinConfig(int radius, int yMin, int yMax, int density, Block... blocks) {
            this.radius = radius;
            this.yMin = yMin;
            this.yMax = yMax;
            this.density = density;
            this.blocks = Lists.newArrayList(blocks);
        }

    }

}
