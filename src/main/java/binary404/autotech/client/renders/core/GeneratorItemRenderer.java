package binary404.autotech.client.renders.core;

import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileSimpleGenerator;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.LazyValue;

public class GeneratorItemRenderer extends ItemTileEntityRenderer {

    public GeneratorItemRenderer() {
        super(new LazyValue<>(TileSimpleGenerator::new));
    }
}
