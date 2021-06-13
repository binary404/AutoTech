package binary404.autotech.common.tile.transfer.fluid;

import binary404.autotech.AutoTech;
import net.minecraft.util.ResourceLocation;

public enum FluidPipeType {
    BASIC(1),
    IMPROVED(2),
    ADVANCED(3),
    ELITE(4),
    ULTIMATE(5);

    private final int tier;

    FluidPipeType(int tier) {
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }

    public int getCapacity() {
        switch (this) {
            case BASIC:
                return 1000;
            case IMPROVED:
                return 2000;
            case ADVANCED:
                return 5000;
            case ELITE:
                return 12000;
            case ULTIMATE:
                return 27000;
            default:
                throw new RuntimeException("?");
        }
    }

    public int getTransferRate() {
        switch (this) {
            case BASIC:
                return 100;
            case IMPROVED:
                return 200;
            case ADVANCED:
                return 500;
            case ELITE:
                return 1200;
            case ULTIMATE:
                return 2700;
            default:
                throw new RuntimeException("?");
        }
    }

    public ResourceLocation getId() {
        switch (this) {
            case BASIC:
                return new ResourceLocation(AutoTech.modid, "bronze_pipe");
            case IMPROVED:
                return new ResourceLocation(AutoTech.modid, "improved_fluid_pipe");
            case ADVANCED:
                return new ResourceLocation(AutoTech.modid, "advanced_fluid_pipe");
            case ELITE:
                return new ResourceLocation(AutoTech.modid, "elite_fluid_pipe");
            case ULTIMATE:
                return new ResourceLocation(AutoTech.modid, "ultimate_fluid_pipe");
            default:
                throw new RuntimeException("?");
        }
    }

    public ResourceLocation getNetworkType() {
        switch (this) {
            case BASIC:
                return new ResourceLocation(AutoTech.modid, "basic_fluid_network");
            case IMPROVED:
                return new ResourceLocation(AutoTech.modid, "improved_fluid_network");
            case ADVANCED:
                return new ResourceLocation(AutoTech.modid, "advanced_fluid_network");
            case ELITE:
                return new ResourceLocation(AutoTech.modid, "elite_fluid_network");
            case ULTIMATE:
                return new ResourceLocation(AutoTech.modid, "ultimate_fluid_network");
            default:
                throw new RuntimeException("?");
        }
    }
}
