package binary404.autotech.common.block.multiblock;

import binary404.autotech.common.container.core.ContainerCore;
import binary404.autotech.common.container.machine.ContainerDistillery;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.core.TileCore;
import binary404.autotech.common.tile.multiblock.TileDistillery;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

public class BlockDistillery extends BlockMultiBlock {

    Tier tier;

    public BlockDistillery(Tier tier) {
        super();
        this.tier = tier;
        setDefaultState();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileDistillery) {
            TileDistillery distillery = (TileDistillery) tile;
            if (FluidUtil.interactWithFluidHandler(player, hand, world, pos, result.getFace())) {
                distillery.sync(4);
                return ActionResultType.SUCCESS;
            }
        }

        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    @Nullable
    @Override
    public <T extends TileCore> ContainerCore getContainer(int id, PlayerInventory inventory, TileCore te, BlockRayTraceResult result) {
        return new ContainerDistillery(id, inventory, (TileDistillery) te);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileDistillery(tier);
    }

    @Override
    protected Facing getFacing() {
        return Facing.HORIZONTAL;
    }
}
