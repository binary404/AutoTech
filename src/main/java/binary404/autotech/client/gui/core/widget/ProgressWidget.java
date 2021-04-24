package binary404.autotech.client.gui.core.widget;

import binary404.autotech.client.gui.core.IRenderContext;
import binary404.autotech.client.gui.core.math.Position;
import binary404.autotech.client.gui.core.math.Size;
import binary404.autotech.client.gui.core.texture.TextureArea;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.network.PacketBuffer;

import java.util.function.DoubleSupplier;

public class ProgressWidget extends Widget {

    public enum MoveType {
        VERTICAL,
        HORIZONTAL,
        VERTICAL_INVERTED
    }

    public final DoubleSupplier progressSupplier;
    private MoveType moveType;
    private TextureArea emptyBarArea;
    private TextureArea filledBarArea;

    private double lastProgressValue;

    public ProgressWidget(DoubleSupplier progressSupplier, int x, int y, int width, int height) {
        super(new Position(x, y), new Size(width, height));
        this.progressSupplier = progressSupplier;
    }

    public ProgressWidget(DoubleSupplier progressSupplier, int x, int y, int width, int height, TextureArea fullImage, MoveType moveType) {
        super(new Position(x, y), new Size(width, height));
        this.progressSupplier = progressSupplier;
        this.emptyBarArea = fullImage.getSubArea(0.0F, 0.0F, 1.0F, 0.5F);
        this.filledBarArea = fullImage.getSubArea(0.0F, 0.5F, 1.0F, 0.5F);
        this.moveType = moveType;
    }

    public ProgressWidget setProgressBar(TextureArea emptyBarArea, TextureArea filledBarArea, MoveType moveType) {
        this.emptyBarArea = emptyBarArea;
        this.filledBarArea = filledBarArea;
        this.moveType = moveType;
        return this;
    }

    @Override
    public void drawInBackground(MatrixStack stack, int mouseX, int mouseY, IRenderContext context) {
        Position pos = getPosition();
        Size size = getSize();
        if (emptyBarArea != null) {
            stack.push();
            emptyBarArea.draw(stack, pos.x, pos.y, size.width, size.height);
            stack.pop();
        }
        if (filledBarArea != null) {
            if (moveType == MoveType.HORIZONTAL) {
                stack.push();
                filledBarArea.drawSubArea(stack, pos.x, pos.y, (int) (size.width * lastProgressValue), size.height, 0.0F, 0.0f, ((int) (size.width * lastProgressValue)) / (size.width * 1.0F), 1.0F);
                stack.pop();
            } else if (moveType == MoveType.VERTICAL) {
                int progressValueScaled = (int) (size.height * lastProgressValue);
                stack.push();
                filledBarArea.drawSubArea(stack, pos.x, pos.y + size.height - progressValueScaled, size.width, progressValueScaled, 0.0F, 1.0F - (progressValueScaled / (size.height * 1.0F)), 1.0F, (progressValueScaled / (size.height * 1.0F)));
                stack.pop();
            } else if (moveType == MoveType.VERTICAL_INVERTED) {
                int progressValueScaled = (int) (size.height * lastProgressValue);
                stack.push();
                filledBarArea.drawSubArea(stack, pos.x, pos.y, size.width, progressValueScaled, 0.0F, 0.0F, 1.0F, (progressValueScaled / (size.height * 1.0F)));
                stack.pop();
            }
        }
    }

    @Override
    public void detectAndSendChanges() {
        double actualValue = progressSupplier.getAsDouble();
        if (Math.abs(actualValue - lastProgressValue) > 0.005) {
            this.lastProgressValue = actualValue;
            writeUpdateInfo(0, buffer -> buffer.writeDouble(actualValue));
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketBuffer buffer) {
        if (id == 0) {
            this.lastProgressValue = buffer.readDouble();
        }
    }
}
