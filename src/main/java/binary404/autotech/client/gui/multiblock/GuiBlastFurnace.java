package binary404.autotech.client.gui.multiblock;

import binary404.autotech.client.gui.core.GuiEnergy;
import binary404.autotech.client.gui.core.Texture;
import binary404.autotech.common.container.multiblock.ContainerBlastFurnace;
import binary404.autotech.common.tile.machine.TileGrinder;
import binary404.autotech.common.tile.multiblock.TileBlastFurnace;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;

public class GuiBlastFurnace extends GuiEnergy<TileBlastFurnace, ContainerBlastFurnace> {

    public GuiBlastFurnace(ContainerBlastFurnace container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Texture.LV_GRINDER);
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Texture.PROGRESS_ARROW.drawQuanity(this.te.getScaledProgress(), matrix, this.guiLeft + 72, this.guiTop + 25);
    }
}
