package binary404.autotech.common.core.plugin.jei;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.machine.BlockMachine;
import binary404.autotech.common.core.recipe.RecipeMaps;
import binary404.autotech.common.core.recipe.core.Recipe;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class AutoTechJeiPlugin implements IModPlugin {

    private static final ResourceLocation ID = AutoTech.key("main");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        for(RecipeMap<?> recipeMap : RecipeMap.getRecipeMaps()) {
            registration.addRecipeCategories(new RecipeMapCategory(recipeMap, registration.getJeiHelpers().getGuiHelper()));
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        for(RecipeMap<?> recipeMap : RecipeMap.getRecipeMaps()) {
            List<Recipe> recipes = recipeMap.getRecipeList()
                    .stream().filter(recipe -> !recipe.isHidden() && recipe.hasValidInputsForDisplay())
                    .collect(Collectors.toList());
            registration.addRecipes(recipes, new ResourceLocation(AutoTech.modid, recipeMap.unlocalizedName));
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for(BlockMachine machine : BlockMachine.machines) {
            RecipeMap<?> recipeMap = machine.getRecipeMap();
            registration.addRecipeCatalyst(new ItemStack(machine), new ResourceLocation(AutoTech.modid, recipeMap.unlocalizedName));
        }
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }
}
