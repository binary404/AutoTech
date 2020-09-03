package binary404.autotech.common.tile.device;

import binary404.autotech.common.block.device.BlockWaterPump;
import binary404.autotech.common.core.logistics.Tank;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileCore;
import binary404.autotech.common.tile.core.TileTickable;
import binary404.autotech.common.tile.util.IInventory;
import binary404.autotech.common.tile.util.ITank;
import com.google.gson.internal.$Gson$Preconditions;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileWaterPump extends TileTickable<BlockWaterPump> implements IInventory, ITank {

    public TileWaterPump() {
        super(ModTiles.waterpump);
        this.inv.set(0);
        this.tank.setCapacity(FluidAttributes.BUCKET_VOLUME * 10).setChange(() -> TileWaterPump.this.sync(10));
    }

    @Override
    protected int postTick(World world) {
        if (this.tank.getFluidAmount() < this.tank.getCapacity()) {
            this.tank.fill(new FluidStack(Fluids.WATER, 100), IFluidHandler.FluidAction.EXECUTE);
        }

        if (!this.tank.isEmpty()) {
            for (Direction direction : Direction.values()) {
                TileEntity tile = world.getTileEntity(pos.offset(direction));
                if (tile != null) {
                    int drain = get(tile, direction).orElse(new Tank(0)).fill(new FluidStack(Fluids.WATER, 100), IFluidHandler.FluidAction.EXECUTE);
                    this.tank.drain(drain, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }

        return super.postTick(world);
    }

    public static LazyOptional<IFluidHandler> get(TileEntity tile, Direction direction) {
        return tile == null ? LazyOptional.empty() : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction != null ? direction.getOpposite() : null);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return false;
    }
}
