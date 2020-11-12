package binary404.autotech.common.container.machine;

import binary404.autotech.common.container.ModContainers;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.container.slot.SlotBase;
import binary404.autotech.common.tile.machine.TileAssembler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.AabbHelper;

public class AssemblerContainer extends ContainerTile<TileAssembler> {

    public AssemblerContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(ModContainers.assembler, id, inventory, buffer);
    }

    public AssemblerContainer(int id, PlayerInventory inventory, TileAssembler te) {
        super(ModContainers.assembler, id, inventory, te);
    }

    public static AssemblerContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new AssemblerContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, TileAssembler te) {
        super.init(inventory, te);

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                this.addSlot(new SlotBase(te.getInventory(), j + i * 3, 37 + j * 18, 5 + i * 18));
            }
        }

        addSlot(new SlotBase(te.getInventory(), 9, 145, 23));

        addPlayerInventory(inventory, 8, 84, 4);
    }
}
