package binary404.autotech.common.tile.machine;

import binary404.autotech.common.block.machine.BlockCentrifuge;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.CentrifugeManager;
import binary404.autotech.common.fluid.ModFluids;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileMachine;
import binary404.autotech.common.tile.util.ITank;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileCentrifuge extends TileMachine implements ITank {

    CentrifugeManager.CentrifugeRecipe recipe;

    public TileCentrifuge() {
        this(Tier.MV);
    }

    public TileCentrifuge(Tier tier) {
        super(ModTiles.centrifuge, tier);
        this.inv.set(3);
        this.tank.setCapacity(FluidAttributes.BUCKET_VOLUME * 5).setChange(() -> TileCentrifuge.this.sync(10));
    }

    @Override
    protected boolean canStart() {
        if (inv.getStackInSlot(0).isEmpty() || energy.getEnergyStored() <= 0) {
            return false;
        }

        this.recipe = CentrifugeManager.getRecipe(inv.getStackInSlot(0));

        if (recipe == null) {
            return false;
        }

        if (this.inv.getStackInSlot(0).getCount() < recipe.getInputCount()) {
            return false;
        }

        if (!recipe.getOutput2().isEmpty() && !inv.getStackInSlot(2).isEmpty()) {
            if (inv.getStackInSlot(2).getItem() != recipe.getOutput2().getItem())
                return false;
        }

        if (this.tier.ordinal() < this.recipe.getMinTier().ordinal()) {
            return false;
        }

        if (this.tank.getFluid().getFluid() != ModFluids.distilled_water) {
            return false;
        }
        return this.inv.getStackInSlot(1).isEmpty() || this.inv.getStackInSlot(1).getItem() == this.recipe.getOutput().getItem();
    }

    @Override
    protected boolean hasValidInput() {
        if (recipe == null) {
            this.recipe = CentrifugeManager.getRecipe(inv.getStackInSlot(0));
        }

        if (recipe == null) {
            return false;
        }

        if (this.tier.ordinal() < this.recipe.getMinTier().ordinal()) {
            return false;
        }

        if (this.tank.getFluid().getFluid() != ModFluids.distilled_water) {
            return false;
        }

        if (this.tank.getFluid().getAmount() < 1000) {
            return false;
        }

        return recipe.getInputCount() <= inv.getStackInSlot(0).getCount();
    }

    @Override
    protected void processStart() {
        processMax = recipe.getEnergy();
        processRem = processMax;
    }

    @Override
    protected void clearRecipe() {
        this.recipe = null;
    }

    @Override
    protected void processFinish() {
        if (recipe == null) {
            this.recipe = CentrifugeManager.getRecipe(inv.getStackInSlot(0));
        }

        if (recipe == null) {
            processOff();
            return;
        }

        ItemStack output1 = recipe.getOutput();
        ItemStack output2 = recipe.getOutput2();

        if (inv.getStackInSlot(1).isEmpty()) {
            inv.setStack(1, output1.copy());
        } else {
            inv.getStackInSlot(1).grow(output1.getCount());
        }

        if (!output2.isEmpty()) {
            int secondChance = recipe.getSecondChance();
            if (secondChance >= 100 || world.rand.nextInt(100) < secondChance) {
                if (inv.getStackInSlot(2).isEmpty()) {
                    inv.setStack(2, output2.copy());
                } else {
                    inv.getStackInSlot(2).grow(output2.getCount());
                }
            }
        }

        this.tank.drain(1000, IFluidHandler.FluidAction.EXECUTE);

        inv.getStackInSlot(0).shrink(recipe.getInput().getCount());
    }

    @Override
    protected void transferInput() {
        for (int i = 0; i < 6; i++) {
            if (this.itemConfig.getType(Direction.byIndex(i)).canExtract) {
                if (transferItem(1, 1, Direction.byIndex(i))) {
                    break;
                }
                if (transferItem(2, 1, Direction.byIndex(i))) {
                    break;
                }
            }
        }
    }

    @Override
    protected void transferOutput() {
        for (int i = 0; i < 6; i++) {
            if (this.itemConfig.getType(Direction.byIndex(i)).canReceive) {
                if (extractItem(0, 1, Direction.byIndex(i))) {
                    break;
                }
            }
        }
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return slot == 0 && CentrifugeManager.recipeExists(stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canExtract(int slot) {
        return slot != 0;
    }
}
