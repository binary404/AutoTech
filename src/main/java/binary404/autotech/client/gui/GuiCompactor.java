package binary404.autotech.client.gui;

import binary404.autotech.client.gui.core.GuiEnergy;
import binary404.autotech.client.gui.core.Texture;
import binary404.autotech.common.container.machine.CompactorContainer;
import binary404.autotech.common.tile.machine.TileCompactor;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class GuiCompactor extends GuiEnergy<TileCompactor, CompactorContainer> {

    public GuiCompactor(CompactorContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Texture.LV_GRINDER);
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Texture.PROGRESS_ARROW.drawQuanity(this.te.getScaledProgress(), matrix, this.guiLeft + 72, this.guiTop + 25);
    }

    @Override
    public List<IReorderingProcessor> getEnergyText() {
        List<IReorderingProcessor> text = super.getEnergyText();
        text.add(new TranslationTextComponent("info.autotech.usage", TextFormatting.GRAY + "" + this.te.getEnergyPerUse()).func_241878_f());
        return text;
    }

}
