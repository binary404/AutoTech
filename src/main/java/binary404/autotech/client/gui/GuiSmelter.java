package binary404.autotech.client.gui;

import binary404.autotech.client.gui.core.GuiEnergy;
import binary404.autotech.client.gui.core.Texture;
import binary404.autotech.common.container.machine.SmelterContainer;
import binary404.autotech.common.tile.machine.TileSmelter;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class GuiSmelter extends GuiEnergy<TileSmelter, SmelterContainer> {

    public GuiSmelter(SmelterContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Texture.SMELTER);
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Texture.ENERGY_GAUGE.drawScalableH(matrix, this.te.getEnergy().subSized(), this.guiLeft + 5, this.guiTop + 5);
            Texture.PROGRESS.drawScalableH(matrix, this.te.getScaledProgressH(), this.guiLeft + 89, this.guiTop + 25);
    }

}
