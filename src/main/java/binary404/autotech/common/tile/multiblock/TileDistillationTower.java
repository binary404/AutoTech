package binary404.autotech.common.tile.multiblock;

import binary404.autotech.client.renders.core.ICubeRenderer;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.lib.multiblock.*;
import binary404.autotech.common.core.recipe.RecipeMaps;
import binary404.autotech.common.tile.ModTiles;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.function.Predicate;

public class TileDistillationTower extends MultiblockControllerRecipe {

    public TileDistillationTower() {
        super(ModTiles.distillation_tower, RecipeMaps.DISTILLATION_RECIPES);
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        if (isStructureFormed()) {
            FluidStack stackInTank = importFluids.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
            if (stackInTank != null && stackInTank.getAmount() > 0 && !stackInTank.isEmpty()) {
                TranslationTextComponent fluidName = new TranslationTextComponent(stackInTank.getFluid().getAttributes().getTranslationKey());
                textList.add(new TranslationTextComponent("autotech.multiblock.distillation_tower.fluid", fluidName));
            }
        }
        super.addDisplayText(textList);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        Predicate<BlockWorldState> fluidExportPredicate = countMatch("HatchesAmount", abilityPartPredicate(MultiblockAbility.EXPORT_FLUIDS));
        Predicate<PatternMatchContext> exactlyOneHatch = context -> context.getInt("HatchesAmount") == 1;
        return FactoryBlockPattern.start(BlockPattern.RelativeDirection.RIGHT, BlockPattern.RelativeDirection.FRONT, BlockPattern.RelativeDirection.UP)
                .aisle("YSY", "YZY", "YYY")
                .aisle("XXX", "X#X", "XXX").setRepeatable(0, 12)
                .aisle("XXX", "XXX", "XXX")
                .where('S', selfPredicate())
                .where('Z', abilityPartPredicate(MultiblockAbility.IMPORT_FLUIDS))
                .where('Y', statePredicate(ModBlocks.stainless_steel_casing.getDefaultState()).or(abilityPartPredicate(MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.INPUT_ENERGY)))
                .where('X', fluidExportPredicate.or(statePredicate(ModBlocks.stainless_steel_casing.getDefaultState())))
                .where('#', isAirPredicate())
                .validateLayer(1, exactlyOneHatch)
                .validateLayer(2, exactlyOneHatch)
                .build();
    }

    @Override
    protected boolean allowSameFluidFillForOutputs() {
        return false;
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return Textures.STAINLESS_STEEL_CASING;
    }
}
