package binary404.autotech.common.core.plugin.jei.machine;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.manager.CentrifugeManager;
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

public class CentrifugeRecipeCategory implements IRecipeCategory<CentrifugeManager.CentrifugeRecipe> {

    public static final ResourceLocation UID = AutoTech.key("centrifuge");
    public IDrawableStatic background;
    public IDrawable icon;
    public String localizedName;

    public CentrifugeRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = AutoTech.key("textures/gui/container/centrifuge.png");
        background = guiHelper.createDrawable(location, 25, 0, 151, 74);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.mv_centrifuge));
        localizedName = I18n.format("autotech.centrifuge");
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends CentrifugeManager.CentrifugeRecipe> getRecipeClass() {
        return CentrifugeManager.CentrifugeRecipe.class;
    }

    public static List<CentrifugeManager.CentrifugeRecipe> getRecipes() {
        List<CentrifugeManager.CentrifugeRecipe> recipes = new ArrayList<>();

        for (CentrifugeManager.CentrifugeRecipe recipe : CentrifugeManager.getRecipeList()) {
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
    public void setIngredients(CentrifugeManager.CentrifugeRecipe centrifugeRecipe, IIngredients iIngredients) {
        ImmutableList.Builder<List<ItemStack>> inputBuilder = ImmutableList.builder();
        ImmutableList.Builder<ItemStack> outputBuilders = ImmutableList.builder();

        inputBuilder.add(Lists.newArrayList(centrifugeRecipe.getInput()));

        if (!centrifugeRecipe.getOutput().isEmpty())
            outputBuilders.add(centrifugeRecipe.getOutput());

        if (!centrifugeRecipe.getOutput2().isEmpty())
            outputBuilders.add(centrifugeRecipe.getOutput2());

        iIngredients.setInputLists(VanillaTypes.ITEM, inputBuilder.build());
        iIngredients.setOutputLists(VanillaTypes.ITEM, ImmutableList.of(outputBuilders.build()));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, CentrifugeManager.CentrifugeRecipe centrifugeRecipe, IIngredients iIngredients) {
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
        }

        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex == 0) {
                tooltip.add(new StringTextComponent(I18n.format("info.autotech.power") + ":" + centrifugeRecipe.getEnergy()));
                tooltip.add(new StringTextComponent(I18n.format("info.autotech.min_tier") + ":" + centrifugeRecipe.getMinTier().name()));
                tooltip.add(new StringTextComponent(I18n.format("info.autotech.requires") + ":" + "1000mb " + I18n.format("autotech.distilled_water")));
            } else if (slotIndex == 2) {
                tooltip.add(new StringTextComponent(I18n.format("info.autotech.chance") + ":" + centrifugeRecipe.getSecondChance()));
            }
        });
    }
}
