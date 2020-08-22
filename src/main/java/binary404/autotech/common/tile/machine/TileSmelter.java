package binary404.autotech.common.tile.machine;

import binary404.autotech.common.block.machine.BlockSmelter;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.util.Counter;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileTiered;
import binary404.autotech.common.tile.util.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

import java.util.Optional;

public class TileSmelter extends TileTiered<BlockSmelter> implements IInventory {

    protected final Counter burner;
    protected boolean burning;

    public TileSmelter() {
        this(Tier.LV);
    }

    public TileSmelter(Tier tier) {
        super(ModTiles.smelter, tier);
        this.inv.set(2);
        this.burner = new Counter(tier.speed);
    }

    @Override
    public void readStorable(CompoundNBT nbt) {
        super.readStorable(nbt);
        this.burner.read(nbt, "burner");
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT nbt) {
        this.burner.write(nbt, "burner");
        return super.writeStorable(nbt);
    }

    @Override
    public void readSync(CompoundNBT nbt) {
        super.readSync(nbt);
        this.burning = nbt.getBoolean("burning");
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        nbt.putBoolean("burning", this.burning);
        return super.writeSync(nbt);
    }

    @Override
    protected int postTick(World world) {
        super.postTick(world);
        if (!isRemote() && checkRedstone()) {
            if (this.energy.hasEnergy()) {
                if (!this.burner.isEmpty() && this.inv.getStackInSlot(0) != ItemStack.EMPTY && (getSmeltingResultForItem(world, this.inv.getStackInSlot(0)).getItem() == this.inv.getStackInSlot(1).getItem() || this.inv.getStackInSlot(1) == ItemStack.EMPTY) && this.energy.getEnergyStored() >= this.tier.use) {
                    this.burner.back();
                    this.energy.consume(this.tier.use);
                    this.burning = true;
                    sync(4);
                }
            }

            if (this.burner.isEmpty()) {
                ItemStack stack = this.inv.getStackInSlot(0);
                ItemStack burn = this.inv.getStackInSlot(1);
                ItemStack result = this.getSmeltingResultForItem(this.world, new ItemStack(stack.getItem(), 1));
                if (!stack.isEmpty()) {
                    this.burner.setAll(this.tier.speed);
                    stack.shrink(1);

                    if (this.inv.getStackInSlot(1) == ItemStack.EMPTY)
                        this.inv.setStack(1, result);
                    else if (burn.getItem() == result.getItem())
                        burn.grow(1);

                    sync(4);
                }
            }

            if (this.inv.getStackInSlot(0).isEmpty()) {
                this.burner.setTicks(this.tier.speed);
                this.burning = false;
                sync();
            }
        }
        return this.burning ? 10 : -1;
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    public static ItemStack getSmeltingResultForItem(World world, ItemStack itemStack) {
        Optional<FurnaceRecipe> matchingRecipe = getMatchingRecipeForInput(world, itemStack);
        if (!matchingRecipe.isPresent()) return ItemStack.EMPTY;
        return matchingRecipe.get().getRecipeOutput().copy();  // beware! You must deep copy otherwise you will alter the recipe itself
    }

    public static Optional<FurnaceRecipe> getMatchingRecipeForInput(World world, ItemStack itemStack) {
        RecipeManager recipeManager = world.getRecipeManager();
        Inventory singleItemInventory = new Inventory(itemStack);
        Optional<FurnaceRecipe> matchingRecipe = recipeManager.getRecipe(IRecipeType.SMELTING, singleItemInventory, world);
        return matchingRecipe;
    }

    @Override
    public boolean canInsert(int index, ItemStack stack) {
        return index == 0 && getSmeltingResultForItem(this.world, stack) != null && getSmeltingResultForItem(this.world, stack) != ItemStack.EMPTY;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canExtract(int slot) {
        return slot == 1;
    }

    public Counter getBurner() {
        return this.burner;
    }

    public boolean isBurning() {
        return this.burning;
    }

}
