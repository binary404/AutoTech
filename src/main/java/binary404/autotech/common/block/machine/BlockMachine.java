package binary404.autotech.common.block.machine;

import binary404.autotech.client.renders.core.OrientedOverlayRenderer;
import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.recipe.RecipeMaps;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import binary404.autotech.common.tile.core.TileSimpleMachine;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockMachine extends BlockTile {

    public static List<BlockMachine> machines = new ArrayList<>();

    RecipeMap<?> recipeMap;
    Tier tier;
    OrientedOverlayRenderer renderer;

    public BlockMachine(RecipeMap<?> recipeMap, OrientedOverlayRenderer renderer, Tier tier) {
        super();
        this.recipeMap = recipeMap;
        this.tier = tier;
        this.renderer = renderer;
        machines.add(this);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileSimpleMachine(this.recipeMap, this.renderer, this.tier);
    }
}
