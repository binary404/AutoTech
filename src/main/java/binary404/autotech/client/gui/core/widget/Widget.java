package binary404.autotech.client.gui.core.widget;

import binary404.autotech.client.gui.core.IRenderContext;
import binary404.autotech.client.gui.core.ISizeProvider;
import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.client.gui.core.math.Position;
import binary404.autotech.client.gui.core.math.Size;
import com.google.common.base.Preconditions;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public abstract class Widget extends AbstractGui {

    protected ModularUserInterface gui;
    protected ISizeProvider sizes;
    protected WidgetUiAccess uiAccess;
    private Position parentPosition = Position.ORIGIN;
    private Position selfPosition;
    private Position position;
    private Size size;

    public Widget(Position selfPosition, Size size) {
        Preconditions.checkNotNull(selfPosition, "selfPosition");
        Preconditions.checkNotNull(size, "size");
        this.selfPosition = selfPosition;
        this.size = size;
        this.position = this.parentPosition.add(selfPosition);
    }

    public void setGui(ModularUserInterface gui) {
        this.gui = gui;
    }

    public void setSizes(ISizeProvider sizes) {
        this.sizes = sizes;
    }

    public void setUiAccess(WidgetUiAccess uiAccess) {
        this.uiAccess = uiAccess;
    }

    public void setParentPosition(Position parentPosition) {
        Preconditions.checkNotNull(parentPosition, "parentPosition");
        this.parentPosition = parentPosition;
        recomputePosition();
    }

    protected void setSelfPosition(Position selfPosition) {
        Preconditions.checkNotNull(selfPosition, "selfPosition");
        this.selfPosition = selfPosition;
        recomputePosition();
    }

    protected void setSize(Size size) {
        Preconditions.checkNotNull(size, "size");
        this.size = size;
        onSizeUpdate();
    }

    public final Position getPosition() {
        return position;
    }

    public final Size getSize() {
        return size;
    }

    public Rectangle toRectangleBox() {
        Position pos = getPosition();
        Size size = getSize();
        return new Rectangle(pos.x, pos.y, size.width, size.height);
    }

    private void recomputePosition() {
        this.position = this.parentPosition.add(selfPosition);
        onPositionUpdate();
    }

    protected void onPositionUpdate() {
    }

    protected void onSizeUpdate() {
    }

    public boolean isMouseOverElement(double mouseX, double mouseY, boolean correctPositionOnMouseWheelMoveEvent) {
        mouseX = correctPositionOnMouseWheelMoveEvent ? mouseX + getPosition().x : mouseX;
        return isMouseOverElement(mouseX, mouseY);
    }

    public boolean isMouseOverElement(double mouseX, double mouseY) {
        Position position = getPosition();
        Size size = getSize();
        return isMouseOver(position.x, position.y, size.width, size.height, mouseX, mouseY);
    }

    public static boolean isMouseOver(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y && x + width > mouseX && y + height > mouseY;
    }

    /**
     * Called on both sides to initialize widget data
     */
    public void initWidget() {
    }

    /**
     * Called on serverside to detect changes and synchronize them with clients
     */
    public void detectAndSendChanges() {
    }

    /**
     * Called clientside every tick with this modular UI open
     */
    public void updateScreen() {
    }

    /**
     * Called each draw tick to draw this widget in GUI
     */
    public void drawInForeground(MatrixStack stack, int mouseX, int mouseY) {
    }

    /**
     * Called each draw tick to draw this widget in GUI
     */
    public void drawInBackground(MatrixStack stack, int mouseX, int mouseY, IRenderContext context) {
    }

    /**
     * Called when mouse is clicked in GUI
     */
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    /**
     * Called when mouse is released in GUI
     */
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    /**
     * Called when key is typed in GUI
     */
    public boolean keyTyped(char charTyped, int keyCode) {
        return false;
    }

    /**
     * Read data received from server's {@link #writeUpdateInfo}
     */
    public void readUpdateInfo(int id, PacketBuffer buffer) {
    }

    public void handleClientAction(int id, PacketBuffer buffer) {
    }

    public List<INativeWidget> getNativeWidgets() {
        if (this instanceof INativeWidget) {
            return Collections.singletonList((INativeWidget) this);
        }
        return Collections.emptyList();
    }

    /**
     * Writes data to be sent to client's {@link #readUpdateInfo}
     */
    protected final void writeUpdateInfo(int id, Consumer<PacketBuffer> packetBufferWriter) {
        if (uiAccess != null && gui != null) {
            uiAccess.writeUpdateInfo(this, id, packetBufferWriter);
        }
    }

    protected final void writeClientAction(int id, Consumer<PacketBuffer> packetBufferWriter) {
        if (uiAccess != null) {
            uiAccess.writeClientAction(this, id, packetBufferWriter);
        }
    }

    protected static boolean isClientSide() {
        return FMLEnvironment.dist.isClient();
    }

    public static final class ClickData {
        public final int button;
        public final boolean isShiftClick;
        public final boolean isCtrlClick;

        public ClickData(int button, boolean isShiftClick, boolean isCtrlClick) {
            this.button = button;
            this.isShiftClick = isShiftClick;
            this.isCtrlClick = isCtrlClick;
        }

        public void writeToBuf(PacketBuffer buf) {
            buf.writeVarInt(button);
            buf.writeBoolean(isShiftClick);
            buf.writeBoolean(isCtrlClick);
        }

        public static ClickData readFromBuf(PacketBuffer buf) {
            int button = buf.readVarInt();
            boolean shiftClick = buf.readBoolean();
            boolean ctrlClick = buf.readBoolean();
            return new ClickData(button, shiftClick, ctrlClick);
        }
    }
}
