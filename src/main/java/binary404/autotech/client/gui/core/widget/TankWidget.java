package binary404.autotech.client.gui.core.widget;

import binary404.autotech.client.gui.core.IRenderContext;
import binary404.autotech.client.gui.core.math.Position;
import binary404.autotech.client.gui.core.math.Size;
import binary404.autotech.client.gui.core.texture.TextureArea;
import binary404.autotech.client.util.RenderUtil;
import binary404.autotech.common.core.util.FluidHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

public class TankWidget extends Widget implements IIngredientSlot {

    public final IFluidTank fluidTank;

    public int fluidRenderOffset = 1;
    private boolean hideToolTip;
    private boolean alwaysShowFull;

    private boolean allowClickFilling;
    private boolean allowClickEmptying;

    private TextureArea[] backgroundTexture;
    private TextureArea overlayTexture;

    private FluidStack lastFluidInTank;
    private int lastTankCapacity;

    public TankWidget(IFluidTank tank, int x, int y, int width, int height) {
        super(new Position(x, y), new Size(width, height));
        this.fluidTank = tank;
    }

    public TankWidget setHideTooltip(boolean hideToolTip) {
        this.hideToolTip = hideToolTip;
        return this;
    }

    public TankWidget setAlwaysShowFull(boolean alwaysShowFull) {
        this.alwaysShowFull = alwaysShowFull;
        return this;
    }

    public TankWidget setBackgroundTexture(TextureArea... backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
        return this;
    }

    public TankWidget setOverlayTexture(TextureArea overlayTexture) {
        this.overlayTexture = overlayTexture;
        return this;
    }

    public TankWidget setFluidRenderOffset(int fluidRenderOffset) {
        this.fluidRenderOffset = fluidRenderOffset;
        return this;
    }

    public TankWidget setContainerClicking(boolean allowClickFilling, boolean allowClickEmptying) {
        if (!(fluidTank instanceof IFluidHandler))
            throw new IllegalStateException("Container IO is only supported for IFluidHandler tanks");
        this.allowClickFilling = allowClickFilling;
        this.allowClickEmptying = allowClickEmptying;
        return this;
    }

    @Override
    public Object getIngredientOverMouse(int mouseX, int mouseY) {
        if (isMouseOverElement(mouseX, mouseY)) {
            return lastFluidInTank;
        }
        return null;
    }

    public String getFormattedFluidAmount() {
        return String.format("%,d", lastFluidInTank == null ? 0 : lastFluidInTank.getAmount());
    }

    public String getFluidLocalizedName() {
        return lastFluidInTank == null ? "" : I18n.format(lastFluidInTank.getTranslationKey());
    }

    @Override
    public void drawInBackground(MatrixStack stack, int mouseX, int mouseY, IRenderContext context) {
        Position pos = getPosition();
        Size size = getSize();
        if (backgroundTexture != null) {
            for (TextureArea textureArea : backgroundTexture) {
                stack.push();
                textureArea.draw(stack, pos.x, pos.y, size.width, size.height);
                stack.pop();
            }
        }
        if (lastFluidInTank != null && lastFluidInTank.getAmount() > 0) {
            RenderSystem.disableBlend();
            RenderUtil.drawFluidForGui(lastFluidInTank, alwaysShowFull ? lastFluidInTank.getAmount() : lastTankCapacity,
                    pos.x + fluidRenderOffset, pos.y + fluidRenderOffset,
                    size.width - fluidRenderOffset, size.height - fluidRenderOffset);
            int bucketsAmount = lastFluidInTank.getAmount() / 1000;
            if (alwaysShowFull && !hideToolTip && bucketsAmount > 0) {
                String s = String.valueOf(bucketsAmount);
                FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
                fontRenderer.drawStringWithShadow(stack, s, pos.x + 1 + size.width - 2 - fontRenderer.getStringWidth(s), pos.y + (size.height / 3) + 3, 0xFFFFFF);
            }
            RenderSystem.enableBlend();
        }
        if (overlayTexture != null) {
            overlayTexture.draw(stack, pos.x, pos.y, size.width, size.height);
        }
    }

    @Override
    public void drawInForeground(MatrixStack stack, int mouseX, int mouseY) {
        super.drawInForeground(stack, mouseX, mouseY);
    }

