package binary404.autotech.common.tile.machine;

import binary404.autotech.common.block.machine.BlockGrinder;
import binary404.autotech.common.core.GrinderManager;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.util.Counter;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileMachine;
import binary404.autotech.common.tile.core.TileTiered;
import binary404.autotech.common.tile.util.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class TileGrinder extends TileMachine<BlockGrinder> implements IInventory {

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

        return true;
    }

    @Override
    protected boolean hasValidInput() {
        if (recipe == null) {
            this.recipe = GrinderManager.getRecipe(inv.getStackInSlot(0));
        }

        if (recipe == null) {
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
