package binary404.autotech.common.container.machine;

import binary404.autotech.common.container.ModContainers;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.container.slot.SlotBase;
import binary404.autotech.common.tile.machine.TileCharger;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class ChargerContainer extends ContainerTile<TileCharger> {

    public ChargerContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(ModContainers.charger, id, inventory, buffer);
    }

    public ChargerContainer(int id, PlayerInventory inventory, TileCharger te) {
        super(ModContainers.charger, id, inventory, te);
    }

    public static ChargerContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new ChargerContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, TileCharger te) {
        super.init(inventory, te);

        addSlot(new SlotBase(te.getInventory(), 0, 90, 23));

        addPlayerInventory(inventory, 8, 84, 4);
    }
}
