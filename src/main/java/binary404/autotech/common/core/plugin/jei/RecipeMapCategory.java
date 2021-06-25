package binary404.autotech.common.core.plugin.jei;

import binary404.autotech.AutoTech;
import binary404.autotech.client.gui.BlankUIHolder;
import binary404.autotech.client.gui.core.IRenderContext;
import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.client.gui.core.widget.SlotWidget;
import binary404.autotech.client.gui.core.widget.TankWidget;
import binary404.autotech.client.gui.core.widget.Widget;
import binary404.autotech.common.block.machine.BlockMachine;
import binary404.autotech.common.core.logistics.fluid.FluidTankList;
import binary404.autotech.common.core.recipe.core.CountableIngredient;
import binary404.autotech.common.core.recipe.core.Recipe;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import binary404.autotech.common.item.ModItems;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecipeMapCategory implements IRecipeCategory<Recipe> {

    private final RecipeMap<?> recipeMap;
    private final ModularUserInterface modularUI;
    private ItemStackHandler importItems, exportItems;
    private FluidTankList importFluids, exportFluids;
    private final IDrawable backgroundDrawable;
    private final IDrawable icon;

    public RecipeMapCategory(RecipeMap<?> recipeMap, IGuiHelper guiHelper) {
        this.recipeMap = recipeMap;
        FluidTank[] importFluidTanks = new FluidTank[recipeMap.getMaxFluidInputs()];
        for (int i = 0; i < importFluidTanks.length; i++)
            importFluidTanks[i] = new FluidTank(16000);
        FluidTank[] exportFluidTanks = new FluidTank[recipeMap.getMaxFluidOutputs()];
        for (int i = 0; i < exportFluidTanks.length; i++)
            exportFluidTanks[i] = new FluidTank(16000);
        this.modularUI = recipeMap.createJeiUITemplate(
                (importItems = new ItemStackHandler(recipeMap.getMaxInputs())),
                (exportItems = new ItemStackHandler(recipeMap.getMaxOutputs())),
                (importFluids = new FluidTankList(false, importFluidTanks)),
                (exportFluids = new FluidTankList(false, exportFluidTanks))
        ).build(new BlankUIHolder(), Minecraft.getInstance().player);
        this.modularUI.initWidgets();
        this.backgroundDrawable = guiHelper.createBlankDrawable(modularUI.getWidth(), modularUI.getHeight() * 2 / 3);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(BlockMachine.machineMap.get(recipeMap)));
    }


    @Override
    public String getTitle() {
        return recipeMap.getLocalizedName();
    }

    @Override
    public IDrawable getBackground() {
        return backgroundDrawable;
    }

    @Override
    public void setIngredients(Recipe recipe, IIngredients iIngredients) {
        List<ItemStack> inputs = new ArrayList<>();
        for (CountableIngredient ingredient : recipe.getInputs()) {
            List<ItemStack> allStacks = new ArrayList<>();
            allStacks.addAll(Arrays.asList(ingredient.getIngredient().getMatchingStacks()));
            for (ItemStack stack : allStacks) {
                inputs.add(new ItemStack(stack.getItem(), ingredient.getCount()));
            }
        }
        List<ItemStack> output = recipe.getOutputs();

        iIngredients.setInputs(VanillaTypes.ITEM, inputs);
        iIngredients.setOutputs(VanillaTypes.ITEM, output);

        iIngredients.setInputs(VanillaTypes.FLUID, recipe.getFluidInputs());
        iIngredients.setOutputs(VanillaTypes.FLUID, recipe.getFluidOutputs());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, Recipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
        IGuiFluidStackGroup fluidStackGroup = recipeLayout.getFluidStacks();
        for (Widget uiWidget : modularUI.guiWidgets.values()) {

            if (uiWidget instanceof SlotWidget) {
                SlotWidget slotWidget = (SlotWidget) uiWidget;
                if (!(slotWidget.getHandle() instanceof SlotItemHandler)) {
                    continue;
                }
                SlotItemHandler handle = (SlotItemHandler) slotWidget.getHandle();
                if (handle.getItemHandler() == importItems) {
                    //this is input item stack slot widget, so add it to item group
                    itemStackGroup.init(handle.getSlotIndex(), true,
                            slotWidget.getPosition().x,
                            slotWidget.getPosition().y);
                } else if (handle.getItemHandler() == exportItems) {
                    //this is output item stack slot widget, so add it to item group
                    itemStackGroup.init(importItems.getSlots() + handle.getSlotIndex(), false,
                            slotWidget.getPosition().x,
                            slotWidget.getPosition().y);
                }
            } else if (uiWidget instanceof TankWidget) {
                TankWidget tankWidget = (TankWidget) uiWidget;
                if (importFluids.getFluidTanks().contains(tankWidget.fluidTank)) {
                    int importIndex = importFluids.getFluidTanks().indexOf(tankWidget.fluidTank);
                    List<List<FluidStack>> inputsList = ingredients.getInputs(VanillaTypes.FLUID);
                    int fluidAmount = 0;
                    if (inputsList.size() > importIndex && !inputsList.get(importIndex).isEmpty())
                        fluidAmount = inputsList.get(importIndex).get(0).getAmount();
                    //this is input tank widget, so add it to fluid group
                    fluidStackGroup.init(importIndex, true,
                            tankWidget.getPosition().x + tankWidget.fluidRenderOffset,
                            tankWidget.getPosition().y + tankWidget.fluidRenderOffset,
                            tankWidget.getSize().width - (2 * tankWidget.fluidRenderOffset),
                            tankWidget.getSize().height - (2 * tankWidget.fluidRenderOffset),
                            fluidAmount, false, null);

                } else if (exportFluids.getFluidTanks().contains(tankWidget.fluidTank)) {
                    int exportIndex = exportFluids.getFluidTanks().indexOf(tankWidget.fluidTank);
                    List<List<FluidStack>> inputsList = ingredients.getOutputs(VanillaTypes.FLUID);
                    int fluidAmount = 0;
                    if (inputsList.size() > exportIndex && !inputsList.get(exportIndex).isEmpty())
                        fluidAmount = inputsList.get(exportIndex).get(0).getAmount();
                    //this is output tank widget, so add it to fluid group
                    fluidStackGroup.init(importFluids.getFluidTanks().size() + exportIndex, false,
                            tankWidget.getPosition().x + tankWidget.fluidRenderOffset,
                            tankWidget.getPosition().y + tankWidget.fluidRenderOffset,
                            tankWidget.getSize().width - (2 * tankWidget.fluidRenderOffset),
                            tankWidget.getSize().height - (2 * tankWidget.fluidRenderOffset),
                            fluidAmount, false, null);

                }
            }
        }
        itemStackGroup.set(ingredients);
        fluidStackGroup.set(ingredients);
    }

    @Override
    public void draw(Recipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        for (Widget widget : modularUI.guiWidgets.values()) {
            widget.drawInBackground(matrixStack, 0, 0, new IRenderContext() {
            });
            widget.drawInForeground(matrixStack, 0, 0);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(AutoTech.modid, recipeMap.getUnlocalizedName());
    }

    @Override
    public Class<? extends Recipe> getRecipeClass() {
        return Recipe.class;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }
}
