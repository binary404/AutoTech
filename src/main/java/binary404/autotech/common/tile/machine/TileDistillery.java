package binary404.autotech.common.tile.machine;

import binary404.autotech.common.block.machine.BlockDistillery;
import binary404.autotech.common.core.logistics.Tank;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.DistilleryManager;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileMachine;
import binary404.autotech.common.tile.util.ITank;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileDistillery extends TileMachine<BlockDistillery> implements ITank {

    protected Tank tank2 = new Tank(0);

    DistilleryManager.DistilleryRecipe recipe;

    public TileDistillery() {
        this(Tier.MV);
    }

    public TileDistillery(Tier tier) {
        super(ModTiles.distillery, tier);
        this.inv.set(0);
        this.tank.setCapacity(FluidAttributes.BUCKET_VOLUME * 5).setChange(() -> TileDistillery.this.sync(10));
        this.tank2.setCapacity(FluidAttributes.BUCKET_VOLUME * 5).setChange(() -> TileDistillery.this.sync(10));
    }

    @Override
    protected boolean canStart() {
        if (tank.isEmpty() || energy.getEnergyStored() <= 0) {
            return false;
        }

        this.recipe = DistilleryManager.getRecipe(tank.getFluid());

        if (recipe == null) {
            return false;
        }

        if (tank.getFluid().getAmount() < recipe.getInput().getAmount()) {
            return false;
        }

        return tank2.getFluid().isEmpty() || tank2.getFluid().getFluid() == recipe.getOutput().getFluid();
    }

    @Override
    protected boolean hasValidInput() {
        if (recipe == null) {
            this.recipe = DistilleryManager.getRecipe(tank.getFluid());
        }

        if (recipe == null) {
            return false;
        }

        return recipe.getInput().getAmount() <= tank.getFluid().getAmount();
    }

    @Override
    protected void processStart() {
        processMax = recipe.getEnergy();
        processRem = processMax;
    }

    @Override
    protected void clearRecipe() {
        recipe = null;
    }

    @Override
    protected void processFinish() {
        if (recipe == null) {
            this.recipe = DistilleryManager.getRecipe(tank.getFluid());
        }

        if (recipe == null) {
            processOff();
            return;
        }

        FluidStack output = recipe.getOutput();

        tank2.fill(output.copy(), IFluidHandler.FluidAction.EXECUTE);


        this.tank.drain(recipe.getInput(), IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    protected void transferOutput() {
        if (!this.tank2.isEmpty()) {
            for (Direction direction : Direction.values()) {
                TileEntity tile = world.getTileEntity(pos.offset(direction));
                if (tile != null) {
                    int drain = get(tile, direction).orElse(new Tank(0)).fill(tank2.getFluid(), IFluidHandler.FluidAction.EXECUTE);
                    this.tank2.drain(drain, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

    public static LazyOptional<IFluidHandler> get(TileEntity tile, Direction direction) {
        return tile == null ? LazyOptional.empty() : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction != null ? direction.getOpposite() : null);
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> new FluidTank(this.tank.getCapacity()) {

                @Override
                public int fill(FluidStack resource, FluidAction action) {
                    return TileDistillery.this.tank.fill(resource, action);
                }

                @Nonnull
                @Override
                public FluidStack drain(FluidStack resource, FluidAction action) {
                    return TileDistillery.this.tank2.drain(resource, action);
                }
            }).cast();
        }

        return super.getCapability(cap, side);
    }

    public Tank getTank2() {
        return tank2;
    }
}
