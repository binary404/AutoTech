package binary404.autotech.common.core.plugin.jei.machine;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.manager.CompactorManager;
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
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public class CompactorRecipeCategory implements IRecipeCategory<CompactorManager.CompactorRecipe> {

    public static final ResourceLocation UID = AutoTech.key("compactor");
    public IDrawableStatic background;
    public IDrawable icon;
    public String localizedName;

    public CompactorRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = AutoTech.key("textures/gui/container/lv_grinder.png");
        background = guiHelper.createDrawable(location, 25, 0, 151, 74);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.lv_compactor));
        localizedName = I18n.format("autotech.compactor");
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends CompactorManager.CompactorRecipe> getRecipeClass() {
        return CompactorManager.CompactorRecipe.class;
    }

    public static List<CompactorManager.CompactorRecipe> getRecipes() {
        List<CompactorManager.CompactorRecipe> recipes = new ArrayList<>();

        for (CompactorManager.CompactorRecipe recipe : CompactorManager.getRecipeList()) {
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
    public void setIngredients(CompactorManager.CompactorRecipe compactorRecipe, IIngredients iIngredients) {
        ImmutableList.Builder<List<ItemStack>> inputBuilder = ImmutableList.builder();
        ImmutableList.Builder<ItemStack> outputBuilders = ImmutableList.builder();

        inputBuilder.add(Lists.newArrayList(compactorRecipe.getInput()));

        if (!compactorRecipe.getOutput().isEmpty())
            outputBuilders.add(compactorRecipe.getOutput());

        iIngredients.setInputLists(VanillaTypes.ITEM, inputBuilder.build());
        iIngredients.setOutputLists(VanillaTypes.ITEM, ImmutableList.of(outputBuilders.build()));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, CompactorManager.CompactorRecipe compactorRecipe, IIngredients iIngredients) {
        List<List<ItemStack>> inputs = iIngredients.getInputs(VanillaTypes.ITEM);
        List<ItemStack> outputs = iIngredients.getOutputs(VanillaTypes.ITEM).get(0);

        IGuiItemStackGroup guiItemStackGroup = iRecipeLayout.getItemStacks();

        guiItemStackGroup.init(0, true, 27, 22);
        guiItemStackGroup.init(1, false, 71, 22);

        guiItemStackGroup.set(0, inputs.get(0));
        guiItemStackGroup.set(1, outputs.get(0));

        guiItemStackGroup.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex == 0) {
                tooltip.add(new StringTextComponent(I18n.format("info.autotech.power") + ":" + compactorRecipe.getEnergy()));
                tooltip.add(new StringTextComponent(I18n.format("info.autotech.min_tier") + ":" + compactorRecipe.getMinTier().name()));
            }
        });
    }
}
