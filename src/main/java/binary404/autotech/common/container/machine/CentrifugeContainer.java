package binary404.autotech.common.container.machine;

import binary404.autotech.common.container.ModContainers;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.container.slot.SlotBase;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.machine.TileCentrifuge;
import binary404.autotech.common.tile.machine.TileCentrifuge;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

public class CentrifugeContainer extends ContainerTile<TileCentrifuge> {

    public CentrifugeContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(ModContainers.centrifuge, id, inventory, buffer);
    }

    public CentrifugeContainer(int id, PlayerInventory inventory, TileCentrifuge te) {
        super(ModContainers.centrifuge, id, inventory, te);
    }

    public static CentrifugeContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new CentrifugeContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, TileCentrifuge te) {
        super.init(inventory, te);
        addSlot(new SlotBase(te.getInventory(), 0, 53, 23));
        addSlot(new SlotBase(te.getInventory(), 1, 97, 23));
        addSlot(new SlotBase(te.getInventory(), 2, 127, 23));

        addPlayerInventory(inventory, 8, 84, 4);
    }

}
