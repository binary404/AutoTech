package binary404.autotech.common.tile.machine;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.machine.BlockGrinder;
import binary404.autotech.common.core.manager.GrinderManager;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileMachine;
import binary404.autotech.common.tile.util.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

public class TileGrinder extends TileMachine<BlockGrinder> {

    GrinderManager.GrinderRecipe recipe;

    public TileGrinder() {
        this(Tier.LV);
    }

    public TileGrinder(Tier tier) {
        super(ModTiles.grinder, tier);
        this.inv.set(4);
    }

    @Override
    protected boolean canStart() {
        if (inv.getStackInSlot(0).isEmpty() || energy.getEnergyStored() <= 0) {
            return false;
        }

        this.recipe = GrinderManager.getRecipe(inv.getStackInSlot(0));

        if (recipe == null) {
            return false;
        }

        if (this.inv.getStackInSlot(0).getCount() < recipe.getInput().getCount()) {
            return false;
        }

        if (!recipe.getSecondaryOutput().isEmpty() && !inv.getStackInSlot(2).isEmpty()) {
            if (inv.getStackInSlot(2).getItem() != recipe.getSecondaryOutput().getItem())
                return false;
        }

        if (!recipe.getThirdOutput().isEmpty() && !inv.getStackInSlot(3).isEmpty()) {
            if (inv.getStackInSlot(3).getItem() != recipe.getThirdOutput().getItem())
                return false;
        }

        if (this.tier.ordinal() < this.recipe.getMinTier().ordinal()) {
            AutoTech.LOGGER.error(this.tier.ordinal() + " " + this.recipe.getMinTier().ordinal());
            return false;
        }

        return this.inv.getStackInSlot(1).isEmpty() || this.inv.getStackInSlot(1).getItem() == this.recipe.getPrimaryOutput().getItem();
    }

    @Override
    protected boolean hasValidInput() {
        if (recipe == null) {
            this.recipe = GrinderManager.getRecipe(inv.getStackInSlot(0));
        }

        if (recipe == null) {
            return false;
        }


        if (this.tier.ordinal() < this.recipe.getMinTier().ordinal()) {
            System.out.println(this.tier.ordinal() + " " + this.recipe.getMinTier().ordinal());
            return false;
        }

        return recipe.getInput().getCount() <= inv.getStackInSlot(0).getCount();
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
            this.recipe = GrinderManager.getRecipe(inv.getStackInSlot(0));
        }
        if (recipe == null) {
            processOff();
            return;
        }

        ItemStack primaryItem = recipe.getPrimaryOutput();
        ItemStack secondOutput = recipe.getSecondaryOutput();
        ItemStack thirdOutput = recipe.getThirdOutput();

        if (inv.getStackInSlot(1).isEmpty()) {
            inv.setStack(1, primaryItem.copy());
        } else {
            inv.getStackInSlot(1).grow(primaryItem.getCount());
        }

        if (!secondOutput.isEmpty() && this.tier != Tier.LV) {
            int secondaryChance = this.recipe.getSecondaryChance();
            if (secondaryChance >= 100 || world.rand.nextInt(100) < secondaryChance) {
                if (inv.getStackInSlot(2).isEmpty()) {
                    inv.setStack(2, secondOutput.copy());
                } else {
                    inv.getStackInSlot(2).grow(secondOutput.getCount());
                }
            }
        }

        if (!thirdOutput.isEmpty() && this.tier != Tier.LV) {
            int thirdChance = this.recipe.getThirdChance();
            if (thirdChance >= 100 || world.rand.nextInt(100) < thirdChance) {
                if (inv.getStackInSlot(3).isEmpty()) {
                    inv.setStack(3, thirdOutput.copy());
                } else {
                    inv.getStackInSlot(3).grow(thirdOutput.getCount());
                }
            }
        }

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
                if (transferItem(3, 1, Direction.byIndex(i))) {
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
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return slot == 0;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canExtract(int slot) {
        return slot == 1;
    }

}
