package binary404.autotech.common.tile.multiblock;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.block.multiblock.BlockDistillery;
import binary404.autotech.common.core.lib.multiblock.*;
import binary404.autotech.common.tile.util.IOutputTank;
import binary404.autotech.common.core.logistics.fluid.InputOutputFluidWrapper;
import binary404.autotech.common.core.logistics.fluid.Tank;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.DistilleryManager;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.util.ITank;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileDistillery extends MultiblockControllerBase<BlockDistillery> implements ITank, IOutputTank {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
            MultiblockAbility.INPUT_ENERGY,
            MultiblockAbility.EXPORT_FLUIDS,
            MultiblockAbility.IMPORT_FLUIDS
    };

    protected Tank output_tank = new Tank(0);

    DistilleryManager.DistilleryRecipe recipe;

    public TileDistillery() {
        this(Tier.MV);
    }

    public TileDistillery(Tier tier) {
        super(ModTiles.distillery, tier);
        this.inv.set(0);
        this.tank.setCapacity(FluidAttributes.BUCKET_VOLUME * 5).setChange(() -> TileDistillery.this.sync(4));
        this.output_tank.setCapacity(FluidAttributes.BUCKET_VOLUME * 5).setChange(() -> TileDistillery.this.sync(4));
    }

    @Override
    public void readSync(CompoundNBT nbt) {
        super.readSync(nbt);
        CompoundNBT tank2 = nbt.getCompound("output_tank");
        this.output_tank.readFromNBT(tank2);
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        CompoundNBT output_tank = new CompoundNBT();
        this.output_tank.writeToNBT(output_tank);
        nbt.put("output_tank", output_tank);
        return super.writeSync(nbt);
    }

    @Override
    protected void updateFormedValid() {

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

        return output_tank.getFluid().isEmpty() || output_tank.getFluid().getFluid() == recipe.getOutput().getFluid();
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

        output_tank.fill(output.copy(), IFluidHandler.FluidAction.EXECUTE);

        this.tank.drain(recipe.getInput(), IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    protected void transferOutput() {
    }

    public static LazyOptional<IFluidHandler> get(TileEntity tile, Direction direction) {
        return tile == null ? LazyOptional.empty() : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction != null ? direction.getOpposite() : null);
    }

    protected BlockState getCasingState() {
        return ModBlocks.heat_proof_casing.getDefaultState();
    }

    protected BlockState getCoilState() {
        return ModBlocks.basic_coil.getDefaultState();
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXX", "XXX", "XXX")
                .aisle("CCC", "C#C", "CCC")
                .aisle("CCC", "C#C", "CCC")
                .aisle("ZZZ", "ZYZ", "ZZZ")
                .where('Z', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
                .where('X', statePredicate(getCasingState()))
                .where('C', statePredicate(getCoilState()))
                .where('Y', selfPredicate())
                .where('#', isAirPredicate())
                .build();
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> new InputOutputFluidWrapper(this.tank, this.output_tank)).cast();
        }
        return super.getCapability(cap, side);
    }

    public Tank getOutputTank() {
        return output_tank;
    }
}
