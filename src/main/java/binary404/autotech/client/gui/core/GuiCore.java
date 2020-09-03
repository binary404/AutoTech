package binary404.autotech.client.gui.core;

import binary404.autotech.common.container.core.ContainerCore;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class GuiCore<C extends ContainerCore> extends ContainerScreen<C> {

    protected final Minecraft mc = Minecraft.getInstance();
    protected Texture backGround;
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
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        drawBackground(matrix, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrix, int mouseX, int mouseY) {
        drawForeground(matrix, mouseX, mouseY);
    }

    @Override
    protected void func_230459_a_(MatrixStack matrixStack, int x, int y) {
        super.func_230459_a_(matrixStack, x, y);

        for(Widget widget : this.buttons) {
            if(widget.isHovered())
                widget.renderToolTip(matrixStack, x, y);
        }
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
    public static void gaugeV(TextureAtlasSprite sprite, int x, int y, int w, int h, int cap, int cur) {
        if (cap > 0 && cur > 0) {
            int i = (int) (((float) cur / cap) * h);
            final int j = i / 16;
            final int k = i - j * 16;
            for (int l = 0; l <= j; l++) {
                int height = l == j ? k : 16;
                int yy = (y - (l + 1) * 16) + h;
                if (height > 0) {
                    int m = 16 - height;
                    int n = 16 - w;
                    float uMin = sprite.getMinU();
                    float uMax = sprite.getMaxU();
                    float vMin = sprite.getMinV();
                    float vMax = sprite.getMaxV();
                    uMax = uMax - n / 16.0F * (uMax - uMin);
                    vMin = vMin - m / 16.0F * (vMin - vMax);
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder buffer = tessellator.getBuffer();
                    buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
                    buffer.pos(x, yy + 16, 0).tex(uMin, vMax).endVertex();
                    buffer.pos(x + w, yy + 16, 0).tex(uMax, vMax).endVertex();
                    buffer.pos(x + w, yy + m, 0).tex(uMax, vMin).endVertex();
                    buffer.pos(x, yy + m, 0).tex(uMin, vMin).endVertex();
                    tessellator.draw();
                }
            }
        }
    }


}
