package binary404.autotech.client.gui;

import binary404.autotech.client.gui.core.GuiTile;
import binary404.autotech.client.gui.core.Texture;
import binary404.autotech.common.container.device.ContainerWaterPump;
import binary404.autotech.common.tile.device.TileWaterPump;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.capability.templates.FluidTank;

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
