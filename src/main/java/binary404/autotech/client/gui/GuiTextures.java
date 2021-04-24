package binary404.autotech.client.gui;

import binary404.autotech.client.gui.core.texture.AdoptableTextureArea;
import binary404.autotech.client.gui.core.texture.TextureArea;

public class GuiTextures {

    public static final TextureArea BACKGROUND = AdoptableTextureArea.fullImage("textures/gui/base/background.png", 176, 166, 3, 3);
    public static final TextureArea BORDERED_BACKGROUND = AdoptableTextureArea.fullImage("textures/gui/base/bordered_background.png", 195, 135, 4, 4);

    public static final TextureArea SLOT = AdoptableTextureArea.fullImage("textures/gui/base/slot.png", 18, 18, 1, 1);
    public static final TextureArea FLUID_SLOT = AdoptableTextureArea.fullImage("textures/gui/base/fluid_slot.png", 18, 18, 1, 1);

    public static final TextureArea INT_CIRCUIT_OVERLAY = TextureArea.fullImage("textures/gui/overlay/int_circuit_overlay.png");
    public static final TextureArea IN_SLOT_OVERLAY = TextureArea.fullImage("textures/gui/overlay/in_slot_overlay.png");
    public static final TextureArea OUT_SLOT_OVERLAY = TextureArea.fullImage("textures/gui/overlay/out_slot_overlay.png");

    public static final TextureArea DISPLAY = TextureArea.fullImage("textures/gui/base/display.png");

    public static final TextureArea TANK_ICON = TextureArea.fullImage("textures/gui/base/tank_icon.png");
    public static final TextureArea INDICATOR_NO_ENERGY = TextureArea.fullImage("textures/gui/base/indicator_no_energy.png");

    public static final TextureArea PROGRESS_BAR_ARROW = TextureArea.fullImage("textures/gui/progress_bar/progress_bar_arrow.png");
}
