package binary404.autotech.common.container.machine;

import binary404.autotech.common.container.ModContainers;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.container.slot.SlotBase;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.machine.TileGrinder;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class GrinderContainer extends ContainerTile<TileGrinder> {

    public GrinderContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(ModContainers.grinder, id, inventory, buffer);
    }

    public GrinderContainer(int id, PlayerInventory inventory, TileGrinder te) {
        super(ModContainers.grinder, id, inventory, te);
    }

    public static GrinderContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new GrinderContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, TileGrinder te) {
        super.init(inventory, te);
        addSlot(new SlotBase(te.getInventory(), 0, 53, 23));
        addSlot(new SlotBase(te.getInventory(), 1, 97, 23));
        if (te.tier != Tier.LV) {
            addSlot(new SlotBase(te.getInventory(), 2, 119, 23));
            addSlot(new SlotBase(te.getInventory(), 3, 141, 23));
        }
        addPlayerInventory(inventory, 8, 84, 4);
    }
}
