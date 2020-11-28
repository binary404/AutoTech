package binary404.autotech.common.tile.machine;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.machine.BlockCompactor;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.CompactorManager;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileMachine;
import net.minecraft.item.ItemStack;

public class TileCompactor extends TileMachine<BlockCompactor> {

    CompactorManager.CompactorRecipe recipe;

    public TileCompactor() {
        this(Tier.LV);
    }

    public TileCompactor(Tier tier) {
        super(ModTiles.compactor, tier);
        this.inv.set(2);
    }

    @Override
    protected boolean canStart() {
        if (inv.getStackInSlot(0).isEmpty() || energy.getEnergyStored() <= 0) {
            return false;
        }

        recipe = CompactorManager.getRecipe(this.inv.getStackInSlot(0));

        if (recipe == null)
            return false;

        if (this.inv.getStackInSlot(0).getCount() < recipe.getInputCount()) {
            return false;
        }

        if (this.tier.ordinal() < this.recipe.getMinTier().ordinal()) {
            return false;
        }

        return this.inv.getStackInSlot(1).isEmpty() || this.inv.getStackInSlot(1).getItem() == this.recipe.getOutput().getItem();
    }

    @Override
    protected boolean hasValidInput() {
        if (recipe == null)
            recipe = CompactorManager.getRecipe(this.inv.getStackInSlot(0));

        if (recipe == null)
            return false;

        if (this.tier.ordinal() < this.recipe.getMinTier().ordinal()) {
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
        if (recipe == null)
            recipe = CompactorManager.getRecipe(inv.getStackInSlot(0));

        if (recipe == null) {
            processOff();
            return;
        }

        ItemStack output = recipe.getOutput();

        if (inv.getStackInSlot(1).isEmpty()) {
            inv.setStack(1, output.copy());
        } else {
            inv.getStackInSlot(1).grow(output.getCount());
        }

        inv.getStackInSlot(0).shrink(recipe.getInput().getCount());
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return slot == 0 && CompactorManager.recipeExists(stack);
    }


    @Override
    public boolean canExtract(int slot) {
        return slot != 0;
    }
}
