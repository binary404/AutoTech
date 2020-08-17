package binary404.autotech.common.block.generator;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.container.core.ContainerCore;
import binary404.autotech.common.container.generator.BioGeneratorContainer;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileCore;
import binary404.autotech.common.tile.generator.TileBioGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockBioGenerator extends BlockTile {

    Tier tier;

    public BlockBioGenerator(Properties properties, Tier tier) {
        super(properties);
        this.tier = tier;
        setDefaultState();
    }

    @Nullable
    @Override
    public <T extends TileCore> ContainerCore getContainer(int id, PlayerInventory inventory, TileCore te, BlockRayTraceResult result) {
        return new BioGeneratorContainer(id, inventory, (TileBioGenerator) te);
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileBioGenerator(this.tier);
    }

    @Override
    protected Facing getFacing() {
        return Facing.HORIZONTAL;
    }
}
