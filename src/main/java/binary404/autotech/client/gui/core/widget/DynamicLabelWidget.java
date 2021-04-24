package binary404.autotech.client.gui.core.widget;

import binary404.autotech.client.gui.core.math.Position;
import binary404.autotech.client.gui.core.math.Size;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.function.Supplier;

public class DynamicLabelWidget extends Widget {

    protected Supplier<String> textSupplier;
    private String lastTextValue = "";
    private int color;

    public DynamicLabelWidget(int xPosition, int yPosition, Supplier<String> text) {
        this(xPosition, yPosition, text, 0x404040);
    }

    public DynamicLabelWidget(int xPosition, int yPosition, Supplier<String> text, int color) {
        super(new Position(xPosition, yPosition), Size.ZERO);
        this.textSupplier = text;
        this.color = color;
    }

    private void updateSize() {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        String resultText = lastTextValue;
        setSize(new Size(fontRenderer.getStringWidth(resultText), fontRenderer.FONT_HEIGHT));
        if (uiAccess != null) {
            uiAccess.notifySizeChange();
        }
    }

    @Override
    public void drawInForeground(MatrixStack stack, int mouseX, int mouseY) {
        String suppliedText = textSupplier.get();
        if (!suppliedText.equals(lastTextValue)) {
            this.lastTextValue = suppliedText;
            updateSize();
        }
        String[] split = textSupplier.get().split("\n");
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        Position position = getPosition();
        for (int i = 0; i < split.length; i++) {
            stack.push();
            fontRenderer.drawString(stack, split[i], position.x, position.y + (i * (fontRenderer.FONT_HEIGHT + 2)), color);
            stack.pop();
        }
    }

}
