package binary404.autotech.client.gui.machine;

import binary404.autotech.client.gui.core.GuiEnergy;
import binary404.autotech.client.gui.core.Texture;
import binary404.autotech.common.container.machine.AssemblerContainer;
import binary404.autotech.common.tile.machine.TileAssembler;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class GuiAssembler extends GuiEnergy<TileAssembler, AssemblerContainer> {

    public GuiAssembler(AssemblerContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Texture.ASSEMBLER);
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Texture.PROGRESS_ARROW.drawQuanity(this.te.getScaledProgress(), matrix, this.guiLeft + 98, this.guiTop + 25);

        FluidTank tank = this.te.getTank();
        drawTank(tank, 157, 5);
    }
}
