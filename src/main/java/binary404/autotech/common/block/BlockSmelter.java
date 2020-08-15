package binary404.autotech.common.block;

import binary404.autotech.common.container.SmelterContainer;
import binary404.autotech.common.tile.TileCore;
import binary404.autotech.common.tile.TileSmelter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.math.BlockRayTraceResult;

import javax.annotation.Nullable;

public class BlockSmelter extends BlockTile {

    public BlockSmelter(Properties properties) {
        super(properties, TileSmelter.class);
    }

    @Nullable
    @Override
    public <T extends TileCore> Container getContainer(int id, PlayerInventory inventory, TileCore te, BlockRayTraceResult result) {
        return new SmelterContainer(id, inventory, (TileSmelter) te);
    }
}
