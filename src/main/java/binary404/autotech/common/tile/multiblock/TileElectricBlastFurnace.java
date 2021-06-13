package binary404.autotech.common.tile.multiblock;

import binary404.autotech.client.renders.core.ICubeRenderer;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.block.CoilType;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.block.misc.BlockCoil;
import binary404.autotech.common.core.lib.multiblock.*;
import binary404.autotech.common.core.recipe.RecipeMaps;
import binary404.autotech.common.core.recipe.core.Recipe;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import binary404.autotech.common.tile.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;

import java.util.function.Predicate;

public class TileElectricBlastFurnace extends MultiblockControllerRecipe {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
            MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.IMPORT_FLUIDS,
            MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.EXPORT_FLUIDS,
            MultiblockAbility.INPUT_ENERGY
    };

    private int blastFurnaceTemp;

    public TileElectricBlastFurnace() {
        super(ModTiles.blast_furnace, RecipeMaps.BLAST_RECIPES);
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        this.blastFurnaceTemp = context.getOrDefault("CoilType", CoilType.CUPRONICKEL).getCoilTemperature();
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.blastFurnaceTemp = 0;
    }

    @Override
    public boolean checkRecipe(Recipe recipe, boolean consumeIfSuccess) {
        int recipeRequiredTemp = recipe.getIntegerProperty("blast_furnace_temperature");
        return this.blastFurnaceTemp >= recipeRequiredTemp;
    }

    public static Predicate<BlockWorldState> heatingCoilPredicate() {
        return blockWorldState -> {
            BlockState state = blockWorldState.getBlockState();
            if (!(state.getBlock() instanceof BlockCoil))
                return false;
            BlockCoil coil = (BlockCoil) state.getBlock();
            CoilType coilType = coil.getType();
            CoilType currentCoilType = blockWorldState.getMatchContext().getOrPut("CoilType", coilType);
            return currentCoilType.getString().equals(coilType.getString());
        };
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXX", "CCC", "CCC", "XXX")
                .aisle("XXX", "C#C", "C#C", "XXX")
                .aisle("XSX", "CCC", "CCC", "XXX")
                .setAmountAtLeast('L', 10)
                .where('S', selfPredicate())
                .where('L', statePredicate(ModBlocks.heat_proof_casing.getDefaultState()))
                .where('X', statePredicate(ModBlocks.heat_proof_casing.getDefaultState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
                .where('C', heatingCoilPredicate())
                .where('#', isAirPredicate())
                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return Textures.HEAT_PROOF_CASING;
    }
}
