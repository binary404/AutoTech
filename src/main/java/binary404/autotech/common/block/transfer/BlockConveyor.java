package binary404.autotech.common.block.transfer;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.tile.transfer.TileConveyor;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockConveyor extends Block {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final EnumProperty<EnumType> TYPE = EnumProperty.create("type", EnumType.class);

    public BlockConveyor() {
        super(AbstractBlock.Properties.create(Material.ROCK).harvestLevel(2).harvestTool(ToolType.PICKAXE).hardnessAndResistance(3.0f, 6.0f).doesNotBlockMovement());
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    @Override
    public BlockState getStateAtViewpoint(BlockState state, IBlockReader world, BlockPos pos, Vector3d viewpoint) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileConveyor) {
            state = state.with(FACING, ((TileConveyor) tileEntity).getFacing()).with(TYPE, ((TileConveyor) tileEntity).getConveyorType());
        }
        if (state.get(TYPE).equals(EnumType.FLAT) || state.get(TYPE).equals(EnumType.FLAT_FAST)) {
            Direction right = state.get(FACING).rotateY();
            Direction left = state.get(FACING).rotateYCCW();
            if (isConveyorAndFacing(pos.offset(right), world, left) && isConveyorAndFacing(pos.offset(left), world, right) || (isConveyorAndFacing(pos.offset(right).down(), world, left) && isConveyorAndFacing(pos.offset(left).down(), world, right))) {
                //state = state.with(SIDES, EnumSides.BOTH);
            } else if (isConveyorAndFacing(pos.offset(right), world, left) || isConveyorAndFacing(pos.offset(right).down(), world, left)) {
                //state = state.with(SIDES, EnumSides.RIGHT);
            } else if (isConveyorAndFacing(pos.offset(left), world, right) || isConveyorAndFacing(pos.offset(left).down(), world, right)) {
                //state = state.with(SIDES, EnumSides.LEFT);
            } else {
                //state = state.with(SIDES, EnumSides.NONE);
            }
        }
        return state;
    }


    private boolean isConveyorAndFacing(BlockPos pos, IBlockReader world, Direction toFace) {
        return world.getBlockState(pos).getBlock() instanceof BlockConveyor && (toFace == null || world.getBlockState(pos).get(FACING).equals(toFace));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING, TYPE, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileConveyor();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (placer != null) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileConveyor) {
                ((TileConveyor) tileEntity).setFacing(placer.getHorizontalFacing());
                ((TileConveyor) tileEntity).sync();
            }
            updateConveyorPlacing(worldIn, pos, state, true);
        }
    }

    private void updateConveyorPlacing(World worldIn, BlockPos pos, BlockState state, boolean first) {
        TileEntity entity = worldIn.getTileEntity(pos);
        if (entity instanceof TileConveyor) {
            Direction direction = ((TileConveyor) entity).getFacing();
            Direction right = state.get(FACING).rotateY();
            Direction left = state.get(FACING).rotateYCCW();
            if (isConveyorAndFacing(pos.up().offset(direction), worldIn, null)) {//SELF UP
                ((TileConveyor) entity).setType(((TileConveyor) entity).getConveyorType().getVertical(Direction.UP));
                ((TileConveyor) entity).sync();
            } else if (isConveyorAndFacing(pos.up().offset(direction.getOpposite()), worldIn, null)) { //SELF DOWN
                ((TileConveyor) entity).setType(((TileConveyor) entity).getConveyorType().getVertical(Direction.DOWN));
                ((TileConveyor) entity).sync();
            }

            //UPDATE SURROUNDINGS
            if (!first) return;
            if (isConveyorAndFacing(pos.offset(direction.getOpposite()).down(), worldIn, direction)) { //BACK DOWN
                updateConveyorPlacing(worldIn, pos.offset(direction.getOpposite()).down(), state, false);
            }
            if (isConveyorAndFacing(pos.offset(left).down(), worldIn, right)) { //LEFT DOWN
                updateConveyorPlacing(worldIn, pos.offset(left).down(), state, false);
            }
            if (isConveyorAndFacing(pos.offset(right).down(), worldIn, left)) { //RIGHT DOWN
                updateConveyorPlacing(worldIn, pos.offset(right).down(), state, false);
            }
            if (isConveyorAndFacing(pos.offset(direction).down(), worldIn, direction)) { //FRONT DOWN
                updateConveyorPlacing(worldIn, pos.offset(direction).down(), state, false);
            }
            worldIn.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        ItemStack handStack = player.getHeldItem(handIn);
        if (tileEntity instanceof TileConveyor) {
            if (handStack.getItem().equals(Items.GLOWSTONE_DUST) && !((TileConveyor) tileEntity).getConveyorType().isFast()) {
                ((TileConveyor) tileEntity).setType(((TileConveyor) tileEntity).getConveyorType().getFast());
                handStack.shrink(1);
                ((TileConveyor) tileEntity).sync();
                return ActionResultType.SUCCESS;
            } else if (handStack.getItem().equals(Items.SLIME_BALL) && !((TileConveyor) tileEntity).isSticky()) {
                ((TileConveyor) tileEntity).setSticky(true);
                handStack.shrink(1);
                ((TileConveyor) tileEntity).sync();
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        VoxelShape shape = VoxelShapes.empty();
        for (VoxelShape shapes : getBoundingBoxes(state, reader, pos)) {
            shape = VoxelShapes.combineAndSimplify(shape, shapes, IBooleanFunction.OR);
        }
        return shape;
    }

    public List<VoxelShape> getBoundingBoxes(BlockState state, IBlockReader source, BlockPos pos) {
        List<VoxelShape> boxes = new ArrayList<>();
        if (state.get(TYPE).isVertical()) {
            boxes.add(VoxelShapes.create(0, 0, 0, 1, 1 / 16D, 1));
        } else {
            boxes.add(VoxelShapes.create(0, 0, 0, 1, 1 / 16D, 1));
        }
        TileEntity entity = source.getTileEntity(pos);
        return boxes;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.get(TYPE).isVertical()) {
            return VoxelShapes.create(0, 0, 0, 1, 0.40, 1);
        } else {
            VoxelShape shape = VoxelShapes.create(0, 0, 0, 1, 1 / 16D, 1);
            return shape;
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState state = context.getWorld().getFluidState(context.getPos());
        return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(WATERLOGGED, Boolean.valueOf(state.getFluid() == Fluids.WATER));
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityCollision(state, worldIn, pos, entityIn);
        TileEntity entity = worldIn.getTileEntity(pos);
        if (entity instanceof TileConveyor) {
            ((TileConveyor) entity).handleEntityMovement(entityIn);
        }
    }

    public enum EnumType implements IStringSerializable {

        FLAT(false, "autotech:block/conveyor"),
        UP(false, "autotech:block/conveyor_ramp_inverted", "autotech:blocks/conveyor_color_inverted"),
        DOWN(false, "autotech:block/conveyor_ramp"),
        FLAT_FAST(true, "autotech:block/conveyor", "autotech:blocks/conveyor_color_fast"),
        UP_FAST(true, "autotech:block/conveyor_ramp_inverted", "autotech:blocks/conveyor_color_inverted_fast"),
        DOWN_FAST(true, "autotech:block/conveyor_ramp", "autotech:blocks/conveyor_color_fast");

        private boolean fast;
        private String model;
        private String texture;

        EnumType(boolean fast, String model) {
            this(false, model, "autotech:blocks/conveyor_color");
        }

        EnumType(boolean fast, String model, String texture) {
            this.fast = fast;
            this.model = model;
            this.texture = texture;
        }

        public static EnumType getFromName(String name) {
            for (EnumType type : EnumType.values()) {
                if (type.getName().equalsIgnoreCase(name)) {
                    return type;
                }
            }
            return FLAT;
        }

        public boolean isFast() {
            return fast;
        }

        public EnumType getFast() {
            switch (this) {
                case FLAT:
                    return FLAT_FAST;
                case UP:
                    return UP_FAST;
                case DOWN:
                    return DOWN_FAST;
                default:
                    return this;
            }
        }

        public EnumType getVertical(Direction facing) {
            if (this.isFast()) {
                if (facing == Direction.UP) {
                    return UP_FAST;
                }
                if (facing == Direction.DOWN) {
                    return DOWN_FAST;
                }
                return FLAT_FAST;
            } else {
                if (facing == Direction.UP) {
                    return UP;
                }
                if (facing == Direction.DOWN) {
                    return DOWN;
                }
                return FLAT_FAST;
            }
        }

        public boolean isVertical() {
            return isDown() || isUp();
        }

        public boolean isUp() {
            return this.equals(UP) || this.equals(UP_FAST);
        }

        public boolean isDown() {
            return this.equals(DOWN) || this.equals(DOWN_FAST);
        }

        public String getName() {
            return getString();
        }

        public String getModel() {
            return model;
        }

        public String getTexture() {
            return texture;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        @Override
        public String getString() { //getName
            return this.toString().toLowerCase();
        }
    }


}
