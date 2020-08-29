package binary404.autotech.common.core.plugin.jei;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.plugin.jei.machine.GrinderRecipeCategory;
import binary404.autotech.common.core.plugin.jei.machine.SawMillRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JeiPluginAutoTech implements IModPlugin {

    public static IJeiHelpers jeiHelpers;
    public static IGuiHelper guiHelper;
    public static IJeiRuntime jeiRuntime;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new GrinderRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SawMillRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(GrinderRecipeCategory.getRecipes(), GrinderRecipeCategory.UID);
        registration.addRecipes(SawMillRecipeCategory.getRecipes(), SawMillRecipeCategory.UID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.lv_grinder), GrinderRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.mv_grinder), GrinderRecipeCategory.UID);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.lv_sawmill), SawMillRecipeCategory.UID);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.lv_smelter), VanillaRecipeCategoryUid.FURNACE);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        JeiPluginAutoTech.jeiRuntime = jeiRuntime;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("autotech:plugin");
    }
}
