package binary404.autotech.common.tile.core;

import binary404.autotech.client.gui.GuiTextures;
import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.client.gui.core.widget.ImageWidget;
import binary404.autotech.client.gui.core.widget.LabelWidget;
import binary404.autotech.client.renders.core.OrientedOverlayRenderer;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.logistics.fluid.FluidTankList;
import binary404.autotech.common.core.recipe.RecipeMaps;
import binary404.autotech.common.core.recipe.machine.SmeltingRecipeLogic;
import binary404.autotech.common.tile.ModTiles;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class TileFurnace extends TileTiered {

    protected SmeltingRecipeLogic workable;
    protected OrientedOverlayRenderer renderer;

    public TileFurnace() {
        super(ModTiles.simple_furnace, Tier.LV);
    }

    public TileFurnace(Tier tier) {
        super(ModTiles.simple_furnace, tier);
        this.workable = createWorkable();
        this.renderer = Textures.FURNACE;
        initializeInventory();
        reinitializeEnergyContainer();
    }

    protected SmeltingRecipeLogic createWorkable() {
        return new SmeltingRecipeLogic(this, RecipeMaps.SMELTING_RECIPES, () -> energyStorage);
    }

    @Override
    public void renderTileEntity(CCRenderState state, IVertexOperation... pipeLine) {
        super.renderTileEntity(state, pipeLine);
        renderer.render(state, this.facing, workable != null && workable.isActive(), pipeLine);
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

    protected int getInputTankCapacity(int index) {
        return 64000;
    }

    protected int getOutputTankCapacity(int index) {
        return 64000;
    }

    protected ModularUserInterface.Builder createGuiTemplate(PlayerEntity player) {
        ModularUserInterface.Builder builder = workable.recipeMap.createUITemplate(workable::getProgressPercent, importItems, exportItems, importFluids, exportFluids)
                .widget(new LabelWidget(5, 5, getFullName()))
                .widget(new ImageWidget(79, 42, 18, 18, GuiTextures.INDICATOR_NO_ENERGY)
                        .setPredicate(workable::isHasNotEnoughEnergy))
                .bindPlayerInventory(player.inventory);

        return builder;
    }

    @Override
    public ModularUserInterface createUI(PlayerEntity playerEntity) {
        return createGuiTemplate(playerEntity).build(this, playerEntity);
    }

}
