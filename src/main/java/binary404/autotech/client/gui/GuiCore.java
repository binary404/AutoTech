package binary404.autotech.client.gui;

import binary404.autotech.common.container.core.ContainerCore;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class GuiCore<C extends ContainerCore> extends ContainerScreen<C> {

    protected final Minecraft mc = Minecraft.getInstance();
    protected final Texture backGround;
    private List<Rectangle2d> extraAreas = new ArrayList<>();

    public GuiCore(C container, PlayerInventory inv, ITextComponent title, Texture backGround) {
        super(container, inv, title);
        this.backGround = backGround;
        this.xSize = backGround.getWidth();
        this.ySize = backGround.getHeight();
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrix, mouseX, mouseY);
    }

    @Override
    protected void func_230450_a_(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        drawBackground(matrix, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void func_230451_b_(MatrixStack matrix, int mouseX, int mouseY) {
        drawForeground(matrix, mouseX, mouseY);
    }

    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.backGround.draw(matrix, this.guiLeft, this.guiTop);
    }

    protected void drawForeground(MatrixStack matrix, int mouseX, int mouseY) {
    }

    public void bindTexture(ResourceLocation guiTexture) {
        this.mc.getTextureManager().bindTexture(guiTexture);
    }

    public boolean isMouseOver(double mouseX, double mouseY, int w, int h) {
        return mouseX >= this.guiLeft && mouseY >= this.guiTop && mouseX < this.guiLeft + w && mouseY < this.guiTop + h;
    }

    public List<Rectangle2d> getExtraAreas() {
        return this.extraAreas;
    }

}
