package binary404.autotech.common.tile.machine;

import binary404.autotech.common.block.machine.BlockSawMill;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.SawMillManager;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileMachine;
import net.minecraft.item.ItemStack;

public class TileSawMill extends TileMachine {

    private SawMillManager.SawMillRecipe recipe;

    public TileSawMill() {
        this(Tier.LV);
    }

    public TileSawMill(Tier tier) {
        super(ModTiles.sawmill, tier);
        this.inv.set(3);
    }

    @Override
    protected boolean canStart() {
        if (inv.getStackInSlot(0).isEmpty() || energy.getEnergyStored() <= 0)
            return false;

        getRecipe();

        if (recipe == null)
            return false;

        if (inv.getStackInSlot(0).getCount() < recipe.getInputCount())
            return false;

        ItemStack primaryItem = recipe.getOutput();
        ItemStack secondItem = recipe.getSecondaryOutput();

        if (!secondItem.isEmpty() && !inv.getStackInSlot(2).isEmpty()) {
            if (inv.getStackInSlot(2).getItem() != secondItem.getItem())
                return false;
        }

        return inv.getStackInSlot(1).isEmpty() || inv.getStackInSlot(1).getItem() == primaryItem.getItem();
    }

    @Override
    protected boolean hasValidInput() {
        if (recipe == null)
            getRecipe();
        if (recipe == null)
            return false;
        return recipe.getInputCount() <= inv.getStackInSlot(0).getCount();
    }

    @Override
    protected void clearRecipe() {
        recipe = null;
    }

    protected void getRecipe() {
        recipe = SawMillManager.getRecipe(this.inv.getStackInSlot(0));
    }

    @Override
    protected void processStart() {
        processMax = recipe.getEnergy();
        processRem = processMax;
    }

    @Override
    protected void processFinish() {
        if (recipe == null)
            getRecipe();
        if (recipe == null) {
            processOff();
            return;
        }

        ItemStack primaryItem = recipe.getOutput();
        ItemStack secondaryItem = recipe.getSecondaryOutput();

        if (inv.getStackInSlot(1).isEmpty())
            inv.setStack(1, primaryItem.copy());
        else
            inv.getStackInSlot(1).grow(primaryItem.getCount());

        if (!secondaryItem.isEmpty()) {
            int recipeChance = this.recipe.getSecondaryOutputChance();
            if (recipeChance >= 100 || world.rand.nextInt(100) < recipeChance) {
                if (inv.getStackInSlot(2).isEmpty())
                    inv.setStack(2, secondaryItem.copy());
                else
                    inv.getStackInSlot(2).grow(secondaryItem.getCount());
            }
        }

        inv.getStackInSlot(0).shrink(recipe.getInput().getCount());
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return slot == 0 && SawMillManager.recipeExists(stack);
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
