package binary404.autotech.common.core.lib.multiblock;

import binary404.autotech.client.gui.GuiTextures;
import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.client.gui.core.widget.AdvancedTextWidget;
import binary404.autotech.client.gui.core.widget.Widget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;

import java.util.List;

public abstract class MultiblockControllerDisplay extends MultiblockControllerBase {

    public MultiblockControllerDisplay(TileEntityType<?> type) {
        super(type);
    }

    protected void addDisplayText(List<ITextComponent> textList) {
        if (!isStructureFormed()) {
            TextComponent toolTip = new TranslationTextComponent("autotech.multiblock.invalid.tooltip");
            toolTip.mergeStyle(TextFormatting.GRAY);
            textList.add(new TranslationTextComponent("autotech.multiblock.invalid_structure")
                    .mergeStyle(Style.EMPTY.setFormatting(TextFormatting.RED)
                            .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, toolTip))));
        }
    }

    protected void handleDisplayClick(String componentData, Widget.ClickData data) {

    }

    protected ModularUserInterface.Builder createUITemplate(PlayerEntity playerEntity) {
        ModularUserInterface.Builder builder = ModularUserInterface.extendedBuilder();
        builder.image(7, 5, 162, 121, GuiTextures.DISPLAY);
        builder.label(10, 7, getFullName(), 0xFFFFF);
        builder.widget(new AdvancedTextWidget(10, 17, this::addDisplayText, 0xFFFFFF)
                .setMaxWidthLimit(156)
                .setClickHandler(this::handleDisplayClick));
        builder.bindPlayerInventory(playerEntity.inventory, 134);
        return builder;
    }

    public ModularUserInterface createUI(PlayerEntity playerEntity) {
        return createUITemplate(playerEntity).build(this, playerEntity);
    }
}
