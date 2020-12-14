package binary404.autotech.common.container.multiblock;

import binary404.autotech.common.container.ModContainers;
import binary404.autotech.common.container.core.ContainerTile;
import binary404.autotech.common.tile.multiblock.TileArcFurnace;
import binary404.autotech.common.tile.multiblock.TileAssemblyLine;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class AssemblyLineContainer extends ContainerTile<TileAssemblyLine> {

    public AssemblyLineContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(ModContainers.assembly_line, id, inventory, buffer);
    }

    public AssemblyLineContainer(int id, PlayerInventory inventory, TileAssemblyLine te) {
        super(ModContainers.assembly_line, id, inventory, te);
    }

    protected void init(PlayerInventory inventory, TileAssemblyLine te) {
        super.init(inventory, te);

        addPlayerInventory(inventory, 8, 84, 4);
    }

}
