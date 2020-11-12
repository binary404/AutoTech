package binary404.autotech.common.tile.multiblock;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.block.multiblock.BlockBlastFurnace;
import binary404.autotech.common.core.lib.multiblock.BlockPattern;
import binary404.autotech.common.core.lib.multiblock.FactoryBlockPattern;
import binary404.autotech.common.core.lib.multiblock.MultiblockControllerBase;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.BlastFurnaceManager;
import binary404.autotech.common.tile.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;

public class TileBlastFurnace extends MultiblockControllerBase<BlockBlastFurnace> {

    BlastFurnaceManager.BlastFurnaceRecipe recipe;

    public TileBlastFurnace() {
        super(ModTiles.blast_furnace, Tier.LV);
        this.inv.set(2);
    }

    @Override
    protected boolean canStart() {
        if (inv.getStackInSlot(0).isEmpty() || energy.getEnergyStored() <= 0)
            return false;

        this.recipe = BlastFurnaceManager.getRecipe(inv.getStackInSlot(0));

        if (recipe == null)
            return false;

        if (inv.getStackInSlot(0).getCount() < recipe.getInput().getCount())
            return false;

        return this.inv.getStackInSlot(1).isEmpty() || this.inv.getStackInSlot(1).getItem() == this.recipe.getOutput().getItem();
    }

    @Override
    protected boolean hasValidInput() {
        if (recipe == null)
            recipe = BlastFurnaceManager.getRecipe(inv.getStackInSlot(0));

        if (recipe == null)
            return false;

        return recipe.getInput().getCount() <= inv.getStackInSlot(0).getCount();
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
        if (recipe == null)
            recipe = BlastFurnaceManager.getRecipe(inv.getStackInSlot(0));

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
    protected void updateFormedValid() {

    }

    protected BlockState getCasingState() {
        return ModBlocks.iron_plating.getDefaultState();
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXX", "XZX", "XXX")
                .aisle("XZX", "Z#Z", "XZX")
                .aisle("XXX", "XYX", "XXX")
                .where('X', statePredicate(getCasingState()))
                .where('Y', selfPredicate())
                .where('Z', tilePredicate((state, tile) -> tile instanceof TileBlastFurnaceHatch).or(statePredicate(getCasingState())))
                .where('#', isAirPredicate())
                .build();
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return slot == 0 && BlastFurnaceManager.recipeExists(stack);
    }

    @Override
    public boolean canExtract(int slot) {
        return slot != 0;
    }
}
