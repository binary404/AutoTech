package binary404.autotech.common.tile.core;

import binary404.autotech.client.gui.TileEntityUIFactory;
import binary404.autotech.client.gui.core.IUIHolder;
import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.core.logistics.*;
import binary404.autotech.common.core.logistics.fluid.FluidHandlerProxy;
import binary404.autotech.common.core.logistics.fluid.FluidTankList;
import binary404.autotech.common.core.logistics.item.*;
import binary404.autotech.common.core.recipe.core.TETrait;
import binary404.autotech.common.core.util.NBTUtil;
import binary404.autotech.common.core.util.StackUtil;
import binary404.autotech.common.core.util.Util;
import binary404.autotech.common.tile.util.*;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Cuboid6;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import net.minecraftforge.items.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public abstract class TileCore extends TileEntity implements IBlockEntity, IUIHolder, ITickableTileEntity {

    protected IItemHandlerModifiable importItems;
    protected IItemHandlerModifiable exportItems;

    protected IItemHandler itemInventory;

    protected FluidTankList importFluids;
    protected FluidTankList exportFluids;

    protected IFluidHandler fluidInventory;

    protected boolean newInventory = true;

    //Direction
    protected Direction facing = Direction.NORTH;

    private long timer = 0L;
    private final int offset = Util.getRandomIntXSTR(20);

    private Redstone redstone = Redstone.IGNORE;

    protected List<TETrait> teTraits = new ArrayList<>();

    public TileCore(TileEntityType<?> type) {
        super(type);
    }

    protected void initializeInventory() {
        if (this.newInventory) {
            this.importItems = createImportItemHandler();
            this.exportItems = createExportItemHandler();
            this.itemInventory = new ItemHandlerProxy(importItems, exportItems);

            this.importFluids = createImportFluidHandler();
            this.exportFluids = createExportFluidHandler();
            this.fluidInventory = new FluidHandlerProxy(importFluids, exportFluids);
        }
    }

    protected IItemHandlerModifiable createImportItemHandler() {
        return new ItemStackHandler(0);
    }

    protected IItemHandlerModifiable createExportItemHandler() {
        return new ItemStackHandler(0);
    }

    protected FluidTankList createImportFluidHandler() {
        return new FluidTankList(false);
    }

    protected FluidTankList createExportFluidHandler() {
        return new FluidTankList(false);
    }

    public void addTileEntityTrait(TETrait trait) {
        teTraits.removeIf(otherTrait -> {
            if (trait.getName().equals(otherTrait.getName())) {
                return true;
            }
            if (otherTrait.getNetworkID() == trait.getNetworkID()) {
                String message = "Trait %s is incompatible with trait %s as the both use the same network id %d";
                throw new IllegalArgumentException(String.format(message, trait, otherTrait, trait.getNetworkID()));
            }
            return false;
        });
        this.teTraits.add(trait);
    }

    public abstract ModularUserInterface createUI(PlayerEntity playerEntity);

    @Override
    public boolean isRemote() {
        return getWorld().isRemote;
    }

    @Override
    public void markAsDirty() {
        markDirty();
    }

    @Override
    public boolean onRightClick(PlayerEntity player, Hand hand, Direction direction, BlockRayTraceResult hitResult) {
        if (!player.isSneaking()) {
            if (getWorld() != null && !getWorld().isRemote) {
                TileEntityUIFactory.INSTANCE.openUI(this, (ServerPlayerEntity) player);
            }
            return true;
        }
        return false;
    }

    protected boolean shouldSerializeInventories() {
        return true;
    }

    public void scheduleChunkForRenderUpdate() {
        BlockPos pos = getPos();
        getWorld().markBlockRangeForRenderUpdate(pos, getBlockState(), getBlockState());
    }

    public void notifyBlockUpdate() {
        getWorld().notifyNeighborsOfStateChange(pos, getBlockState().getBlock());
    }

    public void writeInitialSyncData(PacketBuffer buffer) {
        buffer.writeByte(this.facing.getIndex());
    }

    public void receiveInitialSyncData(PacketBuffer buffer) {
        this.facing = Direction.values()[buffer.readByte()];
    }

    public void receiveCustomData(int discriminator, PacketBuffer buffer) {
        if (discriminator == -2) {
            this.facing = Direction.values()[buffer.readByte()];
            scheduleChunkForRenderUpdate();
        }
    }

    private static class UpdateEntry {
        private final int discriminator;
        private final byte[] updateData;

        public UpdateEntry(int discriminator, byte[] updateData) {
            this.discriminator = discriminator;
            this.updateData = updateData;
        }
    }

    protected final List<UpdateEntry> updateEntries = new ArrayList<>();

    public void writeCustomData(int discriminator, Consumer<PacketBuffer> dataWriter) {
        ByteBuf backedBuffer = Unpooled.buffer();
        dataWriter.accept(new PacketBuffer(backedBuffer));
        byte[] updateData = Arrays.copyOfRange(backedBuffer.array(), 0, backedBuffer.writerIndex());
        updateEntries.add(new UpdateEntry(discriminator, updateData));
        BlockState blockState = getBlockState();
        world.notifyBlockUpdate(getPos(), blockState, blockState, 0);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT updateTag = new CompoundNBT();
        ListNBT tagList = new ListNBT();
        for (UpdateEntry updateEntry : updateEntries) {
            CompoundNBT entryTag = new CompoundNBT();
            entryTag.putInt("i", updateEntry.discriminator);
            entryTag.putByteArray("d", updateEntry.updateData);
            tagList.add(entryTag);
        }
        this.updateEntries.clear();
        updateTag.put("d", tagList);
        return new SUpdateTileEntityPacket(getPos(), 0, updateTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT updateTag = pkt.getNbtCompound();
        ListNBT tagList = updateTag.getList("d", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundNBT entryTag = tagList.getCompound(i);
            int discriminator = entryTag.getInt("i");
            byte[] updateData = entryTag.getByteArray("d");
            ByteBuf backBuffer = Unpooled.copiedBuffer(updateData);
            receiveCustomData(discriminator, new PacketBuffer(backBuffer));
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT updateTag = super.getUpdateTag();
        ByteBuf backedBuffer = Unpooled.buffer();
        writeInitialSyncData(new PacketBuffer(backedBuffer));
        byte[] updateData = Arrays.copyOfRange(backedBuffer.array(), 0, backedBuffer.writerIndex());
        updateTag.putByteArray("d", updateData);
        return updateTag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        byte[] updateData = tag.getByteArray("d");
        ByteBuf backedBuffer = Unpooled.copiedBuffer(updateData);
        receiveInitialSyncData(new PacketBuffer(backedBuffer));
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);

        if (shouldSerializeInventories()) {
            if (importItems instanceof INBTSerializable)
                ((INBTSerializable) importItems).deserializeNBT(nbt.getCompound("ImportInventory"));
            else
                NBTUtil.readItems(importItems, "ImportInventory", nbt);

            if (exportItems instanceof INBTSerializable)
                ((INBTSerializable) exportItems).deserializeNBT(nbt.get("ExportInventory"));
            else
                NBTUtil.readItems(exportItems, "ExportInventory", nbt);

            importFluids.deserializeNBT(nbt.getCompound("ImportFluidInventory"));
            exportFluids.deserializeNBT(nbt.getCompound("ExportFluidInventory"));
        }

        for (TETrait teTrait : this.teTraits) {
            CompoundNBT traitCompound = nbt.getCompound(teTrait.getName());
            teTrait.deserializeNBT(traitCompound);
        }

        this.newInventory = false;

        this.facing = Direction.values()[nbt.getInt("facing")];
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt = super.write(compound);

        if (shouldSerializeInventories()) {
            if (importItems instanceof INBTSerializable)
                compound.put("ImportInventory", ((INBTSerializable) importItems).serializeNBT());
            else
                NBTUtil.writeItems(importItems, "ImportInventory", nbt);

            if (exportItems instanceof INBTSerializable)
                compound.put("ExportInventory", ((INBTSerializable) exportItems).serializeNBT());
            else
                NBTUtil.writeItems(exportItems, "ExportInventory", nbt);

            nbt.put("ImportFluidInventory", importFluids.serializeNBT());
            nbt.put("ExportFluidInventory", exportFluids.serializeNBT());
        }

        for (TETrait teTrait : this.teTraits) {
            nbt.put(teTrait.getName(), teTrait.serializeNBT());
        }

        nbt.putInt("facing", facing.ordinal());

        return nbt;
    }

    public void setDirection(Direction direction) {
        this.facing = direction;
        if(getWorld() != null && !getWorld().isRemote) {
            notifyBlockUpdate();
            markDirty();
            writeCustomData(-2, buffer -> buffer.writeByte(direction.getIndex()));
        }
    }

    @Override
    public void onPlaced(World world, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (state.hasProperty(BlockStateProperties.FACING))
            this.facing = state.get(BlockStateProperties.FACING);
    }

    @Override
    public void onRemoved(World world, BlockState state, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            for (int i = 0; i < this.getItemInventory().getSlots(); i++) {
                ItemStack stack = this.getItemInventory().getStackInSlot(i);
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }
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

    @Override
    public void tick() {
        timer++;
        for (TETrait teTrait : this.teTraits) {
            if (shouldUpdate(teTrait)) {
                teTrait.update();
            }
        }
    }

    protected boolean shouldUpdate(TETrait trait) {
        return true;
    }

    public long getTimer() {
        return timer;
    }

    public long getOffsetTimer() {
        return timer + offset;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> getFluidInventory()).cast();
        } else if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> getItemInventory()).cast();
        }
        return super.getCapability(cap, side);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return getCapability(cap, null);
    }

    public IItemHandler getItemInventory() {
        return itemInventory;
    }

    public IFluidHandler getFluidInventory() {
        return fluidInventory;
    }

    public IItemHandlerModifiable getImportItems() {
        return importItems;
    }

    public IItemHandlerModifiable getExportItems() {
        return exportItems;
    }

    public FluidTankList getImportFluids() {
        return importFluids;
    }

    public FluidTankList getExportFluids() {
        return exportFluids;
    }

    public void pushItemsIntoNearbyHandlers(Direction... allowed) {
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        for (Direction direction : allowed) {
            mutablePos.setPos(getPos()).move(direction);
            TileEntity tileEntity = getWorld().getTileEntity(mutablePos);
            if (tileEntity == null)
                continue;
            IItemHandler handler = getItemHandlerCap(tileEntity, direction.getOpposite());
            IItemHandler myHandler = getItemHandlerCap(this, direction);
            if (handler.getSlots() == 0 || myHandler.getSlots() == 0)
                continue;

            moveInventoryItems(myHandler, handler);
        }
    }

    public void pullItemsFromNearbyHandlers(Direction... allowed) {
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        for (Direction direction : allowed) {
            mutablePos.setPos(getPos()).move(direction);
            TileEntity tileEntity = getWorld().getTileEntity(mutablePos);
            if (tileEntity == null)
                continue;
            IItemHandler handler = getItemHandlerCap(tileEntity, direction.getOpposite());
            IItemHandler myHandler = getItemHandlerCap(this, direction);
            if (handler.getSlots() == 0 || myHandler.getSlots() == 0)
                continue;

            moveInventoryItems(handler, myHandler);
        }
    }

    protected static void moveInventoryItems(IItemHandler source, IItemHandler target) {
        for (int srcIndex = 0; srcIndex < source.getSlots(); srcIndex++) {
            ItemStack sourceStack = source.extractItem(srcIndex, Integer.MAX_VALUE, true);
            if (sourceStack.isEmpty())
                continue;
            ItemStack remainder = ItemHandlerHelper.insertItemStacked(target, sourceStack, true);
            int amountToInsert = sourceStack.getCount() - remainder.getCount();
            if (amountToInsert > 0) {
                sourceStack = source.extractItem(srcIndex, amountToInsert, false);
                ItemHandlerHelper.insertItemStacked(target, sourceStack, false);
            }
        }
    }

    public void pullFluidsFromNearbyHandlers(Direction... allowed) {
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        for (Direction direction : allowed) {
            mutablePos.setPos(getPos()).move(direction);
            TileEntity tileEntity = getWorld().getTileEntity(mutablePos);
            if (tileEntity == null)
                continue;

            IFluidHandler handler = getFluidHandlerCap(tileEntity, direction.getOpposite());
            IFluidHandler myHandler = getFluidHandlerCap(this, direction);
            if (handler == EmptyFluidHandler.INSTANCE || myHandler == EmptyFluidHandler.INSTANCE)
                continue;

            FluidStack drainStack = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
            int drainAmount = myHandler.fill(drainStack, IFluidHandler.FluidAction.EXECUTE);
            if (drainAmount > 0) {
                handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    public void pushFluidsIntoNearbyHandlers(Direction... allowed) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (Direction direction : allowed) {
            mutable.setPos(getPos()).move(direction);
            TileEntity tileEntity = getWorld().getTileEntity(mutable);
            if (tileEntity == null)
                continue;

            IFluidHandler handler = getFluidHandlerCap(tileEntity, direction.getOpposite());
            IFluidHandler myHandler = getFluidHandlerCap(this, direction);
            if (handler == EmptyFluidHandler.INSTANCE || myHandler == EmptyFluidHandler.INSTANCE)
                continue;

            int fillAmount = handler.fill(myHandler.getFluidInTank(0), IFluidHandler.FluidAction.EXECUTE);
            if (fillAmount > 0)
                myHandler.drain(fillAmount, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    public boolean fillInternalTankFromFluidContainer(IItemHandlerModifiable importItems, IItemHandlerModifiable exportItems, int inputSlot, int outputSlot) {
        ItemStack inputStack = importItems.extractItem(inputSlot, 1, true);
        FluidActionResult result = FluidUtil.tryEmptyContainer(inputStack, getImportFluids(), Integer.MAX_VALUE, null, true);
        if (result.isSuccess()) {
            ItemStack remainingItem = result.getResult();
            if (ItemStack.areItemStacksEqual(inputStack, remainingItem))
                return false;
            if (!remainingItem.isEmpty() && !exportItems.insertItem(outputSlot, remainingItem, true).isEmpty())
                return false;
            importItems.extractItem(inputSlot, 1, false);
            exportItems.insertItem(outputSlot, remainingItem, false);
            return true;
        }
        return false;
    }

    public boolean fillContainerFromInternalTank(IItemHandlerModifiable importItems, IItemHandlerModifiable exportItems, int inputSlot, int outputSlot) {
        ItemStack emptyContainer = importItems.extractItem(inputSlot, 1, true);
        FluidActionResult result = FluidUtil.tryFillContainer(emptyContainer, getExportFluids(), Integer.MAX_VALUE, null, true);
        if (result.isSuccess()) {
            ItemStack remainingItem = result.getResult();
            if (!exportItems.insertItem(outputSlot, remainingItem, true).isEmpty())
                return false;
            importItems.extractItem(inputSlot, 1, false);
            exportItems.insertItem(outputSlot, remainingItem, false);
            return true;
        }
        return false;
    }

    public IFluidHandler getFluidHandlerCap(TileEntity tileEntity, Direction direction) {
        return tileEntity == null ? EmptyFluidHandler.INSTANCE : tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).orElse(EmptyFluidHandler.INSTANCE);
    }

    public IItemHandler getItemHandlerCap(TileEntity tileEntity, Direction direction) {
        return tileEntity == null ? new ItemStackHandler(0) : tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction).orElse(new ItemStackHandler(0));
    }

    public static boolean addItemsToItemHandler(final IItemHandler handler,
                                                final boolean simulate,
                                                final List<ItemStack> items) {
        // determine if there is sufficient room to insert all items into the target inventory
        final boolean canMerge = Util.simulateItemStackMerge(items, handler);

        // if we're not simulating and the merge should succeed, perform the merge.
        if (!simulate && canMerge)
            items.forEach(stack -> {
                ItemStack rest = ItemHandlerHelper.insertItemStacked(handler, stack, simulate);
                if (!rest.isEmpty())
                    throw new IllegalStateException(
                            String.format("Insertion failed, remaining stack contained %d items.", rest.getCount()));
            });

        return canMerge;
    }

    public static boolean addFluidsToFluidHandler(IFluidHandler handler, IFluidHandler.FluidAction action, List<FluidStack> items) {
        boolean filledAll = true;
        for (FluidStack stack : items) {
            int filled = handler.fill(stack, action);
            filledAll &= filled == stack.getAmount();
            if (!filledAll && action.execute())
                return false;
        }
        return filledAll;
    }

    public final String getName() {
        return String.format("%s.machine.%s", this.getBlockState().getBlock().getRegistryName().getNamespace(), this.getBlockState().getBlock().getRegistryName().getPath());
    }

    public final String getFullName() {
        return getName() + ".name";
    }

    public void renderTileEntity(CCRenderState renderState, IVertexOperation... pipeline) {
        TextureAtlasSprite atlasSprite = TextureUtils.getMissingSprite();
        for (Direction face : Direction.values()) {
            Textures.renderFace(renderState, face, Cuboid6.full, atlasSprite, pipeline);
        }
    }

}
