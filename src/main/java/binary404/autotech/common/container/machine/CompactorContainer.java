package binary404.autotech.common.container.machine;

import binary404.autotech.common.container.ModContainers;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.container.slot.SlotBase;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.machine.TileCompactor;
import binary404.autotech.common.tile.machine.TileCompactor;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class CompactorContainer extends ContainerTile<TileCompactor> {

    public CompactorContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(ModContainers.compactor, id, inventory, buffer);
    }

    public CompactorContainer(int id, PlayerInventory inventory, TileCompactor te) {
        super(ModContainers.compactor, id, inventory, te);
    }

    public static CompactorContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new CompactorContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, TileCompactor te) {
        super.init(inventory, te);

        addSlot(new SlotBase(te.getInventory(), 0, 53, 23));
        addSlot(new SlotBase(te.getInventory(), 1, 97, 23));

        addPlayerInventory(inventory, 8, 84, 4);
    }

}
