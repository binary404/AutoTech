package binary404.autotech.common.tile.machine;

import binary404.autotech.common.block.machine.BlockAssembler;
import binary404.autotech.common.block.machine.BlockCentrifuge;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.AssemblerManager;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileMachine;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileAssembler extends TileMachine<BlockAssembler> {

    AssemblerManager.AssemblerRecipe recipe;

    int recipeTicks = 0;

    public TileAssembler() {
        this(Tier.MV);
    }

    public TileAssembler(Tier tier) {
        super(ModTiles.assembler, tier);
        this.inv.set(10);
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

        this.recipe = AssemblerManager.getRecipe(input);

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
            this.recipe = AssemblerManager.getRecipe(input);
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
        this.recipeTicks = recipe.getMinTime();
    }

    @Override
    protected boolean canFinish() {
        return super.canFinish() && recipeTicks <= 0;
    }

    @Override
    protected int processTick() {
        this.recipeTicks--;
        if (processRem <= 0) {
            return 0;
        }
        this.energy.consume(this.tier.use);
        processRem -= this.tier.use;
        return this.tier.use;
    }

    @Override
    protected void processFinish() {
        List<ItemStack> input = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            input.add(inv.getStackInSlot(i));
        }

        if (recipe == null) {
            this.recipe = AssemblerManager.getRecipe(input);
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
