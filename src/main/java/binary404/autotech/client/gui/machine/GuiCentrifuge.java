package binary404.autotech.client.gui.machine;

import binary404.autotech.client.gui.core.GuiEnergy;
import binary404.autotech.client.gui.core.Texture;
import binary404.autotech.common.container.machine.CentrifugeContainer;
import binary404.autotech.common.core.logistics.fluid.Tank;
import binary404.autotech.common.tile.machine.TileCentrifuge;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class GuiCentrifuge extends GuiEnergy<TileCentrifuge, CentrifugeContainer> {

    public GuiCentrifuge(CentrifugeContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Texture.CENTRIFUGE);
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Texture.PROGRESS_ARROW.drawQuanity(this.te.getScaledProgress(), matrix, this.guiLeft + 72, this.guiTop + 25);

        FluidTank tank = this.te.getTank();
        drawTank(tank, 157, 5);
    }

    @Override
    public void drawExtraText(MatrixStack matrix, int mouseX, int mouseY) {
        if (this.isMouseOver(mouseX, mouseY, 14, 62, this.guiLeft + 157, this.guiTop + 5)) {
            List<IReorderingProcessor> list = new ArrayList<>();
            Tank tank = this.te.getTank();
            list.add(new TranslationTextComponent("info.autotech.fluid", tank.getFluid().getFluid().getRegistryName().toString()).func_241878_f());
            list.add(new TranslationTextComponent("info.autotech.fluid_stored", tank.getFluid().getAmount() + "/" + tank.getCapacity()).func_241878_f());
            this.renderTooltip(matrix, list, mouseX, mouseY);
        }
    }

    @Override
    public List<IReorderingProcessor> getEnergyText() {
        List<IReorderingProcessor> text = super.getEnergyText();
        text.add(new TranslationTextComponent("info.autotech.usage", TextFormatting.GRAY + "" + this.te.getEnergyPerUse()).func_241878_f());
        return text;
    }

}
