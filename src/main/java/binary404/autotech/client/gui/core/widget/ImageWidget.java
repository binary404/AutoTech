package binary404.autotech.client.gui.core.widget;

import binary404.autotech.client.gui.core.IRenderContext;
import binary404.autotech.client.gui.core.math.Position;
import binary404.autotech.client.gui.core.math.Size;
import binary404.autotech.client.gui.core.texture.TextureArea;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.network.PacketBuffer;

import java.util.function.BooleanSupplier;

public class ImageWidget extends Widget {

    protected TextureArea area;

    private BooleanSupplier predicate;
    private boolean isVisible = true;

    public ImageWidget(int xPosition, int yPosition, int width, int height) {
        super(new Position(xPosition, yPosition), new Size(width, height));
    }

    public ImageWidget(int xPosition, int yPosition, int width, int height, TextureArea area) {
        this(xPosition, yPosition, width, height);
        this.area = area;
    }

    public ImageWidget setImage(TextureArea area) {
        this.area = area;
        return this;
    }

    public ImageWidget setPredicate(BooleanSupplier predicate) {
        this.predicate = predicate;
        this.isVisible = false;
        return this;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (predicate != null && predicate.getAsBoolean() != isVisible) {
            this.isVisible = predicate.getAsBoolean();
            writeUpdateInfo(1, buf -> buf.writeBoolean(isVisible));
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketBuffer buffer) {
        super.readUpdateInfo(id, buffer);
        if (id == 1) {
            this.isVisible = buffer.readBoolean();
        }
    }

    @Override
    public void drawInBackground(MatrixStack stack, int mouseX, int mouseY, IRenderContext context) {
        if (!this.isVisible || area == null) return;
        Position position = getPosition();
        Size size = getSize();
        stack.push();
        area.draw(stack, position.x, position.y, size.width, size.height);
        stack.pop();
    }

}
