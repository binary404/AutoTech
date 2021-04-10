package binary404.autotech.common.world;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.util.Util;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.LakesFeature;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;

public class OilLakeFeature extends LakesFeature {

    public OilLakeFeature(Codec<BlockStateFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BlockStateFeatureConfig config) {
        if (Util.tryPercentage(0.07))
            return super.generate(reader, generator, rand, pos, config);
        return super.generate(reader, generator, rand, new BlockPos(pos.getX(), rand.nextInt(30) + 10, pos.getZ()), config);
    }

    public static ConfiguredFeature<?, ?> createOilLakeFeature(float multi) {
        return ModFeatures.OIL_LAKE.withConfiguration(new BlockStateFeatureConfig(ModBlocks.crude_oil.getDefaultState())).withPlacement(Placement.WATER_LAKE.configure(new ChanceConfig((int) (multi * 6))));
    }
}
