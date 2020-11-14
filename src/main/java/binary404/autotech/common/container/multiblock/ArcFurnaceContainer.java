package binary404.autotech.common.container.multiblock;

import binary404.autotech.common.container.ModContainers;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.container.slot.SlotBase;
import binary404.autotech.common.tile.multiblock.TileArcFurnace;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class ArcFurnaceContainer extends ContainerTile<TileArcFurnace> {

    public ArcFurnaceContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(ModContainers.arc_furnace, id, inventory, buffer);
    }

    public ArcFurnaceContainer(int id, PlayerInventory inventory, TileArcFurnace te) {
        super(ModContainers.arc_furnace, id, inventory, te);
    }

    public static ArcFurnaceContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new ArcFurnaceContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, TileArcFurnace te) {
        super.init(inventory, te);
        addSlot(new SlotBase(te.getInventory(), 0, 53, 23));
        addSlot(new SlotBase(te.getInventory(), 1, 97, 23));
        addPlayerInventory(inventory, 8, 84, 4);
    }
}
