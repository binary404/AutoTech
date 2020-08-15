package binary404.autotech.client.gui.widget;

import binary404.autotech.client.gui.Texture;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IconButton extends Button {

    protected final Minecraft mc = Minecraft.getInstance();
    private Screen screen;
    private Texture texture;
    private Texture hovering;
    private Consumer<List<ITextComponent>> tooltipConsumer = stringList -> {
    };


    public IconButton(int x, int y, Texture texture, IPressable onPress, Screen screen) {
        this(x, y, texture, Texture.EMPTY, new StringTextComponent(""), onPress, screen);
    }

    public IconButton(int x, int y, Texture texture, Texture hovering, IPressable onPress, Screen screen) {
        this(x, y, texture, hovering, new StringTextComponent(""), onPress, screen);
    }

    public IconButton(int x, int y, Texture texture, ITextComponent text, IPressable onPress, Screen screen) {
        this(x, y, texture, Texture.EMPTY, text, onPress, screen);
    }

    public IconButton(int x, int y, Texture texture, Texture hovering, ITextComponent text, IPressable onPress, Screen screen) {
        super(x, y, texture.getWidth(), texture.getHeight(), text, onPress);
        this.texture = texture;
        this.screen = screen;
        this.hovering = hovering;
    }

    @Override
    public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float pt) {
        if (this.visible) {
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            blit(matrix, this.texture, this.x, this.y);
        }
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float pt) {
        if (this.visible) {
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            blit(matrix, this.texture, this.x, this.y);
        }
    }

    @Override
    public void renderToolTip(MatrixStack p_230443_1_, int p_230443_2_, int p_230443_3_) {
        List<ITextComponent> tooltip = new ArrayList<>();
        this.tooltipConsumer.accept(tooltip);
        if (!tooltip.isEmpty()) {
            this.screen.renderTooltip(p_230443_1_, tooltip, p_230443_2_, p_230443_3_);
        }
    }

    public void blit(MatrixStack matrix, Texture texture, int x, int y) {
        bindTexture(texture.getLocation());
        blit(matrix, x, y, texture.getU(), texture.getV(), texture.getWidth(), texture.getHeight());
    }

    public void bindTexture(ResourceLocation guiTexture) {
        this.mc.getTextureManager().bindTexture(guiTexture);
    }

    public Screen getScreen() {
        return this.screen;
    }

    public IconButton setScreen(Screen screen) {
        this.screen = screen;
        return this;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public IconButton setTexture(Texture texture) {
        this.texture = texture;
        return this;
    }

    public Texture getHovering() {
        return this.hovering;
    }

    public IconButton setHovering(Texture hovering) {
        this.hovering = hovering;
        return this;
    }

    public Consumer<List<ITextComponent>> getTooltip() {
        return this.tooltipConsumer;
    }

    public IconButton setTooltip(Consumer<List<ITextComponent>> tooltipConsumer) {
        this.tooltipConsumer = tooltipConsumer;
        return this;
    }

    public static final IconButton EMPTY = new IconButton(0, 0, Texture.EMPTY, b -> {
    }, new ChatScreen(""));

}
