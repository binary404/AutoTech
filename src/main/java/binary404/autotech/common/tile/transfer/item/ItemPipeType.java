package binary404.autotech.common.tile.transfer.item;

import binary404.autotech.AutoTech;
import binary404.autotech.common.tile.ModTiles;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public enum ItemPipeType {

    BASIC(1),
    IMPROVED(2),
    ADVANCED(3);

    private final int tier;

    ItemPipeType(int tier) {
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }

    public int getMaxTicksInPipe() {
        switch (this) {
            case BASIC:
                return 30;
            case IMPROVED:
                return 20;
            case ADVANCED:
                return 10;
            default:
                throw new RuntimeException("?");
        }
    }

    public int getSpeedComparedToBasicTier() {
        int mySpeed = this == BASIC ? getMaxTicksInPipe() :
                (this == IMPROVED ? BASIC.getMaxTicksInPipe() + getMaxTicksInPipe() :
                        (this == ADVANCED ? BASIC.getMaxTicksInPipe() + IMPROVED.getMaxTicksInPipe() + getMaxTicksInPipe() :
                                0));

        int speedOfBasicTier = BASIC.getMaxTicksInPipe();

        return (int) ((float) mySpeed / (float) speedOfBasicTier * 100F);
    }

    public TileEntityType<TileItemPipe> getTileType() {
        switch (this) {
            case BASIC:
                return ModTiles.basic_item_pipe;
            case IMPROVED:
                return ModTiles.improved_item_pipe;
            case ADVANCED:
                return ModTiles.advanced_item_pipe;
            default:
                throw new RuntimeException("?");
        }
    }

    public ResourceLocation getId() {
        switch (this) {
            case BASIC:
                return new ResourceLocation(AutoTech.modid, "basic_item_pipe");
            case IMPROVED:
                return new ResourceLocation(AutoTech.modid, "improved_item_pipe");
            case ADVANCED:
                return new ResourceLocation(AutoTech.modid, "advanced_item_pipe");
            default:
                throw new RuntimeException("?");
        }
    }

}
