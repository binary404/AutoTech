package binary404.autotech.common.core.plugin.jei;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.manager.CentrifugeManager;
import binary404.autotech.common.core.plugin.jei.machine.*;
import binary404.autotech.common.core.plugin.jei.multiblock.ArcFurnaceRecipeCategory;
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
        registration.addRecipeCategories(new CentrifugeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CompactorRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AssemblerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ArcFurnaceRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(GrinderRecipeCategory.getRecipes(), GrinderRecipeCategory.UID);
        registration.addRecipes(SawMillRecipeCategory.getRecipes(), SawMillRecipeCategory.UID);
        registration.addRecipes(CentrifugeRecipeCategory.getRecipes(), CentrifugeRecipeCategory.UID);
        registration.addRecipes(CompactorRecipeCategory.getRecipes(), CompactorRecipeCategory.UID);
        registration.addRecipes(AssemblerRecipeCategory.getRecipes(), AssemblerRecipeCategory.UID);
        registration.addRecipes(ArcFurnaceRecipeCategory.getRecipes(), ArcFurnaceRecipeCategory.UID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.lv_grinder), GrinderRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.mv_grinder), GrinderRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.hv_grinder), GrinderRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ev_grinder), GrinderRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.iv_grinder), GrinderRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.maxv_grinder), GrinderRecipeCategory.UID);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.lv_sawmill), SawMillRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.mv_sawmill), SawMillRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.hv_sawmill), SawMillRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ev_sawmill), SawMillRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.iv_sawmill), SawMillRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.maxv_sawmill), SawMillRecipeCategory.UID);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.lv_compactor), CompactorRecipeCategory.UID);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.mv_centrifuge), CentrifugeRecipeCategory.UID);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.mv_assembler), AssemblerRecipeCategory.UID);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.lv_arc_furnace), ArcFurnaceRecipeCategory.UID);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.lv_smelter), VanillaRecipeCategoryUid.FURNACE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.mv_smelter), VanillaRecipeCategoryUid.FURNACE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.hv_smelter), VanillaRecipeCategoryUid.FURNACE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ev_smelter), VanillaRecipeCategoryUid.FURNACE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.iv_smelter), VanillaRecipeCategoryUid.FURNACE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.maxv_smelter), VanillaRecipeCategoryUid.FURNACE);
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
