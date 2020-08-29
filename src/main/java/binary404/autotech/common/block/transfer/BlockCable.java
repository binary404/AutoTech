package binary404.autotech.common.block.transfer;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.Energy;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.transfer.TileCable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SixWayBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;

public class BlockCable extends BlockTile implements IWaterLoggable {

    public static final BooleanProperty NORTH = SixWayBlock.NORTH;
    public static final BooleanProperty EAST = SixWayBlock.EAST;
    public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
    public static final BooleanProperty WEST = SixWayBlock.WEST;
    public static final BooleanProperty UP = SixWayBlock.UP;
    public static final BooleanProperty DOWN = SixWayBlock.DOWN;
    public static final BooleanProperty TILE = BooleanProperty.create("tile");
    private static final VoxelShape CABLE = makeCuboidShape(6.25, 6.25, 6.25, 9.75, 9.75, 9.75);
    private static final VoxelShape[] MULTIPART = new VoxelShape[]{makeCuboidShape(6.5, 6.5, 0, 9.5, 9.5, 7), makeCuboidShape(9.5, 6.5, 6.5, 16, 9.5, 9.5), makeCuboidShape(6.5, 6.5, 9.5, 9.5, 9.5, 16), makeCuboidShape(0, 6.5, 6.5, 6.5, 9.5, 9.5), makeCuboidShape(6.5, 9.5, 6.5, 9.5, 16, 9.5), makeCuboidShape(6.5, 0, 6.5, 9.5, 7, 9.5)};

    Tier tier;

    public BlockCable(Properties properties, Tier tier) {
        super(properties);
        setStateProps(state -> state.with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(UP, false).with(DOWN, false).with(TILE, false));
        this.tier = tier;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileCable(this.tier);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(TILE);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        VoxelShape voxelShape = CABLE;
        if (state.get(NORTH) || canConnectEnergy(world, pos, Direction.NORTH))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[0]);
        if (state.get(EAST) || canConnectEnergy(world, pos, Direction.EAST))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[1]);
        if (state.get(SOUTH) || canConnectEnergy(world, pos, Direction.SOUTH))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[2]);
        if (state.get(WEST) || canConnectEnergy(world, pos, Direction.WEST))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[3]);
        if (state.get(UP) || canConnectEnergy(world, pos, Direction.UP))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[4]);
        if (state.get(DOWN) || canConnectEnergy(world, pos, Direction.DOWN))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[5]);
        return voxelShape;
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
            cable.sync();
        }

        return createCableState(world, currentPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return createCableState(context.getWorld(), context.getPos());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        return ActionResultType.PASS;
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
        FluidState ifluidstate = world.getFluidState(pos);
        return state.with(NORTH, north[0]).with(SOUTH, south[0]).with(WEST, west[0]).with(EAST, east[0]).with(UP, up[0]).with(DOWN, down[0]).with(TILE, tile).with(BlockStateProperties.WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
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
            cable.sync();
        }
        super.onBlockAdded(state, world, pos, oldState, isMoving);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        findCables(world, pos, pos);
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    public boolean[] canAttach(BlockState state, IWorld world, BlockPos pos, Direction direction) {
        return new boolean[]{world.getBlockState(pos.offset(direction)).getBlock() == this || canConnectEnergy(world, pos, direction), canConnectEnergy(world, pos, direction)};
    }

    public boolean canConnectEnergy(IBlockReader world, BlockPos pos, Direction direction) {
        TileEntity tile = world.getTileEntity(pos.offset(direction));
        return !(tile instanceof TileCable) && Energy.isPresent(tile, direction);
    }

    static final Map<BlockPos, Set<BlockPos>> CACHE = new HashMap<>();

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
                    BlockCable cableBlock = (BlockCable) state.getBlock();
                    first.proxyMap.get(side).searchCache.add(pos);
                    cableBlock.searchCables(world, blockPos, first, side);
                }
            }
        }
    }

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
                    BlockCable cableBlock = (BlockCable) state.getBlock();
                    ss.add(pos);
                    CACHE.put(poss, ss);
                    cableBlock.findCables(world, poss, blockPos);
                }
            }
        }
        CACHE.clear();
    }

    public static Optional<Direction> getHitSide(Vector3d hit, BlockPos pos) {
        double x = hit.x - pos.getX();
        double y = hit.y - pos.getY();
        double z = hit.z - pos.getZ();
        if (x > 0.0D && x < 0.4D) return Optional.of(Direction.WEST);
        else if (x > 0.6D && x < 1.0D) return Optional.of(Direction.EAST);
        else if (z > 0.0D && z < 0.4D) return Optional.of(Direction.NORTH);
        else if (z > 0.6D && z < 1.0D) return Optional.of(Direction.SOUTH);
        else if (y > 0.6D && y < 1.0D) return Optional.of(Direction.UP);
        else if (y > 0.0D && y < 0.4D) return Optional.of(Direction.DOWN);
        return Optional.empty();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, TILE);
        super.fillStateContainer(builder);
    }
}
