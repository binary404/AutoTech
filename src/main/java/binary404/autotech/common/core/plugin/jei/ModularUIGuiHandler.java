package binary404.autotech.common.core.plugin.jei;

import binary404.autotech.client.gui.ModularGui;
import binary404.autotech.client.gui.core.widget.Widget;
import binary404.autotech.common.container.core.ModularContainer;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.*;

public class ModularUIGuiHandler implements IGhostIngredientHandler<ModularGui>, IRecipeTransferHandler<ModularContainer> {

    private final IRecipeTransferHandlerHelper transferHelper;

    public ModularUIGuiHandler(IRecipeTransferHandlerHelper transferHelper) {
        this.transferHelper = transferHelper;
    }

    @Override
    public Class<ModularContainer> getContainerClass() {
        return ModularContainer.class;
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(ModularContainer container, Object recipe, IRecipeLayout recipeLayout, PlayerEntity player, boolean maxTransfer, boolean doTransfer) {
        Optional<IRecipeTransferHandlerWidget> transferHandler = container.getModularUI()
                .getFlatVisibleWidgetCollection().stream()
                .filter(it -> it instanceof IRecipeTransferHandlerWidget)
                .map(it -> (IRecipeTransferHandlerWidget) it)
                .findFirst();
        if (!transferHandler.isPresent()) {
            return transferHelper.createInternalError();
        }
        Map<Integer, IGuiIngredient<ItemStack>> group = new HashMap<>(recipeLayout.getItemStacks().getGuiIngredients());
        group.values().removeIf(it -> it.getAllIngredients().isEmpty());
        String errorTooltip = transferHandler.get().transferRecipe(container, group, player, maxTransfer, doTransfer);
        if (errorTooltip == null) {
            return null;
        }
        return transferHelper.createUserErrorWithTooltip(errorTooltip);
    }

    @Override
    public <I> List<Target<I>> getTargets(ModularGui modularGui, I i, boolean b) {
        Collection<Widget> widgets = modularGui.getModularUI().guiWidgets.values();
        List<Target<I>> targets = new ArrayList<>();
        for (Widget widget : widgets) {
            if (widget instanceof IGhostIngredientTarget) {
                IGhostIngredientTarget ghostTarget = (IGhostIngredientTarget) widget;
                List<Target<?>> widgetTargets = ghostTarget.getPhantomTargets(i);
                //noinspection unchecked
                targets.addAll((List<Target<I>>) (Object) widgetTargets);
            }
        }
        return targets;
    }

    @Override
    public void onComplete() {

    }
}
