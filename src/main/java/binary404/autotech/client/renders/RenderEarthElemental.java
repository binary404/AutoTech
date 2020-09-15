package binary404.autotech.client.renders;

import binary404.autotech.client.models.ModelEarthElemental;
import binary404.autotech.common.entity.EarthElemental;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderEarthElemental extends MobRenderer<EarthElemental, ModelEarthElemental> {

    public RenderEarthElemental(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelEarthElemental(), 1.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(EarthElemental entity) {
        return new ResourceLocation("autotech", "texture");
    }
}
