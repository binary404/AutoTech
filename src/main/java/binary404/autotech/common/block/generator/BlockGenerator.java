package binary404.autotech.common.block.generator;

import binary404.autotech.client.renders.core.OrientedOverlayRenderer;
import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.recipe.map.FuelRecipeMap;
import binary404.autotech.common.tile.core.TileSimpleGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockGenerator extends BlockTile {

    FuelRecipeMap recipeMap;
    Tier tier;
    OrientedOverlayRenderer renderer;

    public BlockGenerator(FuelRecipeMap recipeMap, OrientedOverlayRenderer renderer, Tier tier) {
        super();
        this.recipeMap = recipeMap;
        this.tier = tier;
        this.renderer = renderer;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileSimpleGenerator(recipeMap, renderer, tier);
    }
}
