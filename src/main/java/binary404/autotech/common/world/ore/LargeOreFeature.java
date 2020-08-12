package binary404.autotech.common.world.ore;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;

import java.util.List;
import java.util.Random;

public class LargeOreFeature extends Feature<NoFeatureConfig> {

    public static List<VeinConfig> configs;

    public LargeOreFeature(Codec<NoFeatureConfig> p_i231953_1_) {
        super(p_i231953_1_);
        this.setRegistryName("autotech:large_ore");
    }

    @Override
    public boolean func_230362_a_(ISeedReader p_230362_1_, StructureManager p_230362_2_, ChunkGenerator p_230362_3_, Random random, BlockPos p_230362_5_, NoFeatureConfig p_230362_6_) {
        Block block;

        VeinConfig config = configs.get(random.nextInt(configs.size()));
        block = config.blocks.get(random.nextInt(config.blocks.size()));

        Random gridRandom = new Random(random.nextLong());
        int xMax = p_230362_5_.getX() + config.range, xMin = p_230362_5_.getX() - config.range, zMax = p_230362_5_.getZ() + config.range, zMin = p_230362_5_.getZ() - config.range;

        int yStart = MathHelper.nextInt(gridRandom, config.yMin, config.yMax);

        for (int x = xMin; x <= xMax; x++) {
            for (int z = zMin; z <= zMax; z++) {
                for (int y = yStart - config.range; y < yStart + config.range; y++) {
                    if (gridRandom.nextInt(10) > 2)
                        continue;
                    if (p_230362_1_.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.STONE)
                        p_230362_1_.setBlockState(new BlockPos(x, y, z), block.getDefaultState(), 18);
                }
            }
        }
        return true;
    }

    public static class VeinConfig {

        public List<Block> blocks;
        public int range, yMin, yMax;

        public VeinConfig(int range, int yMin, int yMax, Block... blocks) {
            this.range = range;
            this.blocks = Lists.newArrayList(blocks);
            this.yMin = yMin;
            this.yMax = yMax;
        }

    }

}
