package binary404.autotech.common.block.transfer;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.logistics.energy.Energy;
import binary404.autotech.common.tile.transfer.cable.TileCable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

public class CableBlock extends SixWayBlock {

    public static List<CableBlock> cables = new ArrayList<>();

    public static final BooleanProperty TILE = BooleanProperty.create("tile");

    Tier tier;

    public CableBlock(Properties properties, Tier tier, float width) {
        super(width, properties);
        this.tier = tier;
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(UP, false)
                .with(DOWN, false).with(TILE, false));
        cables.add(this);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(TILE);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileCable(tier);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, TILE);
    }

    private BlockState createCableState(IWorld world, BlockPos pos) {
        final BlockState state = getDefaultState();
        boolean[] north = canAttach(state, world, pos, Direction.NORTH);
        boolean[] south = canAttach(state, world, pos, Direction.SOUTH);
        boolean[] west = canAttach(state, world, pos, Direction.WEST);
        boolean[] east = canAttach(state, world, pos, Direction.EAST);
        boolean[] up = canAttach(state, world, pos, Direction.UP);
        boolean[] down = canAttach(state, world, pos, Direction.DOWN);
        boolean tile = false;
        if (north[1] || south[1] || west[1] || east[1] || up[1] || down[1]) {
            tile = true;
        } else {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileCable) {
                TileCable cable = (TileCable) tileEntity;
                cable.remove();
            }
        }
        return state.with(NORTH, north[0]).with(SOUTH, south[0]).with(WEST, west[0]).with(EAST, east[0]).with(UP, up[0]).with(DOWN, down[0]).with(TILE, tile);
    }

    public boolean[] canAttach(BlockState state, IWorld world, BlockPos pos, Direction direction) {
        return new boolean[]{world.getBlockState(pos.offset(direction)).getBlock() == this || canConnectEnergy(world, pos, direction), canConnectEnergy(world, pos, direction)};
    }

    public boolean canConnectEnergy(IBlockReader world, BlockPos pos, Direction direction) {
        TileEntity tile = world.getTileEntity(pos.offset(direction));
        return !(tile instanceof TileCable) && Energy.isPresent(tile, direction);
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        TileEntity tileEntity = world.getTileEntity(currentPos);
        if (tileEntity instanceof TileCable) {
            TileCable cable = (TileCable) tileEntity;
            cable.energySides.clear();
            for (Direction direction : Direction.values()) {
                if (canConnectEnergy(world, currentPos, direction)) {
                    cable.energySides.add(direction);
                }
            }
        }

        return createCableState(world, currentPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return createCableState(context.getWorld(), context.getPos());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (world.isRemote) return;
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileCable) {
            TileCable cable = (TileCable) tileEntity;
            for (Direction direction : Direction.values()) {
                if (cable.canExtractEnergy(direction)) {
                    cable.search(this, direction);
                }
            }
        } else {
            findCables(world, pos, pos);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileCable) {
            TileCable cable = (TileCable) tileEntity;
            cable.energySides.clear();
            for (Direction direction : Direction.values()) {
                if (canConnectEnergy(world, pos, direction)) {
                    cable.energySides.add(direction);
                }
            }
        }
        super.onBlockAdded(state, world, pos, oldState, isMoving);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        findCables(world, pos, pos);
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    protected int getShapeIndex(BlockState state) {
        int i = 0;

        for (int j = 0; j < Direction.values().length; ++j) {
            if (state.get(FACING_TO_PROPERTY_MAP.get(Direction.values()[j]))) {
                i |= 1 << j;
            }
        }

        return i;
    }

    public void searchCables(IWorld world, BlockPos pos, TileCable first, Direction side) {
        if (!first.proxyMap.get(side).searchCache.contains(pos)) {
            for (Direction direction : Direction.values()) {
                BlockPos blockPos = pos.offset(direction);
                if (blockPos.equals(first.getPos())) continue;
                BlockState state = world.getBlockState(blockPos);
                if (state.getBlock() == this) {
                    TileEntity tileEntity = world.getTileEntity(blockPos);
                    if (tileEntity instanceof TileCable) {
                        TileCable cable = (TileCable) tileEntity;
                        first.proxyMap.get(side).add(blockPos);
                    }
                    CableBlock cableBlock = (CableBlock) state.getBlock();
                    first.proxyMap.get(side).searchCache.add(pos);
                    cableBlock.searchCables(world, blockPos, first, side);
                }
            }
        }
    }

    static final Map<BlockPos, Set<BlockPos>> CACHE = new HashMap<>();

    public void findCables(IWorld world, BlockPos poss, BlockPos pos) {
        Set<BlockPos> ss = CACHE.get(poss);
        if (ss == null) {
            ss = new HashSet<>();
        }
        if (!ss.contains(pos)) {
            for (Direction direction : Direction.values()) {
                BlockPos blockPos = pos.offset(direction);
                BlockState state = world.getBlockState(blockPos);
                if (state.getBlock() == this) {
                    TileEntity tileEntity = world.getTileEntity(blockPos);
                    if (tileEntity instanceof TileCable) {
                        TileCable cable = (TileCable) tileEntity;
                        for (Direction side : Direction.values()) {
                            cable.proxyMap.get(side).cables().clear();
                            cable.search(this, side);
                        }
                    }
                    CableBlock cableBlock = (CableBlock) state.getBlock();
                    ss.add(pos);
                    CACHE.put(poss, ss);
                    cableBlock.findCables(world, poss, blockPos);
                }
            }
        }
        CACHE.clear();
    }

}
