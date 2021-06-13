package binary404.autotech.common.tile.transfer.attachment;

import binary404.autotech.AutoTech;
import binary404.autotech.common.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public enum ExtractorAttachmentType {
    BASIC(1),
    IMPROVED(2),
    ADVANCED(3),
    ELITE(4),
    ULTIMATE(5);

    private final int tier;

    ExtractorAttachmentType(int tier) {
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }

    int getItemTickInterval() {
        return 60;
    }

    int getFluidTickInterval() {
        return 0;
    }

    public int getItemsToExtract() {
        return 8;
    }

    int getFluidsToExtract() {
        return 100;
    }

    public int getFilterSlots() {
        return 0;
    }

    public boolean getCanSetRedstoneMode() {
        return false;
    }

    public boolean getCanSetWhitelistBlacklist() {
        return false;
    }

    public boolean getCanSetRoutingMode() {
        return false;
    }

    public boolean getCanSetExactMode() {
        return false;
    }

    public ResourceLocation getId() {
        switch (this) {
            case BASIC:
                return new ResourceLocation(AutoTech.modid, "basic_extractor");
            case IMPROVED:
                return new ResourceLocation(AutoTech.modid, "improved_extractor");
            case ADVANCED:
                return new ResourceLocation(AutoTech.modid, "advanced_extractor");
            case ELITE:
                return new ResourceLocation(AutoTech.modid, "elite_extractor");
            case ULTIMATE:
                return new ResourceLocation(AutoTech.modid, "ultimate_extractor");
            default:
                throw new RuntimeException("?");
        }
    }

    ResourceLocation getItemId() {
        switch (this) {
            case BASIC:
                return new ResourceLocation(AutoTech.modid, "basic_extractor");
            case IMPROVED:
                return new ResourceLocation(AutoTech.modid, "improved_extractor");
            case ADVANCED:
                return new ResourceLocation(AutoTech.modid, "advanced_extractor");
            case ELITE:
                return new ResourceLocation(AutoTech.modid, "elite_extractor");
            case ULTIMATE:
                return new ResourceLocation(AutoTech.modid, "ultimate_extractor");
            default:
                throw new RuntimeException("?");
        }
    }

    Item getItem() {
        switch (this) {
            case BASIC:
                return ModItems.basic_extractor;
            case IMPROVED:
                return ModItems.improved_extractor;
            case ADVANCED:
                return ModItems.advanced_extractor;
            case ELITE:
                return ModItems.elite_extractor;
            case ULTIMATE:
                return ModItems.ultimate_extractor;
            default:
                throw new RuntimeException("?");
        }
    }

    ResourceLocation getModelLocation() {
        switch (this) {
            case BASIC:
                return new ResourceLocation(AutoTech.modid, "block/pipe/attachment/extractor/basic");
            case IMPROVED:
                return new ResourceLocation(AutoTech.modid, "block/pipe/attachment/extractor/improved");
            case ADVANCED:
                return new ResourceLocation(AutoTech.modid, "block/pipe/attachment/extractor/advanced");
            case ELITE:
                return new ResourceLocation(AutoTech.modid, "block/pipe/attachment/extractor/elite");
            case ULTIMATE:
                return new ResourceLocation(AutoTech.modid, "block/pipe/attachment/extractor/ultimate");
            default:
                throw new RuntimeException("?");
        }
    }

    public static ExtractorAttachmentType get(byte b) {
        ExtractorAttachmentType[] v = values();

        if (b < 0 || b >= v.length) {
            return BASIC;
        }

        return v[b];
    }
}
