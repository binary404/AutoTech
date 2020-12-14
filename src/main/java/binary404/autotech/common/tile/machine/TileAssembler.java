package binary404.autotech.common.tile.machine;

import binary404.autotech.common.block.machine.BlockAssembler;
import binary404.autotech.common.core.logistics.fluid.Tank;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.AssemblerManager;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileMachine;
import binary404.autotech.common.tile.util.ITank;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

public class TileAssembler extends TileMachine<BlockAssembler> implements ITank {

    AssemblerManager.AssemblerRecipe recipe;

    public TileAssembler() {
        this(Tier.MV);
    }

    public TileAssembler(Tier tier) {
        super(ModTiles.assembler, tier);
        this.inv.set(10);
        this.tank = new Tank(1000);
    }

    @Override
    protected boolean canStart() {
        List<ItemStack> input = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            input.add(inv.getStackInSlot(i));
        }

        if (input.isEmpty() || energy.getEnergyStored() <= 0) {
            return false;
        }

        this.recipe = AssemblerManager.getRecipe(input, this.tank.getFluid());

        if (recipe == null) {
            return false;
        }

        if (this.tier.ordinal() < this.recipe.getMinTier().ordinal()) {
            return false;
        }

        return this.inv.getStackInSlot(9).isEmpty() || this.inv.getStackInSlot(9).getItem() == this.recipe.getOutput().getItem();
    }


    @Override
    protected boolean hasValidInput() {
        List<ItemStack> input = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            input.add(inv.getStackInSlot(i));
        }

        if (recipe == null) {
            this.recipe = AssemblerManager.getRecipe(input, this.tank.getFluid());
        }

        if (recipe == null) {
            return false;
        }

        if (this.tier.ordinal() < this.recipe.getMinTier().ordinal()) {
            return false;
        }

        return true;
    }

    @Override
    protected void processStart() {
        processMax = recipe.getEnergy();
        processRem = processMax;
    }

    @Override
    protected void processFinish() {
        List<ItemStack> input = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            input.add(inv.getStackInSlot(i));
        }

        if (recipe == null) {
            this.recipe = AssemblerManager.getRecipe(input, this.tank.getFluid());
        }

        if (recipe == null) {
            processOff();
            return;
        }

        ItemStack output = recipe.getOutput();

        if (inv.getStackInSlot(9).isEmpty()) {
            inv.setStack(9, output.copy());
        } else {
            inv.getStackInSlot(9).grow(output.getCount());
        }

        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                stack.shrink(1);
            }
        }

        this.tank.drain(this.recipe.getFluid_input(), IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return slot != 9;
    }

    @Override
    public boolean canExtract(int slot) {
        return slot == 9;
    }
}
