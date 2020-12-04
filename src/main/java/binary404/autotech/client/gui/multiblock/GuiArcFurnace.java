package binary404.autotech.client.gui.multiblock;

import binary404.autotech.client.gui.core.GuiEnergy;
import binary404.autotech.client.gui.core.Texture;
import binary404.autotech.common.container.multiblock.ArcFurnaceContainer;
import binary404.autotech.common.tile.multiblock.TileArcFurnace;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class GuiArcFurnace extends GuiEnergy<TileArcFurnace, ArcFurnaceContainer> {

    public GuiArcFurnace(ArcFurnaceContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Texture.ARC_FURNACE);
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Texture.PROGRESS_ARROW.drawQuanity(this.te.getScaledProgress(), matrix, this.guiLeft + 103, this.guiTop + 25);
    }
}
