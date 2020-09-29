package binary404.autotech.common.tile.transfer;

import binary404.autotech.common.block.transfer.BlockConveyor;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileCore;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.ArrayList;
import java.util.List;

import static binary404.autotech.common.block.transfer.BlockConveyor.FACING;
import static binary404.autotech.common.block.transfer.BlockConveyor.TYPE;

public class TileConveyor extends TileCore implements ITickableTileEntity {

    private Direction facing;
    private BlockConveyor.EnumType type;
    private boolean sticky;
    private List<ItemEntity> items;

    public TileConveyor() {
        super(ModTiles.conveyor);
        this.facing = Direction.NORTH;
        this.type = BlockConveyor.EnumType.FLAT;
        this.tank.setCapacity(250);
        this.sticky = false;
        this.inv.set(0);
        this.items = new ArrayList<>();
    }

    public Direction getFacing() {
        return facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public BlockConveyor.EnumType getConveyorType() {
        return type;
    }

    public void setType(BlockConveyor.EnumType type) {
        this.type = type;
    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
        sync();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound = super.write(compound);
        compound.putString("Facing", facing.getString());
        compound.putString("Type", type.getName());
        compound.putBoolean("Sticky", sticky);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.facing = Direction.byName(compound.getString("Facing"));
        this.type = BlockConveyor.EnumType.getFromName(compound.getString("Type"));
        this.sticky = compound.getBoolean("Sticky");
    }

    @Override
    public void sync() {
        super.sync();
        this.markDirty();
        this.world.setBlockState(pos, this.world.getBlockState(pos).with(FACING, facing).with(TYPE, type));
        this.world.getTileEntity(pos).read(this.world.getBlockState(pos), write(new CompoundNBT()));
    }

    public static VoxelShape NORTHBB_BIG = VoxelShapes.create(0.0625 * 2, 0, -0.0625 * 2, 0.0625 * 14, 0.0625 * 9, 0.0625 * 14);
    public static VoxelShape SOUTHBB_BIG = VoxelShapes.create(0.0625 * 2, 0, 0.0625 * 2, 0.0625 * 14, 0.0625 * 9, 0.0625 * 18);
    public static VoxelShape EASTBB_BIG = VoxelShapes.create(0.0625 * 2, 0, 0.0625 * 2, 0.0625 * 18, 0.0625 * 9, 0.0625 * 14);
    public static VoxelShape WESTBB_BIG = VoxelShapes.create(-0.0625 * 2, 0, 0.0625 * 2, 0.0625 * 14, 0.0625 * 9, 0.0625 * 14);

    public void handleEntityMovement(Entity entity) {
        if (entity.isAlive()) {
            if (entity instanceof ItemEntity) {
                ((ItemEntity) entity).age = 0;
                getHandlerCapabilityInsert(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                    if (getWorkingBox().getBoundingBox().offset(getPos()).grow(0.01).intersects(entity.getBoundingBox())) {
                        ItemStack stack = ((ItemEntity) entity).getItem();
                        for (int i = 0; i < handler.getSlots(); i++) {
                            stack = handler.insertItem(i, stack, false);
                            if (stack.isEmpty()) {
                                entity.remove();
                                break;
                            } else {
                                ((ItemEntity) entity).setItem(stack);
                            }
                        }
                    }
                });
            }
            handleConveyorMovement(entity, facing, this.pos, type);
            if (entity instanceof ItemEntity && sticky)
                ((ItemEntity) entity).setPickupDelay(5);
        }
    }

