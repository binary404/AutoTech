package binary404.autotech.common.block.generator;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.container.core.ContainerCore;
import binary404.autotech.common.container.generator.BioGeneratorContainer;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.core.TileCore;
import binary404.autotech.common.tile.generator.TileSteamGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

public class BlockSteamGenerator extends BlockTile {

    Tier tier;

    public BlockSteamGenerator(Properties properties, Tier tier) {
        super(properties);
        this.tier = tier;
        setDefaultState();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileSteamGenerator) {
            TileSteamGenerator steamGenerator = (TileSteamGenerator) tile;
            if (FluidUtil.interactWithFluidHandler(player, hand, steamGenerator.getTank())) {
                steamGenerator.sync();
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    @Nullable
    @Override
    public <T extends TileCore> ContainerCore getContainer(int id, PlayerInventory inventory, TileCore te, BlockRayTraceResult result) {
        return new BioGeneratorContainer(id, inventory, (TileSteamGenerator) te);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileSteamGenerator(this.tier);
    }

    @Override
    protected Facing getFacing() {
        return Facing.HORIZONTAL;
    }

    @Override
    protected boolean isPlacerFacing() {
        return true;
    }

    @Override
    protected boolean hasLitProp() {
        return true;
    }
}
