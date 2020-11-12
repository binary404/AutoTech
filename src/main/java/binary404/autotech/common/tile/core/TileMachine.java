package binary404.autotech.common.tile.core;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.util.IInventory;
import binary404.autotech.common.tile.util.IRedstoneInteract;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;

public class TileMachine<T extends BlockTile> extends TileTiered<T> implements IInventory, IRedstoneInteract {

    public int processMax;
    public int processRem;
    protected boolean isActive;

    public TileMachine(TileEntityType<?> type, Tier tier) {
        super(type, tier);
    }

    @Override
    protected int postTick(World world) {
        if (isActive && checkRedstone()) {
            processTick();

            if (!hasValidInput())
                processOff();

            if (canFinish()) {
                processFinish();
                transferOutput();
                transferInput();
                if (!canStart()) {
                    processOff();
                } else {
                    processStart();
                }
                sync(4);
            } else if (energy.getEnergyStored() <= 0) {
                processOff();
            }
        } else {
            transferOutput();
            transferInput();
            if (canStart()) {
                processStart();
                processTick();
                isActive = true;
                sync(4);
            }
        }

        if (!checkRedstone())
            processOff();

        return super.postTick(world);
    }

    protected void transferInput() {

    }

    protected void transferOutput() {

    }

    protected boolean canStart() {
        return false;
    }

    protected void processStart() {

    }

    protected boolean canFinish() {
        return processRem <= 0 && hasValidInput();
    }

    protected boolean hasValidInput() {
        return true;
    }

    protected void processFinish() {

    }

    protected void processOff() {
        processRem = 0;
        isActive = false;
        clearRecipe();
    }

    protected void clearRecipe() {

    }

    @Override
    public void markDirty() {
        if (isActive && !hasValidInput())
            processOff();
        super.markDirty();
    }

    public int getScaledProgress() {
        if (!isActive || processMax <= 0 || processRem <= 0)
            return 0;
        return 24 * (processMax - processRem) / processMax;
    }

    public float getScaledProgressH() {
        if (!isActive || processRem <= 0 || processMax <= 0) {
            return 0;
        }
        return (float) processRem / processMax;
    }

    protected int processTick() {
        if (processRem <= 0) {
            return 0;
        }
        this.energy.consume(this.tier.use);
        processRem -= this.tier.use;
        return this.tier.use;
    }

    @Override
    public void readSync(CompoundNBT nbt) {
        this.processMax = nbt.getInt("max");
        this.processRem = nbt.getInt("rem");
        this.isActive = nbt.getBoolean("active");
        super.readSync(nbt);
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        nbt.putInt("max", processMax);
        nbt.putInt("rem", processRem);
        nbt.putBoolean("active", isActive);
        return super.writeSync(nbt);
    }

    @Override
    public long getGeneration() {
        return 0L;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    public boolean extractItem(int slot, int amount, Direction side) {

        if (slot > inv.getSlots()) {
            return false;
        }
        ItemStack stack = inv.getStackInSlot(slot);

        if (!stack.isEmpty()) {
            amount = Math.min(amount, stack.getMaxStackSize() - stack.getCount());
            stack = inv.getStackInSlot(slot).copy();
        }
        int initialAmount = amount;
        TileEntity adjInv = getAdjacentTileEntity(this, side);

        if (isAccessibleInput(adjInv, side)) {
            IItemHandler inventory = getItemHandlerCap(adjInv, side.getOpposite()).orElse(new ItemStackHandler(0));
            if (inventory == null) {
                return false;
            }
            for (int i = 0; i < inventory.getSlots() && amount > 0; i++) {
                ItemStack queryStack = inventory.extractItem(i, amount, true);
                if (queryStack.isEmpty()) {
                    continue;
                }
                if (stack.isEmpty()) {
                    if (canInsert(slot, queryStack)) {
                        int toExtract = Math.min(amount, queryStack.getCount());
                        stack = inventory.extractItem(i, toExtract, false);
                        amount -= toExtract;
                    }
                } else if (stack.getItem() == queryStack.getItem()) {
                    int toExtract = Math.min(stack.getMaxStackSize() - stack.getCount(), Math.min(amount, queryStack.getCount()));
                    ItemStack extracted = inventory.extractItem(i, toExtract, false);
                    toExtract = Math.min(toExtract, extracted.isEmpty() ? 0 : extracted.getCount());
                    stack.grow(toExtract);
                    amount -= toExtract;
                }
            }
            if (initialAmount != amount) {
                inv.setStack(slot, stack);
                adjInv.markDirty();
                return true;
            }
        }
        return false;
    }

    public boolean transferItem(int slot, int amount, Direction side) {

        if (inv.getStackInSlot(slot).isEmpty() || slot > inv.getSlots()) {
            return false;
        }
        ItemStack initialStack = inv.getStackInSlot(slot).copy();
        initialStack.setCount(Math.min(amount, initialStack.getCount()));
        TileEntity adjInv = getAdjacentTileEntity(this, side);

        if (isAccessibleOutput(adjInv, side)) {
            ItemStack inserted = addToInventory(adjInv, side, initialStack);

            if (inserted.getCount() >= initialStack.getCount()) {
                return false;
            }
            inv.getStackInSlot(slot).shrink(initialStack.getCount() - inserted.getCount());
            if (inv.getStackInSlot(slot).getCount() <= 0) {
                inv.setStack(slot, ItemStack.EMPTY);
            }
            return true;
        }
        return false;
    }

    public static TileEntity getAdjacentTileEntity(World world, BlockPos pos, Direction dir) {

        pos = pos.offset(dir);
        return world == null || !world.isBlockLoaded(pos) ? null : world.getTileEntity(pos);
    }

    public static TileEntity getAdjacentTileEntity(TileEntity refTile, Direction dir) {

        return refTile == null ? null : getAdjacentTileEntity(refTile.getWorld(), refTile.getPos(), dir);
    }

    public static boolean isAccessibleInput(TileEntity tile, Direction side) {

        return getItemHandlerCap(tile, side.getOpposite()).orElse(new ItemStackHandler(0)).getSlots() > 0;
    }

    public static boolean isAccessibleOutput(TileEntity tile, Direction side) {

        return getItemHandlerCap(tile, side.getOpposite()).orElse(new ItemStackHandler(0)).getSlots() > 0;
    }

    public static LazyOptional<IItemHandler> getItemHandlerCap(TileEntity tileEntity, Direction face) {
        return tileEntity == null ? LazyOptional.empty() : tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face);
    }

    public static ItemStack addToInventory(TileEntity tile, Direction side, ItemStack stack) {

        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        IItemHandler handler = getItemHandlerCap(tile, side.getOpposite()).orElse(new ItemStackHandler(0));

        stack = insertStackIntoInventory(handler, stack, false);

        return stack;
    }

    public static ItemStack insertStackIntoInventory(IItemHandler handler, ItemStack stack, boolean simulate) {

        return insertStackIntoInventory(handler, stack, simulate, false);
    }

    public static ItemStack insertStackIntoInventory(IItemHandler handler, ItemStack stack, boolean simulate, boolean forceEmptySlot) {

        return forceEmptySlot ? ItemHandlerHelper.insertItem(handler, stack, simulate) : ItemHandlerHelper.insertItemStacked(handler, stack, simulate);
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }
}
