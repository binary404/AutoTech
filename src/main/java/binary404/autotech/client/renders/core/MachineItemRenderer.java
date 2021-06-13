package binary404.autotech.client.renders.core;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileSimpleMachine;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.LazyValue;

import java.util.concurrent.Callable;

public class MachineItemRenderer extends ItemTileEntityRenderer implements Callable<MachineItemRenderer> {

    public MachineItemRenderer(OrientedOverlayRenderer renderer, Tier tier) {
        super(new LazyValue<>(() -> new TileSimpleMachine(renderer, tier)));
    }

    @Override
    public MachineItemRenderer call() throws Exception {
        return this;
    }
}
