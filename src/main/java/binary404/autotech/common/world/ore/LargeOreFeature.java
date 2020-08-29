package binary404.autotech.common.world.ore;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;
import org.lwjgl.system.CallbackI;

import java.util.List;
import java.util.Random;

public class LargeOreFeature extends Feature<LargeOreFeatureConfig> {

    public static List<VeinConfig> configs;

    public LargeOreFeature() {
        super(LargeOreFeatureConfig.CODEC);
    }

    public boolean func_241855_a(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, LargeOreFeatureConfig config) {
        Block block;

        VeinConfig configOre = configs.get(random.nextInt(configs.size()));
        block = configOre.blocks.get(random.nextInt(configOre.blocks.size()));

        int tier = configOre.tier;
        int range;
        int yMin;
        int yMax;
        if (tier == 1) {
            range = config.getTierOneSize();
            yMin = config.getTierOneYMin();
            yMax = config.getTierOneYMax();
        } else if (tier == 2) {
            range = config.getTierTwoSize();
            yMin = config.getTierTwoYMin();
            yMax = config.getTierTwoYMax();
        } else {
            range = config.getTierThreeSize();
            yMin = config.getTierThreeYMin();
            yMax = config.getTierThreeYMax();
        }

        int x = pos.getX() + random.nextInt(16);
        int z = pos.getZ() + random.nextInt(16);

        Random gridRandom = new Random(random.nextLong());
        int xMax = x + range, xMin = x - range, zMax = z + range, zMin = z - range;

        int yStart = MathHelper.nextInt(gridRandom, yMin, yMax);

        for (int currentX = xMin; currentX <= xMax; currentX++) {
            for (int currentZ = zMin; currentZ <= zMax; currentZ++) {
                for (int y = yStart - range; y < yStart + range; y++) {
                    if (gridRandom.nextInt(10) > 2)
                        continue;
                    if (world.getBlockState(new BlockPos(currentX, y, currentZ)).getMaterial() == Material.ROCK)
                        world.setBlockState(new BlockPos(currentX, y, currentZ), block.getDefaultState(), 18);
                }
            }
        }
        return true;
    }

    public static class VeinConfig {

        public List<Block> blocks;
        public int tier;

        public VeinConfig(int tier, Block... blocks) {
            this.tier = tier;
            this.blocks = Lists.newArrayList(blocks);
        }

    }

}
