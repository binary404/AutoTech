package binary404.autotech.common.tile.core;

import binary404.autotech.client.gui.GuiTextures;
import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.client.gui.core.widget.ImageWidget;
import binary404.autotech.client.gui.core.widget.LabelWidget;
import binary404.autotech.client.renders.core.OrientedOverlayRenderer;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.recipe.RecipeMaps;
import binary404.autotech.common.core.recipe.map.RecipeMap;
import binary404.autotech.common.tile.ModTiles;
import net.minecraft.entity.player.PlayerEntity;

public class TileSimpleMachine extends TileWorkable {

    public TileSimpleMachine() {
        this(RecipeMaps.EMPTY, Textures.GRINDER, Tier.LV);
    }

    public TileSimpleMachine(OrientedOverlayRenderer renderer, Tier tier) {
        this(RecipeMaps.EMPTY, renderer, tier);
    }

    public TileSimpleMachine(RecipeMap<?> recipeMap, OrientedOverlayRenderer renderer, Tier tier) {
        super(ModTiles.simple_machine, recipeMap, renderer, tier);
        initializeInventory();
        reinitializeEnergyContainer();
    }

    @Override
    public void tick() {
        super.tick();
    }

    protected ModularUserInterface.Builder createGuiTemplate(PlayerEntity player) {
        ModularUserInterface.Builder builder = workable.recipeMap.createUITemplate(workable::getProgressPercent, importItems, exportItems, importFluids, exportFluids)
                .widget(new LabelWidget(5, 5, getFullName()))
                .widget(new ImageWidget(79, 42, 18, 18, GuiTextures.INDICATOR_NO_ENERGY)
                        .setPredicate(workable::isHasNotEnoughEnergy))
                .bindPlayerInventory(player.inventory);

        return builder;
    }

    @Override
    public ModularUserInterface createUI(PlayerEntity playerEntity) {
        return createGuiTemplate(playerEntity).build(this, playerEntity);
    }
}
