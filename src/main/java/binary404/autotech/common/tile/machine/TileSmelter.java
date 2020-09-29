package binary404.autotech.common.tile.machine;

import binary404.autotech.common.block.machine.BlockSmelter;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.util.Counter;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileMachine;
import binary404.autotech.common.tile.core.TileTiered;
import binary404.autotech.common.tile.util.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

import java.util.Optional;

public class TileSmelter extends TileMachine<BlockSmelter> {

    FurnaceRecipe recipe;

    public TileSmelter() {
        this(Tier.LV);
    }

    public TileSmelter(Tier tier) {
        super(ModTiles.smelter, tier);
        this.inv.set(2);
    }

    @Override
    protected boolean canStart() {
        if (inv.getStackInSlot(0).isEmpty() || energy.getEnergyStored() <= 0)
            return false;
        this.recipe = getSmeltingResultForItem(world, this.inv.getStackInSlot(0));

        if (recipe == null)
            return false;

        ItemStack output = recipe.getRecipeOutput();

        return inv.getStackInSlot(1).isEmpty() || output.getItem() == inv.getStackInSlot(1).getItem();
    }

    @Override
    protected boolean hasValidInput() {
        if (recipe == null)
            this.recipe = getSmeltingResultForItem(world, this.inv.getStackInSlot(0));
        if (recipe == null)
            return false;
        return true;
    }

    @Override
    protected void clearRecipe() {
        recipe = null;
    }

    @Override
    protected void processStart() {
        processMax = recipe.getCookTime() * 150;
        processRem = processMax;
    }

    @Override
    protected void processFinish() {
        if (recipe == null)
            this.recipe = getSmeltingResultForItem(world, this.inv.getStackInSlot(0));
        if (recipe == null) {
            processOff();
            return;
        }
        ItemStack output = recipe.getRecipeOutput().copy();
        if (inv.getStackInSlot(1).isEmpty())
            inv.setStack(1, output);
        else
            inv.getStackInSlot(1).grow(output.getCount());

        inv.getStackInSlot(0).shrink(1);
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    public static FurnaceRecipe getSmeltingResultForItem(World world, ItemStack itemStack) {
        Optional<FurnaceRecipe> matchingRecipe = getMatchingRecipeForInput(world, itemStack);
        if (!matchingRecipe.isPresent()) return null;
        return matchingRecipe.get();  // beware! You must deep copy otherwise you will alter the recipe itself
    }

    public static Optional<FurnaceRecipe> getMatchingRecipeForInput(World world, ItemStack itemStack) {
        RecipeManager recipeManager = world.getRecipeManager();
        Inventory singleItemInventory = new Inventory(itemStack);
        Optional<FurnaceRecipe> matchingRecipe = recipeManager.getRecipe(IRecipeType.SMELTING, singleItemInventory, world);
        return matchingRecipe;
    }

    @Override
    public boolean canInsert(int index, ItemStack stack) {
        return index == 0 && getSmeltingResultForItem(this.world, stack) != null && getSmeltingResultForItem(this.world, stack).getRecipeOutput() != ItemStack.EMPTY;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canExtract(int slot) {
        return slot != 0;
    }

    @Override
    protected void transferInput() {
        for (int i = 0; i < 6; i++) {
            if (this.itemConfig.getType(Direction.byIndex(i)).canExtract) {
                if (transferItem(1, 1, Direction.byIndex(i))) {
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
}
