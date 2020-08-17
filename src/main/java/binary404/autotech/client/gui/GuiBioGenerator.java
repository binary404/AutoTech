package binary404.autotech.client.gui;

import binary404.autotech.common.container.generator.BioGeneratorContainer;
import binary404.autotech.common.tile.generator.TileBioGenerator;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class GuiBioGenerator extends GuiEnergy<TileBioGenerator, BioGeneratorContainer> {

    public GuiBioGenerator(BioGeneratorContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Texture.BIO_GENERATOR);
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Texture.ENERGY_GAUGE.drawScalableH(matrix, this.te.getEnergy().subSized(), this.guiLeft + 5, this.guiTop + 5);
        if(this.te.isGenerating())
            Texture.PROGRESS.drawScalableH(matrix, this.te.getGenerator().subSized(), this.guiLeft + 113, this.guiTop + 24);
    }
}
