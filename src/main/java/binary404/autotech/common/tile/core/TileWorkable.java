package binary404.autotech.common.tile.core;

import binary404.autotech.client.renders.core.OrientedOverlayRenderer;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.logistics.fluid.FluidTankList;
import binary404.autotech.common.core.recipe.core.Recipe;
import binary404.autotech.common.core.recipe.machine.RecipeLogicEnergy;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashSet;
import java.util.Set;

public abstract class TileWorkable extends TileTiered {

    protected RecipeLogicEnergy workable;
    protected OrientedOverlayRenderer renderer;

    public TileWorkable(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public TileWorkable(TileEntityType<?> tileEntityType, RecipeMap<?> map, OrientedOverlayRenderer renderer, Tier tier) {
        super(tileEntityType, tier);
        this.workable = createWorkable(map);
        this.renderer = renderer;
    }

    protected RecipeLogicEnergy createWorkable(RecipeMap<?> recipeMap) {
        return new RecipeLogicEnergy(this, recipeMap, () -> energyStorage);
    }

    @Override
    public void renderTileEntity(CCRenderState state, IVertexOperation... pipeLine) {
        super.renderTileEntity(state, pipeLine);
        renderer.render(state, this.facing, workable != null && workable.isActive(), pipeLine);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.workable = createWorkable(RecipeMap.getByname(nbt.getString("RecipeMap")));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putString("RecipeMap", this.workable.recipeMap.getUnlocalizedName());
        return super.write(compound);
    }

    @Override
    protected IItemHandlerModifiable createImportItemHandler() {
        if (workable == null)
            return new ItemStackHandler(0);
        return new ItemStackHandler(workable.recipeMap.getMaxInputs());
    }

    @Override
    protected IItemHandlerModifiable createExportItemHandler() {
        if (workable == null)
            return new ItemStackHandler(0);
        return new ItemStackHandler(workable.recipeMap.getMaxOutputs());
    }

    @Override
    protected FluidTankList createImportFluidHandler() {
        if (workable == null)
            return new FluidTankList(false);
        FluidTank[] fluidImports = new FluidTank[workable.recipeMap.getMaxFluidInputs()];
        for (int i = 0; i < fluidImports.length; i++) {
            FluidTank filteredTank = new FluidTank(getInputTankCapacity(i));
            fluidImports[i] = filteredTank;
        }
        return new FluidTankList(false, fluidImports);
    }

    @Override
    protected FluidTankList createExportFluidHandler() {
        if (workable == null)
            return new FluidTankList(false);
        FluidTank[] fluidExports = new FluidTank[workable.recipeMap.getMaxFluidOutputs()];
        for (int i = 0; i < fluidExports.length; i++) {
            fluidExports[i] = new FluidTank(getOutputTankCapacity(i));
        }
        return new FluidTankList(false, fluidExports);
    }

    protected boolean canInputFluid(FluidStack inputFluid) {
        RecipeMap<?> recipeMap = workable.recipeMap;
        if (recipeMap.canInputFluidForce(inputFluid.getFluid()))
            return true;
        Set<Recipe> matchingRecipes = null;
        for (IFluidTank fluidTank : importFluids) {
            FluidStack fluidInTank = fluidTank.getFluid();
            if (fluidInTank != null) {
                if (matchingRecipes == null) {
                    matchingRecipes = new HashSet<>(recipeMap.getRecipesForFluid(fluidInTank));
                } else {
                    matchingRecipes.removeIf(recipe -> !recipe.hasInputFluid(fluidInTank));
                }
            }
        }
        if (matchingRecipes == null) {
            return !recipeMap.getRecipesForFluid(inputFluid).isEmpty();
        } else {
            return matchingRecipes.stream().anyMatch(recipe -> recipe.hasInputFluid(inputFluid));
        }
    }

    protected int getInputTankCapacity(int index) {
        return 64000;
    }

    protected int getOutputTankCapacity(int index) {
        return 64000;
    }


}
