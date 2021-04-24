package binary404.autotech.client.gui;

import binary404.autotech.client.gui.core.IRenderContext;
import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.client.gui.core.widget.Widget;
import binary404.autotech.common.container.core.ModularContainer;
import binary404.autotech.common.network.PacketUIWidgetUpdate;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.common.MinecraftForge;

public class ModularGui extends ContainerScreen<ModularContainer> implements IRenderContext {

    private final ModularUserInterface modularUI;

    public ModularUserInterface getModularUI() {
        return modularUI;
    }

    public ModularGui(ModularUserInterface modularUI) {
        super(new ModularContainer(modularUI), Minecraft.getInstance().player.inventory, StringTextComponent.EMPTY);
        this.modularUI = modularUI;
    }

    @Override
    protected void init() {
        this.xSize = modularUI.getWidth();
        this.ySize = modularUI.getHeight();
        this.minecraft.player.openContainer = this.container;
        super.init();
        this.modularUI.updateScreenSize(width, height);
    }

    @Override
    public void tick() {
        super.tick();
        modularUI.guiWidgets.values().forEach(Widget::updateScreen);
    }

    public void handleWidgetUpdate(PacketUIWidgetUpdate packet) {
        if (packet.windowId == container.windowId) {
            Widget widget = modularUI.guiWidgets.get(packet.widgetId);
            int updateId = packet.updateData.readVarInt();
            if (widget != null) {
                widget.readUpdateInfo(updateId, packet.updateData);
            }
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.hoveredSlot = null;
        renderBackground(matrixStack);

        RenderSystem.disableRescaleNormal();
        RenderSystem.disableLighting();
        RenderSystem.disableDepthTest();

        drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);

        RenderSystem.pushMatrix();
        RenderSystem.translatef(guiLeft, guiTop, 0.0F);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableRescaleNormal();

        for (int i = 0; i < this.container.inventorySlots.size(); ++i) {
            Slot slot = this.container.inventorySlots.get(i);
            if (slot.isEnabled()) {
                this.moveItems(matrixStack, slot);
            }
            if (isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseX, mouseY) && slot.isEnabled()) {
                renderSlotOverlay(matrixStack, slot);
                setHoveredSlot(slot);
            }
        }

        RenderSystem.popMatrix();

        drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);

        RenderSystem.pushMatrix();
        RenderSystem.translatef(guiLeft, guiTop, 0.0F);

        MinecraftForge.EVENT_BUS.post(new GuiContainerEvent.DrawForeground(this, matrixStack, mouseX, mouseY));

        RenderSystem.enableDepthTest();
        renderItemStackOnMouse(matrixStack, mouseX, mouseY);
        renderReturningItemStack();

        RenderSystem.popMatrix();

        renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    public void setHoveredSlot(Slot hoveredSlot) {
        this.hoveredSlot = hoveredSlot;
    }

    public void renderSlotOverlay(MatrixStack stack, Slot slot) {
        RenderSystem.disableDepthTest();
        int slotX = slot.xPos;
        int slotY = slot.yPos;
        RenderSystem.colorMask(true, true, true, false);
        fillGradient(stack, slotX, slotY, slotX + 16, slotY + 16, -2130706433, -2130706433);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
    }

    private void renderItemStackOnMouse(MatrixStack stack, int mouseX, int mouseY) {
        PlayerInventory inventory = this.minecraft.player.inventory;
        ItemStack itemStack = this.draggedStack.isEmpty() ? inventory.getItemStack() : this.draggedStack;

        if (!itemStack.isEmpty()) {
            int dragOffset = this.draggedStack.isEmpty() ? 8 : 16;
            if (!this.draggedStack.isEmpty() && this.isRightMouseClick) {
                itemStack = itemStack.copy();
                itemStack.setCount(MathHelper.ceil((float) itemStack.getCount() / 2.0F));
            } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                itemStack = itemStack.copy();
                itemStack.setCount(this.dragSplittingRemnant);
            }
            this.drawItemStack(itemStack, mouseX - guiLeft - 8, mouseY - guiTop - dragOffset, null);
        }
    }

    private void renderReturningItemStack() {
        if (!this.returningStack.isEmpty()) {
            float partialTicks = (float) (System.currentTimeMillis() - this.returningStackTime) / 100.0F;
            if (partialTicks >= 1.0F) {
                partialTicks = 1.0F;
                this.returningStack = ItemStack.EMPTY;
            }
            int deltaX = this.returningStackDestSlot.xPos - this.touchUpX;
            int deltaY = this.returningStackDestSlot.yPos - this.touchUpY;
            int currentX = this.touchUpX + (int) ((float) deltaX * partialTicks);
            int currentY = this.touchUpY + (int) ((float) deltaY * partialTicks);
            //noinspection ConstantConditions
            this.drawItemStack(this.returningStack, currentX, currentY, null);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        modularUI.guiWidgets.values().forEach(widget -> {
            matrixStack.push();
            widget.drawInForeground(matrixStack, x, y);
            matrixStack.pop();
        });
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        matrixStack.push();
        modularUI.backgroundPath.draw(matrixStack, guiLeft, guiTop, xSize, ySize);
        matrixStack.pop();
        modularUI.guiWidgets.values().forEach(widget -> {
            matrixStack.push();
            RenderSystem.enableBlend();
            widget.drawInBackground(matrixStack, x, y, this);
            matrixStack.pop();
        });
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean result = modularUI.guiWidgets.values().stream().anyMatch(widget -> widget.mouseClicked(mouseX, mouseY, button));
        if (!result)
            return super.mouseClicked(mouseX, mouseY, button);
        else
            return result;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean result = modularUI.guiWidgets.values().stream().anyMatch(widget -> widget.mouseReleased(mouseX, mouseY, button));
        if (!result)
            return super.mouseReleased(mouseX, mouseY, button);
        else
            return result;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        boolean result = modularUI.guiWidgets.values().stream().anyMatch(widget -> widget.keyTyped(codePoint, modifiers));
        return result;
    }
}
