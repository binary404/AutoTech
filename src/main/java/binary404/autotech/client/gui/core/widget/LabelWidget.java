package binary404.autotech.client.gui.core.widget;

import binary404.autotech.client.gui.core.IRenderContext;
import binary404.autotech.client.gui.core.math.Position;
import binary404.autotech.client.gui.core.math.Size;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

public class LabelWidget extends Widget {

    protected boolean xCentered = false;

    protected String text;
    protected Object[] formatting;
    private int color;

    public LabelWidget(int xPosition, int yPosition, String text, Object... formatting) {
        this(xPosition, yPosition, text, 0x404040, formatting);
    }

    public LabelWidget(int xPosition, int yPosition, String text, int color) {
        this(xPosition, yPosition, text, color, new Object[0]);
    }

    public LabelWidget(int xPosition, int yPosition, String text, int color, Object[] formatting) {
        super(new Position(xPosition, yPosition), Size.ZERO);
        this.text = text;
        this.color = color;
        this.formatting = formatting;
        recomputeSize();
    }

    private String getResultText() {
        return I18n.format(text, formatting);
    }

    private void recomputeSize() {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        String resultText = getResultText();
        setSize(new Size(fontRenderer.getStringWidth(resultText), fontRenderer.FONT_HEIGHT));
        if (uiAccess != null) {
            uiAccess.notifySizeChange();
        }
    }

    public LabelWidget setXCentered(boolean xCentered) {
        this.xCentered = xCentered;
        return this;
    }

    @Override
    public void drawInBackground(MatrixStack stack, int mouseX, int mouseY, IRenderContext context) {
        String resultText = getResultText();
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        Position pos = getPosition();
        if (!xCentered) {
            fontRenderer.drawString(stack, resultText, pos.x, pos.y, color);
        } else {
            fontRenderer.drawString(stack, resultText,
                    pos.x - fontRenderer.getStringWidth(resultText) / 2, pos.y, color);
        }
    }
}
