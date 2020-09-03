package binary404.autotech.common.tile.core;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.*;
import binary404.autotech.common.core.util.NBTUtil;
import binary404.autotech.common.core.util.StackUtil;
import binary404.autotech.common.tile.util.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class TileCore<B extends BlockTile> extends TileEntity implements IBlockEntity {

    protected byte facing = 3;
    public byte[] sideCache = {0, 0, 0, 0, 0, 0};

    /**
     * Used when this is instance of {@link IInventory}
     **/
    protected final Inventory inv = Inventory.createBlank();
    private final LazyOptional<Inventory> invHolder = LazyOptional.of(() -> this.inv);
    /**
     * Used when this is instance of {@link ITank}
     **/

    public SideConfigItem itemConfig = new SideConfigItem(this);

    protected Tank tank = new Tank(0);
    private final LazyOptional<FluidTank> tankHolder = LazyOptional.of(() -> this.tank);

    protected boolean isContainerOpen;
    /**
     * Used when this is instance of {@link IRedstoneInteract}
     **/
    private Redstone redstone = Redstone.IGNORE;

    public TileCore(TileEntityType<?> type) {
        super(type);
        if (this instanceof IInventory) {
            this.inv.setTile(this);
        }
    }

    public B getBlock() {
        return (B) getBlockState().getBlock();
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);

        this.facing = compound.getByte("facing_direction");
        this.sideCache = compound.getByteArray("facing_cache");

        readSync(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt = super.write(compound);
        nbt.putByte("facing_direction", facing);
        nbt.putByteArray("facing_cache", sideCache);
        return writeSync(nbt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), 3, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        readSync(pkt.getNbtCompound());
    }

    protected void readSync(CompoundNBT nbt) {
        if (this instanceof IInventory && !keepInventory()) {
            this.inv.deserializeNBT(nbt);
        }
        if (this instanceof ITank && !keepFluid()) {
            this.tank.readFromNBT(nbt);
        }
        this.redstone = Redstone.values()[nbt.getInt("redstone_mode")];
        readStorable(nbt);
    }

    protected CompoundNBT writeSync(CompoundNBT nbt) {
        this.itemConfig.write(nbt);
        if (this instanceof IInventory && !keepInventory()) {
            nbt.merge(this.inv.serializeNBT());
        }
        if (this instanceof ITank && !keepFluid()) {
            this.tank.writeToNBT(nbt);
        }
        nbt.putInt("redstone_mode", this.redstone.ordinal());
        return writeStorable(nbt);
    }

    public void readStorable(CompoundNBT nbt) {
        this.itemConfig.read(nbt);
        if (this instanceof IInventory && keepInventory()) {
            this.inv.deserializeNBT(nbt);
        }
        if (this instanceof ITank && keepFluid()) {
            this.tank.readFromNBT(nbt);
        }
    }

    public CompoundNBT writeStorable(CompoundNBT nbt) {
        if (this instanceof IInventory && keepInventory()) {
            nbt.merge(this.inv.serializeNBT());
        }
        if (this instanceof ITank && keepFluid()) {
            this.tank.writeToNBT(nbt);
        }

        return nbt;
    }

    @Override
    public void onPlaced(World world, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        CompoundNBT tag = StackUtil.getTagOrEmpty(stack);
        if (!tag.isEmpty()) {
            readStorable(tag.getCompound(NBTUtil.TAG_STORABLE_STACK));
        }
    }

    @Override
    public void onRemoved(World world, BlockState state, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (this instanceof IInventory) {
                if (!keepInventory() || !keepStorable()) {
                    getInventory().drop(world, this.pos);
                }
            }
        }
    }

    public ItemStack storeToStack(ItemStack stack) {
        CompoundNBT nbt = writeStorable(new CompoundNBT());
        CompoundNBT nbt1 = StackUtil.getTagOrEmpty(stack);
        if (!nbt.isEmpty() && keepStorable()) {
            nbt1.put(NBTUtil.TAG_STORABLE_STACK, nbt);
            stack.setTag(nbt1);
        }
        return stack;
    }

    public boolean keepStorable() {
        return true;
    }

    private boolean keepInventory() {
        return false;
    }

    protected boolean keepFluid() {
        return false;
    }

    public Tank getTank() {
        return this.tank;
    }

    public Redstone getRedstoneMode() {
        return this.redstone;
    }

    public void setRedstoneMode(Redstone mode) {
        this.redstone = mode;
    }

    public boolean checkRedstone() {
        boolean power = this.world != null && this.world.getRedstonePowerFromNeighbors(this.pos) > 0;
        return Redstone.IGNORE.equals(getRedstoneMode()) || power && Redstone.ON.equals(getRedstoneMode()) || !power && Redstone.OFF.equals(getRedstoneMode());
    }

    public void sync() {
        if (this.world instanceof ServerWorld) {
            final BlockState state = getBlockState();
            this.world.notifyBlockUpdate(this.pos, state, state, 3);
            this.world.markChunkDirty(this.pos, this);
        }
    }

    public boolean isRemote() {
        return this.world != null && this.world.isRemote;
    }

    public boolean canExtract(int slot) {
        return true;
    }

    public boolean canInsert(int slot) {
        return true;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (this instanceof IInventory && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && !this.inv.isBlank()) {
            if (this.itemConfig.getType(side).canExtract && !this.itemConfig.getType(side).canReceive) {
                return LazyOptional.of(() -> new OutputInventoryWrapper(this.inv, this)).cast();
            } else if (this.itemConfig.getType(side).canReceive && !this.itemConfig.getType(side).canExtract) {
                return LazyOptional.of(() -> new InputInventoryWrapper(this.inv, this)).cast();
            } else if (this.itemConfig.getType(side).canExtract && this.itemConfig.getType(side).canReceive) {
                return this.invHolder.cast();
            }
        }
        if (this instanceof ITank && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return this.tankHolder.cast();
        }
        return super.getCapability(cap, side);
    }

    public void setContainerOpen(boolean value) {
        final boolean b = this.isContainerOpen;
        this.isContainerOpen = value;
        if (b != value) {
            sync();
        }
    }

    public Inventory getInventory() {
        return this.inv;
    }

    public TransferType getTransferType() {
        return TransferType.ALL;
    }

    public SideConfigItem getItemConfig() {
        return this.itemConfig;
    }

    public boolean setFacing(int side, boolean alternative) {
        if (side < 0 || side > 5)
            return false;
        facing = (byte) side;
        return true;
    }


}
