package binary404.autotech.common.container.multiblock;

import binary404.autotech.common.container.ModContainers;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.container.machine.GrinderContainer;
import binary404.autotech.common.container.slot.SlotBase;
import binary404.autotech.common.tile.machine.TileGrinder;
import binary404.autotech.common.tile.multiblock.TileBlastFurnace;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class ContainerBlastFurnace extends ContainerTile<TileBlastFurnace> {

    public ContainerBlastFurnace(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(ModContainers.blast_furnace, id, inventory, buffer);
    }

    public ContainerBlastFurnace(int id, PlayerInventory inventory, TileBlastFurnace te) {
        super(ModContainers.blast_furnace, id, inventory, te);
    }

    public static ContainerBlastFurnace create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new ContainerBlastFurnace(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, TileBlastFurnace te) {
        super.init(inventory, te);
        addSlot(new SlotBase(te.getInventory(), 0, 53, 23));
        addSlot(new SlotBase(te.getInventory(), 1, 97, 23));
        addPlayerInventory(inventory, 8, 84, 4);
    }
}
