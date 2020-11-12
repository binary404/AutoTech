package binary404.autotech.client.gui.core;

import binary404.autotech.client.gui.widget.IconButton;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.core.logistics.Energy;
import binary404.autotech.common.network.PacketHandler;
import binary404.autotech.common.tile.core.TileEnergy;
import binary404.autotech.common.tile.util.IInventory;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Direction;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class GuiEnergy<T extends TileEnergy<?> & IInventory, C extends ContainerTile<T>> extends GuiTile<T, C> {

    public GuiEnergy(C container, PlayerInventory inv, ITextComponent title, Texture backGround) {
        super(container, inv, title, backGround);
    }

    @Override
    protected void drawForeground(MatrixStack matrix, int mouseX, int mouseY) {
        super.drawForeground(matrix, mouseX, mouseY);
        String title = this.title.getString();
        int width = this.font.getStringWidth(title);
        this.font.drawString(matrix, title, this.xSize / 2 - width / 2, -14.0F, 0x777777);
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        drawEnergy(matrix);
    }

    protected void drawEnergy(MatrixStack matrix) {
        Texture.ENERGY_GAUGE.drawScalableH(matrix, this.te.getEnergy().subSized(), this.guiLeft + 5, this.guiTop + 5);
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y) {
        super.renderHoveredTooltip(matrixStack, x, y);
        drawEnergyText(matrixStack, x, y);
        drawExtraText(matrixStack, x, y);
    }


    public void drawEnergyText(MatrixStack matrix, int mouseX, int mouseY) {
        if (Texture.ENERGY_GAUGE.isMouseOver(this.guiLeft + 5, this.guiTop + 5, mouseX, mouseY)) {
            this.renderTooltip(matrix, getEnergyText(), mouseX, mouseY);
        }
    }

    public void drawExtraText(MatrixStack matrix, int mouseX, int mouseY) {

    }

    @Override
    public void renderTooltip(MatrixStack p_238654_1_, List<? extends IReorderingProcessor> p_238654_2_, int p_238654_3_, int p_238654_4_) {
        super.renderTooltip(p_238654_1_, p_238654_2_, p_238654_3_, p_238654_4_);
    }

    public List<IReorderingProcessor> getEnergyText() {
        List<IReorderingProcessor> list = new ArrayList<>();
        Energy energy = this.te.getEnergy();
        list.add(new TranslationTextComponent("info.autotech.stored_energy", TextFormatting.GRAY + "" + energy.getStored() + "/" + TextFormatting.GRAY + energy.getCapacity()).func_241878_f());
        list.add(new TranslationTextComponent("info.autotech.max_transfer", TextFormatting.GRAY + "" + energy.getMaxExtract()).func_241878_f());
        return list;
    }

}
