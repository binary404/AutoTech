package binary404.autotech.common.container.machine;

import binary404.autotech.common.container.ModContainers;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.tile.machine.TileDistillery;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class ContainerDistillery extends ContainerTile<TileDistillery> {

    public ContainerDistillery(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(ModContainers.distillery, id, inventory, buffer);
    }

    public ContainerDistillery(int id, PlayerInventory inventory, TileDistillery te) {
        super(ModContainers.distillery, id, inventory, te);
    }

    public static ContainerDistillery create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new ContainerDistillery(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, TileDistillery te) {
        super.init(inventory, te);
        addPlayerInventory(inventory, 8, 84, 4);
    }

}
