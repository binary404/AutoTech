package binary404.autotech.common.core.plugin.jei.machine;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.manager.GrinderManager;
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

public class GrinderRecipeCategory implements IRecipeCategory<GrinderManager.GrinderRecipe> {

    public static final ResourceLocation UID = AutoTech.key("grinder");
    public IDrawableStatic background;
    public IDrawable icon;
    public String localizedName;

    public GrinderRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = AutoTech.key("textures/gui/container/grinder.png");
        background = guiHelper.createDrawable(location, 25, 0, 151, 74);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.lv_grinder));
        localizedName = "Grinder";
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends GrinderManager.GrinderRecipe> getRecipeClass() {
        return GrinderManager.GrinderRecipe.class;
    }

    public static List<GrinderManager.GrinderRecipe> getRecipes() {
        List<GrinderManager.GrinderRecipe> recipes = new ArrayList<>();

        for (GrinderManager.GrinderRecipe recipe : GrinderManager.getRecipeList()) {
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
    public void setIngredients(GrinderManager.GrinderRecipe grinderRecipe, IIngredients iIngredients) {
        ImmutableList.Builder<List<ItemStack>> inputBuilder = ImmutableList.builder();
        ImmutableList.Builder<ItemStack> outputBuilders = ImmutableList.builder();

        inputBuilder.add(Lists.newArrayList(grinderRecipe.getInput()));

        if (!grinderRecipe.getPrimaryOutput().isEmpty())
            outputBuilders.add(grinderRecipe.getPrimaryOutput());
        if (!grinderRecipe.getSecondaryOutput().isEmpty())
            outputBuilders.add(grinderRecipe.getSecondaryOutput());
        if (!grinderRecipe.getThirdOutput().isEmpty())
            outputBuilders.add(grinderRecipe.getThirdOutput());

        iIngredients.setInputLists(VanillaTypes.ITEM, inputBuilder.build());
        iIngredients.setOutputLists(VanillaTypes.ITEM, ImmutableList.of(outputBuilders.build()));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, GrinderManager.GrinderRecipe grinderRecipe, IIngredients iIngredients) {
        List<List<ItemStack>> inputs = iIngredients.getInputs(VanillaTypes.ITEM);
        List<ItemStack> outputs = iIngredients.getOutputs(VanillaTypes.ITEM).get(0);

        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 27, 22);
        guiItemStacks.init(1, false, 71, 22);

        guiItemStacks.set(0, inputs.get(0));
        guiItemStacks.set(1, outputs.get(0));

        if (outputs.size() > 1 && !outputs.get(1).isEmpty()) {
            guiItemStacks.init(2, false, 93, 22);
            guiItemStacks.set(2, outputs.get(1));

            if (outputs.size() > 2 && !outputs.get(2).isEmpty()) {
                guiItemStacks.init(3, false, 115, 22);
                guiItemStacks.set(3, outputs.get(2));
            }
        }
        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex == 0) {
                tooltip.add(new TranslationTextComponent("info.autotech.power" + ":" + grinderRecipe.getEnergy()));
                tooltip.add(new TranslationTextComponent("info.autotech.min_tier" + ":" + grinderRecipe.getMinTier().name()));
            } else if (slotIndex == 2) {
                tooltip.add(new TranslationTextComponent("info.autotech.chance" + ":" + grinderRecipe.getSecondaryChance()));
            } else if (slotIndex == 3) {
                tooltip.add(new TranslationTextComponent("info.autotech.chance" + ":" + grinderRecipe.getThirdChance()));
            }
        });
    }
}
