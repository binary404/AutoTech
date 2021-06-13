package binary404.autotech.client.renders.core;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileSimpleGenerator;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.LazyValue;

import java.util.concurrent.Callable;

public class GeneratorItemRenderer extends ItemTileEntityRenderer implements Callable<GeneratorItemRenderer> {

    public GeneratorItemRenderer(OrientedOverlayRenderer renderer, Tier tier) {
        super(new LazyValue<>(() -> new TileSimpleGenerator(renderer, tier)));
    }

    @Override
    public GeneratorItemRenderer call() {
        return this;
    }
}
