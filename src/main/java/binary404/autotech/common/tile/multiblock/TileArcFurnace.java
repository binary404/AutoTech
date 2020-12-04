package binary404.autotech.common.tile.multiblock;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.block.multiblock.BlockBlastFurnace;
import binary404.autotech.common.block.multiblock.BlockHatch;
import binary404.autotech.common.core.lib.multiblock.*;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.ArcFurnaceManager;
import binary404.autotech.common.tile.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

public class TileArcFurnace extends MultiblockControllerBase<BlockBlastFurnace> {

    ArcFurnaceManager.ArcFurnaceRecipe recipe;

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
            MultiblockAbility.INPUT_ENERGY,
            MultiblockAbility.IMPORT_ITEMS,
            MultiblockAbility.EXPORT_ITEMS
    };

    public TileArcFurnace() {
        this(Tier.LV);
    }

    public TileArcFurnace(Tier tier) {
        super(ModTiles.arc_furnace, tier);
        this.inv.set(2);
    }

    @Override
    protected boolean canStart() {
        if (inv.getStackInSlot(0).isEmpty() || energy.getEnergyStored() <= 0)
            return false;

        this.recipe = ArcFurnaceManager.getRecipe(inv.getStackInSlot(0));

        if (recipe == null)
            return false;

        if (inv.getStackInSlot(0).getCount() < recipe.getInputCount())
            return false;

        return this.inv.getStackInSlot(1).isEmpty() || this.inv.getStackInSlot(1).getItem() == this.recipe.getOutput().getItem();
    }

    @Override
    protected boolean hasValidInput() {
        if (recipe == null)
            recipe = ArcFurnaceManager.getRecipe(inv.getStackInSlot(0));

        if (recipe == null)
            return false;

        return recipe.getInputCount() <= inv.getStackInSlot(0).getCount();
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
            recipe = ArcFurnaceManager.getRecipe(inv.getStackInSlot(0));

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
        return ModBlocks.heat_proof_casing.getDefaultState();
    }

    protected BlockState getCoilState() {
        return ModBlocks.basic_coil.getDefaultState();
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("ZZZ", "CCC", "CCC", "XXX")
                .aisle("ZXZ", "C#C", "C#C", "XXX")
                .aisle("ZYZ", "CCC", "CCC", "XXX")
                .where('X', statePredicate(getCasingState()))
                .where('Y', selfPredicate())
                .where('Z', abilityPartPredicate(ALLOWED_ABILITIES).or(statePredicate(getCasingState())))
                .where('#', isAirPredicate())
                .where('C', statePredicate(getCoilState()))
                .build();
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return slot == 0 && ArcFurnaceManager.recipeExists(stack);
    }

    @Override
    public boolean canExtract(int slot) {
        return slot != 0;
    }
}
