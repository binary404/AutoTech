package binary404.autotech.common.core.plugin.jei.machine;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.manager.AssemblerManager;
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

public class AssemblerRecipeCategory implements IRecipeCategory<AssemblerManager.AssemblerRecipe> {

    public static final ResourceLocation UID = AutoTech.key("assembler");
    public IDrawableStatic background;
    public IDrawable icon;
    public String localizedName;

    public AssemblerRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = AutoTech.key("textures/gui/container/assembler.png");
        background = guiHelper.createDrawable(location, 25, 0, 151, 74);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.mv_assembler));
        localizedName = I18n.format("autotech.assembler");
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends AssemblerManager.AssemblerRecipe> getRecipeClass() {
        return AssemblerManager.AssemblerRecipe.class;
    }

    public static List<AssemblerManager.AssemblerRecipe> getRecipes() {
        List<AssemblerManager.AssemblerRecipe> recipes = new ArrayList<>();

        for (AssemblerManager.AssemblerRecipe recipe : AssemblerManager.getRecipeList()) {
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
    public void setIngredients(AssemblerManager.AssemblerRecipe assemblerRecipe, IIngredients iIngredients) {
        ImmutableList.Builder<List<ItemStack>> inputBuilder = ImmutableList.builder();
        ImmutableList.Builder<ItemStack> outputBuilders = ImmutableList.builder();

        for (ItemStack stack : assemblerRecipe.getInput()) {
            if (!stack.isEmpty()) {
                inputBuilder.add(Lists.newArrayList(stack));
            }
        }

        if (!assemblerRecipe.getOutput().isEmpty())
            outputBuilders.add(assemblerRecipe.getOutput());

        iIngredients.setInputLists(VanillaTypes.ITEM, inputBuilder.build());
        iIngredients.setOutputLists(VanillaTypes.ITEM, ImmutableList.of(outputBuilders.build()));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, AssemblerManager.AssemblerRecipe centrifugeRecipe, IIngredients iIngredients) {
        List<ItemStack> outputs = iIngredients.getOutputs(VanillaTypes.ITEM).get(0);

        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                guiItemStacks.init(j + i * 3, true, 11 + j * 18, 4 + i * 18);
            }
        }
        guiItemStacks.init(10, false, 119, 22);

        for (int i = 0; i < centrifugeRecipe.getInput().size(); i++) {
            if (!centrifugeRecipe.getInput().get(i).isEmpty()) {
                guiItemStacks.set(i, centrifugeRecipe.getInput().get(i));
            }
        }

        guiItemStacks.set(10, outputs.get(0));

        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex == 0) {
                tooltip.add(new StringTextComponent(I18n.format("info.autotech.power") + ":" + centrifugeRecipe.getEnergy()));
                tooltip.add(new StringTextComponent(I18n.format("info.autotech.min_tier") + ":" + centrifugeRecipe.getMinTier().name()));
                tooltip.add(new StringTextComponent(I18n.format("info.autotech.requires") + ":" + "1000mb " + I18n.format("autotech.distilled_water")));
            }
        });
    }

}
