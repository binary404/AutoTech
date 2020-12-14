package binary404.autotech.client.gui.machine;

import binary404.autotech.client.gui.core.GuiEnergy;
import binary404.autotech.client.gui.core.Texture;
import binary404.autotech.common.container.machine.ChargerContainer;
import binary404.autotech.common.tile.machine.TileCharger;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class GuiCharger extends GuiEnergy<TileCharger, ChargerContainer> {

    public GuiCharger(ChargerContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Texture.CHARGER);
    }
}
