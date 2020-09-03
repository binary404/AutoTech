package binary404.autotech.common.container.device;

import binary404.autotech.common.container.ModContainers;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.container.device.ContainerWaterPump;
import binary404.autotech.common.container.slot.SlotBase;
import binary404.autotech.common.tile.device.TileWaterPump;
import binary404.autotech.common.tile.generator.TileSteamGenerator;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class ContainerWaterPump extends ContainerTile<TileWaterPump> {

    public ContainerWaterPump(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(ModContainers.waterpump, id, inventory, buffer);
    }

    public ContainerWaterPump(int id, PlayerInventory inventory, TileWaterPump te) {
        super(ModContainers.waterpump, id, inventory, te);
    }

    public static ContainerWaterPump create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new ContainerWaterPump(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, TileWaterPump te) {
        super.init(inventory, te);
        addPlayerInventory(inventory, 8, 84, 4);
    }

}
