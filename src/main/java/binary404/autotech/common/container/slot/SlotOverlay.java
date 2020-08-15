package binary404.autotech.common.container.slot;

import binary404.autotech.AutoTech;
import net.minecraft.util.ResourceLocation;

public class SlotOverlay {

    public static SlotOverlay SLOT = new SlotOverlay("slot");
    public static SlotOverlay ENERGY = new SlotOverlay("ov_energy");
    public static SlotOverlay DISCHARGING = new SlotOverlay("ov_discharge");
    public static SlotOverlay BINDING_CARD = new SlotOverlay("ov_card");
    public static SlotOverlay FILTER = new SlotOverlay("ov_filter");
    public static SlotOverlay SPEED_UP = new SlotOverlay("ov_speed_up");
    public static SlotOverlay STACK_UP = new SlotOverlay("ov_stack_up");
    public static SlotOverlay BUCKET_UP = new SlotOverlay("ov_bucket_up");

    private ResourceLocation location;

    public SlotOverlay(String s) {
        this.location = new ResourceLocation(AutoTech.modid, "textures/gui/container/slot/" + s + ".png");
    }

    public ResourceLocation getLocation() {
        return this.location;
    }

    public SlotOverlay setLocation(ResourceLocation location) {
        this.location = location;
        return this;
    }

}
