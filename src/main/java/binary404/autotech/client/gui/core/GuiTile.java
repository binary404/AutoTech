package binary404.autotech.client.gui.core;

import binary404.autotech.client.gui.widget.IconButton;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.network.PacketHandler;
import binary404.autotech.common.network.PacketItemChange;
import binary404.autotech.common.network.PacketRedstoneChange;
import binary404.autotech.common.tile.core.TileCore;
import binary404.autotech.common.tile.util.IInventory;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class GuiTile<T extends TileCore<?> & IInventory, C extends ContainerTile<T>> extends GuiCore<C> {

    protected final T te;

    protected IconButton[] itemButtons = new IconButton[6];

    boolean itemButtonVisible = false;
    protected IconButton itemButtonEnable;

    protected IconButton redStoneButton = IconButton.EMPTY;

    public GuiTile(C container, PlayerInventory inv, ITextComponent title, Texture backGround) {
        super(container, inv, title, backGround);
        this.te = container.te;
    }

    @Override
    protected void init() {
        super.init();
        if (hasRedstoneButton()) {
            addRedstoneButton(0, 58);
        }
        this.itemButtonEnable = addButton(new IconButton(this.guiLeft + this.xSize, this.guiTop + 4, Texture.CONFIG_BTN_ALL_ITEM, button -> {
            if (this.itemButtonVisible)
                this.itemButtonVisible = false;
            else
                this.itemButtonVisible = true;
        }, this));

        addItemConfig(0, 12);
    }

    protected void addRedstoneButton(int x, int y) {
        this.redStoneButton = addButton(new IconButton(this.guiLeft + this.xSize + x + 2, this.guiTop + y + 3, Texture.REDSTONE.get(this.te.getRedstoneMode()), b -> {
            PacketHandler.sendToServer(new PacketRedstoneChange(this.te.getPos()));
            this.te.setRedstoneMode(this.te.getRedstoneMode().next());
        }, this).setTooltip(tooltip -> tooltip.add(this.te.getRedstoneMode().getDisplayName())));
    }

    protected void addItemConfig(int x, int y) {
        for (int i = 0; i < 6; i++) {
            final int id = i;
            Pair<Integer, Integer> offset = getSideButtonOffsets(9).get(i);
            int xOffset = offset.getLeft();
            int yOffset = offset.getRight();
            Direction side = Direction.byIndex(i);
            this.itemButtons[i] = addButton(new IconButton(this.guiLeft + xOffset + this.xSize + x + 8, this.guiTop + yOffset + y + 10, Texture.CONFIG_ITEM.get(this.te.itemConfig.getType(side)), button -> {
                PacketHandler.sendToServer(new PacketItemChange(id, this.te.getPos()));
                this.te.itemConfig.nextType(side);
            }, this).setTooltip(tooltip -> {
                tooltip.add(new TranslationTextComponent("info.autotech.side." + side.getName2(), TextFormatting.DARK_GRAY));
                tooltip.add(this.te.itemConfig.getType(side).getDisplayName());
            }));
        }
    }

    protected void removeItemButton() {
        for (int i = 0; i < 6; i++) {
            this.itemButtons[i].visible = false;
        }
    }

    public List<Pair<Integer, Integer>> getSideButtonOffsets(int spacing) {
        return Lists.newArrayList(Pair.of(0, spacing), Pair.of(0, -spacing), Pair.of(0, 0), Pair.of(spacing, spacing), Pair.of(-spacing, 0), Pair.of(spacing, 0));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.itemButtonVisible) {
            for (int i = 0; i < 6; i++) {
                this.itemButtons[i].setTexture(Texture.CONFIG_ITEM.get(this.te.itemConfig.getType(Direction.byIndex(i))));
                this.itemButtons[i].visible = true;
            }
        } else {
            removeItemButton();
        }
        this.redStoneButton.setTexture(Texture.REDSTONE.get(this.te.getRedstoneMode()));
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Texture.REDSTONE_BTN_BG.draw(matrix, this.redStoneButton.x - 2, this.redStoneButton.y - 4);
        if (this.itemButtonVisible)
            Texture.CONFIG_BTN_BG.draw(matrix, this.itemButtons[1].x - 8, this.itemButtons[1].y - 10);
        else
            Texture.REDSTONE_BTN_BG.draw(matrix, this.itemButtonEnable.x - 1, this.itemButtonEnable.y - 4);
    }

    protected boolean hasRedstoneButton() {
        return true;
    }

}
