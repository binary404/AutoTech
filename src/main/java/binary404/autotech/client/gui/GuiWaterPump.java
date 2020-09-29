package binary404.autotech.client.gui;

import binary404.autotech.client.gui.core.GuiTile;
import binary404.autotech.client.gui.core.Texture;
import binary404.autotech.common.container.device.ContainerWaterPump;
import binary404.autotech.common.core.logistics.Tank;
import binary404.autotech.common.tile.device.TileWaterPump;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class GuiWaterPump extends GuiTile<TileWaterPump, ContainerWaterPump> {

    public GuiWaterPump(ContainerWaterPump container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Texture.WATERPUMP);
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);

        FluidTank tank = this.te.getTank();
        drawTank(tank, 81, 5);
    }

    @Override
    protected boolean hasRedstoneButton() {
        return false;
    }

    @Override
    protected boolean hasItemButton() {
        return false;
    }
}
