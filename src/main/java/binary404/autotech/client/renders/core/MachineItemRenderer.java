package binary404.autotech.client.renders.core;

import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileSimpleMachine;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.LazyValue;

public class MachineItemRenderer extends ItemTileEntityRenderer {

    public MachineItemRenderer() {
        super(new LazyValue<>(TileSimpleMachine::new));
    }
}
