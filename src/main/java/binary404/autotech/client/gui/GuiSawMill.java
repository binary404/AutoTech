package binary404.autotech.client.gui;

import binary404.autotech.client.gui.core.GuiEnergy;
import binary404.autotech.client.gui.core.Texture;
import binary404.autotech.common.container.machine.SawMillContainer;
import binary404.autotech.common.tile.machine.TileSawMill;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class GuiSawMill extends GuiEnergy<TileSawMill, SawMillContainer> {

    public GuiSawMill(SawMillContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Texture.SAWMILL);
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Texture.ENERGY_GAUGE.drawScalableH(matrix, this.te.getEnergy().subSized(), this.guiLeft + 5, this.guiTop + 5);
        Texture.PROGRESS_ARROW.drawQuanity(this.te.getScaledProgress(), matrix, this.guiLeft + 72, this.guiTop + 25);
    }
}