    private VoxelShape getWorkingBox() {
        switch (facing) {
            default:
            case NORTH:
                return NORTHBB_BIG;
            case SOUTH:
                return SOUTHBB_BIG;
            case EAST:
                return EASTBB_BIG;
            case WEST:
                return WESTBB_BIG;
        }
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            items.removeIf(ItemEntity -> ItemEntity.getItem().isEmpty() || !ItemEntity.isAlive());
            if (items.size() >= 20)
                return;

            if (world.getGameTime() % 2 == 0) {
                IFluidTank tank = this.getTank();
                getHandlerCapabilityInsert(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(fluidHandler -> {
                    if (!tank.drain(50, IFluidHandler.FluidAction.SIMULATE).isEmpty() && fluidHandler.fill(tank.drain(50, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0) {
                        FluidStack drain = tank.drain(fluidHandler.fill(tank.drain(50, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                    }
                });
            }

            if (world.getGameTime() % 20 == 0) {
                getHandlerCapabilityExtract(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        ItemStack stack = itemHandler.extractItem(i, 4, true);
                        if (stack.isEmpty())
                            continue;
                        ItemEntity item = new ItemEntity(getWorld(), getPos().getX() + 0.5, getPos().getY() + 0.2, getPos().getZ() + 0.5);
                        item.setMotion(0, -1, 0);
                        item.setPickupDelay(40);
                        item.setItem(itemHandler.extractItem(i, 4, false));
                        if (world.addEntity(item))
                            items.add(item);
                        break;
                    }
                });
            }

            IFluidTank tank = this.getTank();
            getHandlerCapabilityExtract(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(fluidHandler -> {
                if (!fluidHandler.drain(250, IFluidHandler.FluidAction.SIMULATE).isEmpty()) {
                    FluidStack drain = fluidHandler.drain(tank.fill(fluidHandler.drain(250, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                }
            });

        }

        if (!world.isRemote && tank.getFluidAmount() > 0 && world.getGameTime() % 3 == 0 && world.getBlockState(this.pos.offset(facing)).getBlock() instanceof BlockConveyor && world.getTileEntity(this.pos.offset(facing)) instanceof TileConveyor) {
            BlockState state = world.getBlockState(this.pos.offset(facing));
            if (!state.get(TYPE).isVertical()) {
                int amount = Math.max(tank.getFluidAmount() - 1, 1);
                TileConveyor tile = (TileConveyor) world.getTileEntity(this.pos.offset(facing));
                FluidStack drained = tank.drain(tile.getTank().fill(tank.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                if (!drained.isEmpty() && drained.getAmount() > 0) {
                    sync();
                }
            }
        }
    }

    private <T> LazyOptional<T> getHandlerCapabilityExtract(Capability<T> capability) {
        BlockPos offsetPos = getPos().offset(facing.getOpposite());
        TileEntity tile = getWorld().getTileEntity(offsetPos);
        if (tile != null && tile.getCapability(capability, facing).isPresent())
            return tile.getCapability(capability, facing);
        return LazyOptional.empty();
    }

    private <T> LazyOptional<T> getHandlerCapabilityInsert(Capability<T> capability) {
        BlockPos offsetPos = getPos().offset(facing);
        TileEntity tile = getWorld().getTileEntity(offsetPos);
        if (tile != null && tile.getCapability(capability, facing.getOpposite()).isPresent())
            return tile.getCapability(capability, facing.getOpposite());
        return LazyOptional.empty();
    }

    public static void handleConveyorMovement(Entity entity, Direction direction, BlockPos blockPos, BlockConveyor.EnumType type) {
        if (entity instanceof PlayerEntity && entity.isCrouching()) return;
        if (entity.getPosition().getY() - blockPos.getY() > 0.3 && !type.isVertical()) return;

        VoxelShape collision = entity.world.getBlockState(blockPos).getBlock().getCollisionShape(entity.world.getBlockState(blockPos), entity.world, blockPos, ISelectionContext.dummy()).withOffset(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            //collision = collision.withOffset(-0.1, 0, 0);
            //collision = collision.withOffset(0.1, 0, 0);
        }
        if (direction == Direction.EAST || direction == Direction.WEST) {
            //collision = collision.withOffset(0, 0, -0.1);
            //collision = collision.withOffset(0, 0, 0.1);
        }
        if (!type.isVertical() && collision.toBoundingBoxList().stream().noneMatch(axisAlignedBB -> axisAlignedBB.grow(0.01).intersects(entity.getBoundingBox())))
            return;
        //DIRECTION MOVEMENT
        double speed = 0.2;
        if (type.isFast()) speed *= 2;
        Vector3d vec3d = new Vector3d(speed * direction.getDirectionVec().getX(), speed * direction.getDirectionVec().getY(), speed * direction.getDirectionVec().getZ());
        if (type.isVertical()) {
            vec3d = vec3d.add(0, type.isUp() ? 0.258 : -0.05, 0);
            entity.onGround = false;
        }

        //CENTER
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            if (entity.getPosX() - blockPos.getX() < 0.45) {
                vec3d = vec3d.add(0.08, 0, 0);
            }
            if (entity.getPosX() - blockPos.getX() > 0.55) {
                vec3d = vec3d.add(-0.08, 0, 0);
            }
        }
        if (direction == Direction.EAST || direction == Direction.WEST) {
            if (entity.getPosZ() - blockPos.getZ() < 0.45) {
                vec3d = vec3d.add(0, 0, 0.08);
            }
            if (entity.getPosZ() - blockPos.getZ() > 0.55) {
                vec3d = vec3d.add(0, 0, -0.08);
            }
        }
        entity.setMotion(vec3d.x, vec3d.y != 0 ? vec3d.y : entity.getMotion().y, vec3d.z);
    }
}
