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
import java.util.ArrayList;
import java.util.List;

public class BlockGenerator extends BlockTile {

    public static List<BlockGenerator> generators = new ArrayList<>();

    FuelRecipeMap recipeMap;
    Tier tier;
    OrientedOverlayRenderer renderer;

    public BlockGenerator(FuelRecipeMap recipeMap, OrientedOverlayRenderer renderer, Tier tier) {
        super();
        this.recipeMap = recipeMap;
        this.tier = tier;
        this.renderer = renderer;
        generators.add(this);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileSimpleGenerator(recipeMap, renderer, tier);
    }

    public OrientedOverlayRenderer getRenderer() {
        return renderer;
    }

    public Tier getTier() {
        return tier;
    }
}
