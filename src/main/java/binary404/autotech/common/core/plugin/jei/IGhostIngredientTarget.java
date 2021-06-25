package binary404.autotech.common.core.plugin.jei;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler;

import java.util.List;

public interface IGhostIngredientTarget {

    List<IGhostIngredientHandler.Target<?>> getPhantomTargets(Object ingredient);

}
