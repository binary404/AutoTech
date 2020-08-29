package binary404.autotech.common.container.machine;

import binary404.autotech.common.container.ModContainers;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.container.slot.SlotBase;
import binary404.autotech.common.tile.machine.TileSmelter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class SmelterContainer extends ContainerTile<TileSmelter> {

    public SmelterContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(ModContainers.smelter, id, inventory, buffer);
    }

    public SmelterContainer(int id, PlayerInventory inventory, TileSmelter te) {
        super(ModContainers.smelter, id, inventory, te);
    }

    public static SmelterContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new SmelterContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, TileSmelter te) {
        super.init(inventory, te);
        addSlot(new SlotBase(te.getInventory(), 0, 53, 23));
        addSlot(new SlotBase(te.getInventory(), 1, 125, 23));
        addPlayerInventory(inventory, 8, 84, 4);
    }
}
