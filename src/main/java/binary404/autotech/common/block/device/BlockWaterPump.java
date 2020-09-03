package binary404.autotech.common.block.device;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.container.core.ContainerCore;
import binary404.autotech.common.container.device.ContainerWaterPump;
import binary404.autotech.common.tile.core.TileCore;
import binary404.autotech.common.tile.device.TileWaterPump;
import binary404.autotech.common.tile.generator.TileSteamGenerator;
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

public class BlockWaterPump extends BlockTile {

    public BlockWaterPump(Properties properties) {
        super(properties);
        setDefaultState();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileWaterPump) {
            TileWaterPump steamGenerator = (TileWaterPump) tile;
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
        return new ContainerWaterPump(id, inventory, (TileWaterPump) te);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileWaterPump();
    }
}