    @Override
    public void detectAndSendChanges() {
        FluidStack fluidStack = fluidTank.getFluid();

        if (fluidTank.getCapacity() != lastTankCapacity) {
            this.lastTankCapacity = fluidTank.getCapacity();
            writeUpdateInfo(0, buffer -> buffer.writeVarInt(lastTankCapacity));
        }

        if (fluidStack == null && lastFluidInTank != null) {
            this.lastFluidInTank = null;
            writeUpdateInfo(1, buffer -> {
            });
        } else if (fluidStack != null) {
            if (!FluidHelper.isFluidEqual(fluidStack, lastFluidInTank)) {
                this.lastFluidInTank = fluidStack.copy();
                CompoundNBT fluidStackTag = fluidStack.writeToNBT(new CompoundNBT());
                writeUpdateInfo(2, buffer -> buffer.writeCompoundTag(fluidStackTag));
            } else if (fluidStack.getAmount() != lastFluidInTank.getAmount()) {
                this.lastFluidInTank.setAmount(fluidStack.getAmount());
                writeUpdateInfo(3, buffer -> buffer.writeVarInt(lastFluidInTank.getAmount()));
            }
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketBuffer buffer) {
        if (id == 0) {
            this.lastTankCapacity = buffer.readVarInt();
        } else if (id == 1) {
            this.lastFluidInTank = null;
        } else if (id == 2) {
            CompoundNBT fluidStackTag;
            fluidStackTag = buffer.readCompoundTag();
            this.lastFluidInTank = FluidStack.loadFluidStackFromNBT(fluidStackTag);
        } else if (id == 3 && lastFluidInTank != null) {
            this.lastFluidInTank.setAmount(buffer.readVarInt());
        }

        if (id == 4) {
            ItemStack currentStack = gui.player.inventory.getItemStack();
            int newStackSize = buffer.readVarInt();
            currentStack.setCount(newStackSize);
            gui.player.inventory.setItemStack(currentStack);
        }
    }

    @Override
    public void handleClientAction(int id, PacketBuffer buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            boolean isShiftKeyDown = buffer.readBoolean();
            int clickResult = tryClickContainer(isShiftKeyDown);
            if (clickResult >= 0) {
                writeUpdateInfo(4, buf -> buf.writeVarInt(clickResult));
            }
        }
    }

    private int tryClickContainer(boolean isShiftKeyDown) {
        PlayerEntity player = gui.player;
        ItemStack currentStack = player.inventory.getItemStack();
        int maxAttempts = isShiftKeyDown ? currentStack.getCount() : 1;

        if (allowClickFilling && fluidTank.getFluidAmount() > 0) {
            boolean performedFill = false;
            FluidStack initialFluid = fluidTank.getFluid();
            for (int i = 0; i < maxAttempts; i++) {
                FluidActionResult result = FluidUtil.tryFillContainer(currentStack,
                        (IFluidHandler) fluidTank, Integer.MAX_VALUE, null, true);
                if (!result.isSuccess()) break;
                ItemStack remainingStack = result.getResult();
                if (!remainingStack.isEmpty() && !player.inventory.addItemStackToInventory(remainingStack))
                    break; //do not continue if we can't add resulting container into inventory
                currentStack.shrink(1);
                performedFill = true;
            }
            if (performedFill) {
                SoundEvent soundevent = initialFluid.getFluid().getAttributes().getFillSound(initialFluid);
                player.world.playSound(null, player.getPosX(), player.getPosY() + 0.5, player.getPosZ(),
                        soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
                gui.player.inventory.setItemStack(currentStack);
                return currentStack.getCount();
            }
        }

        if (allowClickEmptying) {
            boolean performedEmptying = false;
            for (int i = 0; i < maxAttempts; i++) {
                FluidActionResult result = FluidUtil.tryEmptyContainer(currentStack, (IFluidHandler) fluidTank, Integer.MAX_VALUE, null, true);
                if (!result.isSuccess())
                    break;
                ItemStack remainingStack = result.getResult();
                if (!remainingStack.isEmpty() && !player.inventory.addItemStackToInventory(remainingStack))
                    break; //do not continue if we can't add resulting container into inventory
                currentStack.shrink(1);
                performedEmptying = true;
            }
            FluidStack filledFluid = fluidTank.getFluid();
            if (performedEmptying) {
                SoundEvent soundevent = filledFluid.getFluid().getAttributes().getEmptySound(filledFluid);
                player.world.playSound(null, player.getPosX(), player.getPosY() + 0.5, player.getPosZ(),
                        soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
                gui.player.inventory.setItemStack(currentStack);
                return currentStack.getCount();
            }
        }
        return -1;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverElement(mouseX, mouseY)) {
            ItemStack currentStack = gui.player.inventory.getItemStack();
            if (button == 0 && (allowClickEmptying || allowClickFilling) && FluidUtil.getFluidHandler(currentStack).isPresent()) {
                boolean isShiftKeyDown = GLFW.glfwGetKey(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
                writeClientAction(1, writer -> writer.writeBoolean(isShiftKeyDown));
                return true;
            }
        }
        return false;
    }
}
