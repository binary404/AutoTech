package binary404.autotech.common.container.machine;

import binary404.autotech.common.container.ModContainers;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.container.slot.SlotBase;
import binary404.autotech.common.tile.machine.TileSawMill;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.event.world.NoteBlockEvent;

public class SawMillContainer extends ContainerTile<TileSawMill> {

    public SawMillContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(ModContainers.sawmill, id, inventory, buffer);
    }

    public SawMillContainer(int id, PlayerInventory inventory, TileSawMill te) {
        super(ModContainers.sawmill, id, inventory, te);
    }

    public static SawMillContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new SawMillContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, TileSawMill te) {
        super.init(inventory, te);
        addSlot(new SlotBase(te.getInventory(), 0, 53, 23));
        addSlot(new SlotBase(te.getInventory(), 1, 97, 23));
        addSlot(new SlotBase(te.getInventory(), 2, 127, 23));
        addPlayerInventory(inventory, 8, 84, 4);
    }
}
