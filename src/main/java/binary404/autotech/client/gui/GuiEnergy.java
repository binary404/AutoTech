package binary404.autotech.client.gui;

import binary404.autotech.client.gui.widget.IconButton;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.core.logistics.Energy;
import binary404.autotech.common.network.PacketEnergyChange;
import binary404.autotech.common.network.PacketHandler;
import binary404.autotech.common.tile.core.TileEnergy;
import binary404.autotech.common.tile.util.IInventory;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class GuiEnergy<T extends TileEnergy<?> & IInventory, C extends ContainerTile<T>> extends GuiTile<T, C> {

    protected IconButton[] energyButtons = new IconButton[6];

    public GuiEnergy(C container, PlayerInventory inv, ITextComponent title, Texture backGround) {
        super(container, inv, title, backGround);
    }

    @Override
    public void init() {
        super.init();
        if (hasEnergyButtons()) {
            addEnergyConfig(0, 2);
        }
        if (hasRedstoneButton()) {
            addRedstoneButton(0, 31);
        }
    }

    protected void addEnergyConfig(int x, int y) {
        for (int i = 0; i < 6; i++) {
            final int id = i;
            Pair<Integer, Integer> offset = getSideButtonOffsets(9).get(i);
            int xOffset = offset.getLeft();
            int yOffset = offset.getRight();
            Direction side = Direction.byIndex(i);
            this.energyButtons[i] = addButton(new IconButton(this.guiLeft + xOffset + this.xSize + x + 8, this.guiTop + yOffset + y + 10, Texture.CONFIG_ENERGY.get(this.te.getEnergyConfig().getType(side)), button -> {
                PacketHandler.sendToServer(new PacketEnergyChange(id, this.te.getPos()));
                this.te.getEnergyConfig().nextType(side);
            }, this).setTooltip(tooltip -> {
                tooltip.add(new TranslationTextComponent("info.lollipop.side." + side.getName2(), TextFormatting.DARK_GRAY));
                tooltip.add(this.te.getEnergyConfig().getType(side).getDisplayName());
            }));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (hasEnergyButtons()) {
            for (int i = 0; i < 6; i++) {
                this.energyButtons[i].setTexture(Texture.CONFIG_ENERGY.get(this.te.getEnergyConfig().getType(Direction.byIndex(i))));
            }
        }
    }

    @Override
    protected void drawForeground(MatrixStack matrix, int mouseX, int mouseY) {
        super.drawForeground(matrix, mouseX, mouseY);
        String title = this.title.getString();
        int width = this.font.getStringWidth(title);
        this.font.drawString(matrix, title, this.xSize / 2 - width / 2, -14.0F, 0x777777);
    }

    protected boolean hasEnergyButtons() {
        return true;
    }

    protected boolean hasRedstoneButton() {
        return true;
    }

    @Override
    protected void func_230459_a_(MatrixStack matrix, int mouseX, int mouseY) {
        super.func_230459_a_(matrix, mouseX, mouseY);
        drawEnergyText(matrix, mouseX, mouseY);
    }

    public void drawEnergyText(MatrixStack matrix, int mouseX, int mouseY) {
        if (Texture.ENERGY_GAUGE.isMouseOver(this.guiLeft + 5, this.guiTop + 5, mouseX, mouseY)) {
            this.renderTooltip(matrix, getEnergyText(), mouseX, mouseY);
        }
    }

    public List<ITextComponent> getEnergyText() {
        List<ITextComponent> list = new ArrayList<>();
        Energy energy = this.te.getEnergy();
        list.add(new TranslationTextComponent("info.autotech.stored_energy", TextFormatting.GRAY + "" + energy.getStored() + "/" + TextFormatting.GRAY + energy.getCapacity()));
        list.add(new TranslationTextComponent("info.autotech.max_transfer", TextFormatting.GRAY + "" + energy.getMaxExtract()));
        return list;
    }

}
