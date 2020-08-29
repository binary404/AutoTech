package binary404.autotech.common.world.ore;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class LargeOreFeatureConfig implements IFeatureConfig {

    public static final Codec<LargeOreFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("tier_one_size").forGetter(LargeOreFeatureConfig::getTierOneSize),
            Codec.INT.fieldOf("tier_two_size").forGetter(LargeOreFeatureConfig::getTierTwoSize),
            Codec.INT.fieldOf("tier_three_size").forGetter(LargeOreFeatureConfig::getTierThreeSize),
            Codec.INT.fieldOf("tier_one_y_min").forGetter(LargeOreFeatureConfig::getTierOneYMin),
            Codec.INT.fieldOf("tier_two_y_min").forGetter(LargeOreFeatureConfig::getTierTwoYMin),
            Codec.INT.fieldOf("tier_three_y_min").forGetter(LargeOreFeatureConfig::getTierThreeYMin),
            Codec.INT.fieldOf("tier_one_y_max").forGetter(LargeOreFeatureConfig::getTierOneYMax),
            Codec.INT.fieldOf("tier_two_y_max").forGetter(LargeOreFeatureConfig::getTierTwoYMax),
            Codec.INT.fieldOf("tier_three_y_max").forGetter(LargeOreFeatureConfig::getTierThreeYMax)
    ).apply(instance, LargeOreFeatureConfig::new));

    private final int tierOneSize, tierTwoSize, tierThreeSize;
    private final int tierOneYMin, tierTwoYMin, tierThreeYMin;
    private final int tierOneYMax, tierTwoYMax, tierThreeYMax;

    public LargeOreFeatureConfig(int tierOneSize, int tierTwoSize, int tierThreeSize, int tierOneYMin, int tierTwoYMin, int tierThreeYMin, int tierOneYMax, int tierTwoYMax, int tierThreeYMax) {
        this.tierOneSize = tierOneSize;
        this.tierTwoSize = tierTwoSize;
        this.tierThreeSize = tierThreeSize;
        this.tierOneYMin = tierOneYMin;
        this.tierTwoYMin = tierTwoYMin;
        this.tierThreeYMin = tierThreeYMin;
        this.tierOneYMax = tierOneYMax;
        this.tierTwoYMax = tierTwoYMax;
        this.tierThreeYMax = tierThreeYMax;
    }

    public int getTierOneSize() {
        return this.tierOneSize;
    }

    public int getTierTwoSize() {
        return this.tierTwoSize;
    }

    public int getTierThreeSize() {
        return this.tierThreeSize;
    }

    public int getTierOneYMin() {
        return this.tierOneYMin;
    }

    public int getTierTwoYMin() {
        return tierTwoYMin;
    }

    public int getTierThreeYMin() {
        return tierThreeYMin;
    }

    public int getTierOneYMax() {
        return tierOneYMax;
    }

    public int getTierTwoYMax() {
        return tierTwoYMax;
    }

    public int getTierThreeYMax() {
        return tierThreeYMax;
    }
}
