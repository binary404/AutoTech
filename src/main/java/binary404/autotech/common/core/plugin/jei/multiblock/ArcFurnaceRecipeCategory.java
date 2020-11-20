package binary404.autotech.common.core.plugin.jei.multiblock;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.manager.ArcFurnaceManager;
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

public class ArcFurnaceRecipeCategory implements IRecipeCategory<ArcFurnaceManager.ArcFurnaceRecipe> {

    public static final ResourceLocation UID = AutoTech.key("arc_furnace");
    public IDrawableStatic background;
    public IDrawable icon;
    public String localizedName;

    public ArcFurnaceRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = AutoTech.key("textures/gui/container/lv_grinder.png");
        background = guiHelper.createDrawable(location, 25, 0, 151, 74);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.lv_arc_furnace));
        localizedName = I18n.format("autotech.arc_furnace");
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends ArcFurnaceManager.ArcFurnaceRecipe> getRecipeClass() {
        return ArcFurnaceManager.ArcFurnaceRecipe.class;
    }

    public static List<ArcFurnaceManager.ArcFurnaceRecipe> getRecipes() {
        List<ArcFurnaceManager.ArcFurnaceRecipe> recipes = new ArrayList<>();

        for (ArcFurnaceManager.ArcFurnaceRecipe recipe : ArcFurnaceManager.getRecipeList()) {
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
    public void setIngredients(ArcFurnaceManager.ArcFurnaceRecipe centrifugeRecipe, IIngredients iIngredients) {
        ImmutableList.Builder<List<ItemStack>> inputBuilder = ImmutableList.builder();
        ImmutableList.Builder<ItemStack> outputBuilders = ImmutableList.builder();

        inputBuilder.add(Lists.newArrayList(centrifugeRecipe.getInput()));

        if (!centrifugeRecipe.getOutput().isEmpty())
            outputBuilders.add(centrifugeRecipe.getOutput());

        iIngredients.setInputLists(VanillaTypes.ITEM, inputBuilder.build());
        iIngredients.setOutputLists(VanillaTypes.ITEM, ImmutableList.of(outputBuilders.build()));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, ArcFurnaceManager.ArcFurnaceRecipe centrifugeRecipe, IIngredients iIngredients) {
        List<List<ItemStack>> inputs = iIngredients.getInputs(VanillaTypes.ITEM);
        List<ItemStack> outputs = iIngredients.getOutputs(VanillaTypes.ITEM).get(0);

        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 27, 22);
        guiItemStacks.init(1, false, 71, 22);

        guiItemStacks.set(0, inputs.get(0));
        guiItemStacks.set(1, outputs.get(0));

        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex == 0) {
                tooltip.add(new StringTextComponent(I18n.format("info.autotech.power") + ":" + centrifugeRecipe.getEnergy()));
            }
        });
    }

}
