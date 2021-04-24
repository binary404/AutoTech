package binary404.autotech.client.gui.core.widget;

import binary404.autotech.client.gui.core.IRenderContext;
import binary404.autotech.client.gui.core.math.Position;
import binary404.autotech.client.gui.core.math.Size;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.java.games.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AdvancedTextWidget extends Widget {

    protected int maxWidthLimit;

    private WrapScreen wrapScreen;

    protected Consumer<List<ITextComponent>> textSupplier;
    protected BiConsumer<String, ClickData> clickHandler;
    private List<ITextComponent> displayText = new ArrayList<>();
    private List<IReorderingProcessor> toRender = new ArrayList<>();
    private int color;

    public AdvancedTextWidget(int xPosition, int yPosition, Consumer<List<ITextComponent>> text, int color) {
        super(new Position(xPosition, yPosition), Size.ZERO);
        this.textSupplier = text;
        this.color = color;
    }

    public static ITextComponent withButton(ITextComponent textComponent, String componentData) {
        Style style = textComponent.getStyle();
        style.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "@!" + componentData));
        style.setFormatting(TextFormatting.YELLOW);
        return textComponent;
    }

    public static ITextComponent withHoverTextTranslate(ITextComponent textComponent, String hoverTranslation) {
        Style style = textComponent.getStyle();
        ITextComponent translation = new TranslationTextComponent(hoverTranslation);
        translation.getStyle().setFormatting(TextFormatting.GRAY);
        style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, translation));
        return textComponent;
    }

    public AdvancedTextWidget setMaxWidthLimit(int maxWidthLimit) {
        this.maxWidthLimit = maxWidthLimit;
        if (isClientSide()) {
            updateComponentTextSize();
        }
        return this;
    }

    public AdvancedTextWidget setClickHandler(BiConsumer<String, ClickData> clickHandler) {
        this.clickHandler = clickHandler;
        return this;
    }

    private WrapScreen getWrapScreen() {
        if (wrapScreen == null)
            wrapScreen = new WrapScreen();

        return wrapScreen;
    }

    private void resizeWrapScreen() {
        if (sizes != null) {
            getWrapScreen().resize(Minecraft.getInstance(), sizes.getScreenWidth(), sizes.getScreenHeight());
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();
        if (isClientSide()) {
            resizeWrapScreen();
        }
    }

    @Override
    protected void onPositionUpdate() {
        super.onPositionUpdate();
        if (isClientSide()) {
            resizeWrapScreen();
        }
    }

    @Override
    public void detectAndSendChanges() {
        ArrayList<ITextComponent> textBuffer = new ArrayList<>();
        textSupplier.accept(textBuffer);
        if (!displayText.equals(textBuffer)) {
            this.displayText = textBuffer;
            writeUpdateInfo(1, buffer -> {
                buffer.writeVarInt(displayText.size());
                for (ITextComponent textComponent : displayText) {
                    buffer.writeString(ITextComponent.Serializer.toJson(textComponent));
                }
            });
        }
    }

    protected ITextComponent getTextUnderMouse(double mouseX, double mouseY) {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        Position position = getPosition();
        int selectedLine = (int) ((mouseY - position.y) / (fontRenderer.FONT_HEIGHT + 2));
        if (mouseX >= position.x && selectedLine >= 0 && selectedLine < displayText.size()) {
            ITextComponent selectedComponent = displayText.get(selectedLine);
            double mouseOffset = mouseX - position.x;
            int currentOffset = 0;
            for (ITextComponent lineComponent : selectedComponent.getSiblings()) {
                currentOffset += fontRenderer.getStringWidth(lineComponent.getUnformattedComponentText());
                if (currentOffset >= mouseOffset) {
                    return lineComponent;
                }
            }
        }
        return null;
    }

    private void updateComponentTextSize() {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        int maxStringWidth = 0;
        int totalHeight = 0;
        for (ITextComponent textComponent : displayText) {
            maxStringWidth = Math.max(maxStringWidth, fontRenderer.getStringWidth(textComponent.getString()));
            totalHeight += fontRenderer.FONT_HEIGHT + 2;
        }
        totalHeight -= 2;
        setSize(new Size(maxStringWidth, totalHeight));
        if (uiAccess != null) {
            uiAccess.notifySizeChange();
        }
    }

    private void formatDisplayText() {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        int maxTextWidthResult = maxWidthLimit == 0 ? Integer.MAX_VALUE : maxWidthLimit;
        this.toRender = displayText.stream()
                .flatMap(c -> fontRenderer.trimStringToWidth(c, maxTextWidthResult).stream())
                .collect(Collectors.toList());
    }

    @Override
    public void readUpdateInfo(int id, PacketBuffer buffer) {
        if (id == 1) {
            this.displayText.clear();
            int count = buffer.readVarInt();
            for (int i = 0; i < count; i++) {
                String jsonText = buffer.readString(32767);
                this.displayText.add(ITextComponent.Serializer.getComponentFromJson(jsonText));
            }
            formatDisplayText();
            updateComponentTextSize();
        }
    }

    @Override
    public void handleClientAction(int id, PacketBuffer buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            ClickData clickData = ClickData.readFromBuf(buffer);
            String componentData = buffer.readString(128);
            if (clickHandler != null) {
                clickHandler.accept(componentData, clickData);
            }
        }
    }

    private boolean handleCustomComponentClick(ITextComponent textComponent) {
        Style style = textComponent.getStyle();
        if (style.getClickEvent() != null) {
            ClickEvent clickEvent = style.getClickEvent();
            String componentText = clickEvent.getValue();
            if (clickEvent.getAction() == ClickEvent.Action.OPEN_URL && componentText.startsWith("@!")) {
                String rawText = componentText.substring(2);
                //ClickData clickData = new ClickData(Mouse.getEventButton(), isShiftDown(), isCtrlDown());
                writeClientAction(1, buf -> {
                    //clickData.writeToBuf(buf);
                    buf.writeString(rawText);
                });
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        ITextComponent textComponent = getTextUnderMouse(mouseX, mouseY);
        if (textComponent != null) {
            if (handleCustomComponentClick(textComponent) ||
                    getWrapScreen().handleComponentClicked(textComponent.getStyle())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void drawInBackground(MatrixStack stack, int mouseX, int mouseY, IRenderContext context) {
        super.drawInBackground(stack, mouseX, mouseY, context);
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        Position position = getPosition();
        for (int i = 0; i < toRender.size(); i++) {
            fontRenderer.func_238407_a_(stack, toRender.get(i), position.x, position.y + i * (fontRenderer.FONT_HEIGHT + 2), color);
        }
    }

    @Override
    public void drawInForeground(MatrixStack stack, int mouseX, int mouseY) {
        super.drawInForeground(stack, mouseX, mouseY);
        ITextComponent component = getTextUnderMouse(mouseX, mouseY);
        if (component != null) {
            getWrapScreen().renderTooltip(stack, component, mouseX, mouseY);
        }
    }

    /**
     * Used to call mc-related chat component handling code,
     * for example component hover rendering and default click handling
     */
    private static class WrapScreen extends Screen {

        protected WrapScreen() {
            super(ITextComponent.getTextComponentOrEmpty(""));
        }

        @Override
        public boolean handleComponentClicked(@Nullable Style style) {
            return super.handleComponentClicked(style);
        }

        @Override
        public void renderTooltip(MatrixStack matrixStack, ITextComponent text, int mouseX, int mouseY) {
            super.renderTooltip(matrixStack, text, mouseX, mouseY);
        }

        @Override
        public void renderWrappedToolTip(MatrixStack matrixStack, List<? extends ITextProperties> tooltips, int mouseX, int mouseY, FontRenderer font) {
            GuiUtils.drawHoveringText(matrixStack, tooltips, mouseX, mouseY, width, height, 256, font);
        }
    }

}
