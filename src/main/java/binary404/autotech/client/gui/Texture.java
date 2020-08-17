package binary404.autotech.client.gui;

import binary404.autotech.AutoTech;
import binary404.autotech.common.core.logistics.Redstone;
import binary404.autotech.common.tile.util.TransferType;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class Texture extends AbstractGui {

    public static final Texture EMPTY = register("empty", 0, 0, 0, 0);

    public static final Map<TransferType, Texture> CONFIG_ENERGY = new HashMap<>();

    public static final Map<TransferType, Texture> CONFIG_ITEM = new HashMap<>();
    public static final Texture CONFIG_BTN_ALL_ENERGY = register("container/button_energy", 8, 8, 24, 0);
    public static final Texture CONFIG_BTN_OUT_ENERGY = register("container/button_energy", 8, 8, 0, 0);
    public static final Texture CONFIG_BTN_IN_ENERGY = register("container/button_energy", 8, 8, 8, 0);
    public static final Texture CONFIG_BTN_OFF_ENERGY = register("container/button_energy", 8, 8, 16, 0);

    public static final Texture CONFIG_BTN_ALL_ITEM = register("container/button_energy", 8, 8, 24, 8);
    public static final Texture CONFIG_BTN_OUT_ITEM = register("container/button_energy", 8, 8, 0, 8);
    public static final Texture CONFIG_BTN_IN_ITEM = register("container/button_energy", 8, 8, 8, 8);
    public static final Texture CONFIG_BTN_OFF_ITEM = register("container/button_energy", 8, 8, 16, 8);

    public static final Map<Redstone, Texture> REDSTONE = new HashMap<>();
    public static final Texture REDSTONE_BTN_BG = register("container/button_ov", 15, 16, 23, 0);
    public static final Texture REDSTONE_BTN_IGNORE = register("container/button_ov", 9, 8, 38, 0);
    public static final Texture REDSTONE_BTN_OFF = register("container/button_ov", 9, 8, 47, 0);
    public static final Texture REDSTONE_BTN_ON = register("container/button_ov", 9, 8, 38, 8);

    public static final Texture SMELTER = register("container/furnator", 176, 166, 0, 0);

    public static final Texture BIO_GENERATOR = register("container/bio_generator", 176, 166, 0, 0);

    public static final Texture ENERGY_GAUGE = register("container/furnator", 14, 62, 176, 0);
    public static final Texture PROGRESS = register("container/furnator", 14, 14, 176, 69);

    static {
        CONFIG_ENERGY.put(TransferType.ALL, CONFIG_BTN_ALL_ENERGY);
        CONFIG_ENERGY.put(TransferType.EXTRACT, CONFIG_BTN_OUT_ENERGY);
        CONFIG_ENERGY.put(TransferType.RECEIVE, CONFIG_BTN_IN_ENERGY);
        CONFIG_ENERGY.put(TransferType.NONE, CONFIG_BTN_OFF_ENERGY);

        CONFIG_ITEM.put(TransferType.ALL, CONFIG_BTN_ALL_ITEM);
        CONFIG_ITEM.put(TransferType.EXTRACT, CONFIG_BTN_OUT_ITEM);
        CONFIG_ITEM.put(TransferType.RECEIVE, CONFIG_BTN_IN_ITEM);
        CONFIG_ITEM.put(TransferType.NONE, CONFIG_BTN_OFF_ITEM);

        REDSTONE.put(Redstone.IGNORE, REDSTONE_BTN_IGNORE);
        REDSTONE.put(Redstone.ON, REDSTONE_BTN_ON);
        REDSTONE.put(Redstone.OFF, REDSTONE_BTN_OFF);
    }

    private final ResourceLocation location;
    private final int width, height;
    private final int u, v;

    public Texture(ResourceLocation location, int width, int height, int u, int v) {
        this.location = location;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
    }

    static Texture register(String path, int width, int height, int u, int v) {
        return new Texture(new ResourceLocation(AutoTech.modid, "textures/gui/" + path + ".png"), width, height, u, v);
    }

    public void drawScalableW(MatrixStack matrix, float size, int x, int y) {
        scaleW((int) (size * this.width)).draw(matrix, x, y);
    }

    public void drawScalableH(MatrixStack matrix, float size, int x, int y) {
        int i = (int) (size * this.height);
        scaleH(i).moveV(this.height - i).draw(matrix, x, y + this.height - i);
    }

    public void draw(MatrixStack matrix, int x, int y) {
        if (!isEmpty()) {
            bindTexture(getLocation());
            blit(matrix, x, y, getU(), getV(), getWidth(), getHeight());
        }
    }

    public void bindTexture(ResourceLocation guiTexture) {
        Minecraft.getInstance().getTextureManager().bindTexture(guiTexture);
    }

    public Texture scaleW(int width) {
        return scale(width, this.height);
    }

    public Texture scaleH(int height) {
        return scale(this.width, height);
    }

    public Texture scale(int width, int height) {
        return new Texture(this.location, width, height, this.u, this.v);
    }

    public Texture moveU(int u) {
        return move(u, 0);
    }

    public Texture moveV(int v) {
        return move(0, v);
    }

    public Texture move(int u, int v) {
        return new Texture(this.location, this.width, this.height, this.u + u, this.v + v);
    }

    public ResourceLocation getLocation() {
        return this.location;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getU(int i) {
        return this.u + i;
    }

    public int getV(int i) {
        return this.v + i;
    }

    public int getU() {
        return this.u;
    }

    public int getV() {
        return this.v;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public boolean isMouseOver(int x, int y, double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + this.height;
    }

}
