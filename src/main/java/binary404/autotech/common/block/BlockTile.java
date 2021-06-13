package binary404.autotech.common.block;

import binary404.autotech.common.tile.util.IBlockEntity;
import codechicken.lib.CodeChickenLib;
import codechicken.lib.render.block.BlockRenderingRegistry;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import static net.minecraft.state.properties.BlockStateProperties.*;

import javax.annotation.Nullable;

public class BlockTile extends Block {

    public BlockTile() {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(2.0F, 10.0F).notSolid());
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IBlockEntity) {
            ((IBlockEntity) tile).onAdded(world, state, oldState, isMoving);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IBlockEntity) {
            ((IBlockEntity) tile).onRemoved(world, state, newState, isMoving);
        }
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IBlockEntity) {
            ((IBlockEntity) tile).onPlaced(world, state, placer, stack);
        }
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        if (this instanceof IWaterLoggable && state.get(WATERLOGGED))
            world.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        if (!state.isValidPosition(world, currentPos)) {
            TileEntity tileEntity = world.getTileEntity(currentPos);
            //DoStuff
        }
        return super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IBlockEntity) {
            if (((IBlockEntity) tile).onRightClick(player, hand, result.getFace(), result)) ;
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    protected boolean isPlacerFacing() {
        return false;
    }

    protected Facing getFacing() {
        return Facing.HORIZONTAL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = getDefaultState();
        if (getFacing().equals(Facing.HORIZONTAL)) {
            if (!isPlacerFacing()) {
                state = facing(context, false);
            } else {
                state = getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
            }
        } else if (getFacing().equals(Facing.ALL)) {
            if (!isPlacerFacing()) {
                state = facing(context, true);
            } else {
                state = getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
            }
        }
        if (state != null && this instanceof IWaterLoggable) {
            FluidState fluidState = context.getWorld().getFluidState(context.getPos());
            state = state.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
        }
        return state;
    }

    @Nullable
    private BlockState facing(BlockItemUseContext context, boolean b) {
        BlockState blockstate = this.getDefaultState();
        for (Direction direction : context.getNearestLookingDirections()) {
            if (b || direction.getAxis().isHorizontal()) {
                blockstate = blockstate.with(FACING, b ? direction : direction.getOpposite());
                if (blockstate.isValidPosition(context.getWorld(), context.getPos())) {
                    return blockstate;
                }
            }
        }
        return null;
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        if (!getFacing().equals(Facing.NONE)) {
            for (Rotation rotation : Rotation.values()) {
                if (!rotation.equals(Rotation.NONE)) {
                    if (isValidPosition(super.rotate(state, world, pos, rotation), world, pos)) {
                        return super.rotate(state, world, pos, rotation);
                    }
                }
            }
        }
        return state;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        if (getFacing().equals(Facing.ALL) || getFacing().equals(Facing.HORIZONTAL)) {
            return state.with(FACING, rot.rotate(state.get(FACING)));
        }
        return super.rotate(state, rot);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        if (getFacing().equals(Facing.ALL) || getFacing().equals(Facing.HORIZONTAL)) {
            return state.rotate(mirror.toRotation(state.get(FACING)));
        }
        return super.mirror(state, mirror);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return this instanceof IWaterLoggable && state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean eventReceived(BlockState state, World world, BlockPos pos, int id, int param) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity != null && tileEntity.receiveClientEvent(id, param);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        if (getFacing().equals(Facing.ALL) || getFacing().equals(Facing.HORIZONTAL)) builder.add(FACING);
        if (this instanceof IWaterLoggable) builder.add(WATERLOGGED);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    protected enum Facing {
        HORIZONTAL, ALL, NONE
    }

}
