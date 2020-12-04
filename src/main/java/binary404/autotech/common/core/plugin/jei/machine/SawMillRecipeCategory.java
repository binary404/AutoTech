package binary404.autotech.common.core.plugin.jei.machine;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.manager.SawMillManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class SawMillRecipeCategory implements IRecipeCategory<SawMillManager.SawMillRecipe> {

    public static final ResourceLocation UID = AutoTech.key("sawmill");
    public IDrawableStatic background;
    public IDrawable icon;
    public String localizedName;

    public SawMillRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = AutoTech.key("textures/gui/container/sawmill.png");
        background = guiHelper.createDrawable(location, 25, 0, 151, 74);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.lv_grinder));
        localizedName = "Sawmill";
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends SawMillManager.SawMillRecipe> getRecipeClass() {
        return SawMillManager.SawMillRecipe.class;
    }

    public static List<SawMillManager.SawMillRecipe> getRecipes() {
        List<SawMillManager.SawMillRecipe> recipes = new ArrayList<>();

        for (SawMillManager.SawMillRecipe recipe : SawMillManager.getRecipeList()) {
            recipes.add(recipe);
        }
        return recipes;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(SawMillManager.SawMillRecipe grinderRecipe, IIngredients iIngredients) {
        ImmutableList.Builder<List<ItemStack>> inputBuilder = ImmutableList.builder();
        ImmutableList.Builder<ItemStack> outputBuilders = ImmutableList.builder();

        inputBuilder.add(Lists.newArrayList(grinderRecipe.getInput()));

        if (!grinderRecipe.getOutput().isEmpty())
            outputBuilders.add(grinderRecipe.getOutput());
        if (!grinderRecipe.getSecondaryOutput().isEmpty())
            outputBuilders.add(grinderRecipe.getSecondaryOutput());

        iIngredients.setInputLists(VanillaTypes.ITEM, inputBuilder.build());
        iIngredients.setOutputLists(VanillaTypes.ITEM, ImmutableList.of(outputBuilders.build()));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, SawMillManager.SawMillRecipe grinderRecipe, IIngredients iIngredients) {
        List<List<ItemStack>> inputs = iIngredients.getInputs(VanillaTypes.ITEM);
        List<ItemStack> outputs = iIngredients.getOutputs(VanillaTypes.ITEM).get(0);

        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 27, 22);
        guiItemStacks.init(1, false, 71, 22);

        guiItemStacks.set(0, inputs.get(0));
        guiItemStacks.set(1, outputs.get(0));

        if (outputs.size() > 1 && !outputs.get(1).isEmpty()) {
            guiItemStacks.init(2, false, 101, 22);
            guiItemStacks.set(2, outputs.get(1));

            if (outputs.size() > 2 && !outputs.get(2).isEmpty()) {
                guiItemStacks.init(3, false, 115, 22);
                guiItemStacks.set(3, outputs.get(2));
            }
        }
        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex == 0) {
                tooltip.add(new TranslationTextComponent("info.autotech.power" + ":" + grinderRecipe.getEnergy()));
            } else if (slotIndex == 2) {
                tooltip.add(new TranslationTextComponent("info.autotech.chance" + ":" + grinderRecipe.getSecondaryOutputChance()));
            }
        });
    }

}
