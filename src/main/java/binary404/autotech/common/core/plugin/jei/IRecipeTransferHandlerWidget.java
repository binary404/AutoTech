package binary404.autotech.common.core.plugin.jei;

import binary404.autotech.common.container.core.ModularContainer;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Map;

public interface IRecipeTransferHandlerWidget {

    String transferRecipe(ModularContainer container, Map<Integer, IGuiIngredient<ItemStack>> ingredients, PlayerEntity player, boolean maxTransfer, boolean doTransfer);

}
