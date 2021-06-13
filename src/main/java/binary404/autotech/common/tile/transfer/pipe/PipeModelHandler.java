package binary404.autotech.common.tile.transfer.pipe;

import binary404.autotech.AutoTech;
import binary404.autotech.common.tile.transfer.attachment.AttachmentFactory;
import binary404.autotech.common.tile.transfer.attachment.AttachmentRegistry;
import binary404.autotech.common.tile.transfer.fluid.FluidPipeType;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.http.auth.AUTH;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AutoTech.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PipeModelHandler {

    public static void init() {
        for (AttachmentFactory factory : AttachmentRegistry.INSTANCE.all()) {
            ModelLoader.addSpecialModel(factory.getModelLocation());
        }

        for (String type : new String[]{"item", "fluid"}) {
            ModelLoader.addSpecialModel(new ResourceLocation(AutoTech.modid + ":block/pipe/" + type + "/basic/core"));
            ModelLoader.addSpecialModel(new ResourceLocation(AutoTech.modid + ":block/pipe/" + type + "/basic/extension"));
            ModelLoader.addSpecialModel(new ResourceLocation(AutoTech.modid + ":block/pipe/" + type + "/basic/straight"));
        }

        ModelLoader.addSpecialModel(new ResourceLocation(AutoTech.modid + ":block/pipe/attachment/inventory_attachment"));
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        AutoTech.LOGGER.error("On Model Bake");
        Map<ResourceLocation, IBakedModel> attachmentModels = new HashMap<>();

        for (AttachmentFactory factory : AttachmentRegistry.INSTANCE.all()) {
            attachmentModels.put(factory.getId(), event.getModelRegistry().get(factory.getModelLocation()));
        }

        Map<ResourceLocation, PipeBakedModel> pipeModels = new HashMap<>();

        pipeModels.put(FluidPipeType.BASIC.getId(), new PipeBakedModel(
                event.getModelRegistry().get(new ResourceLocation(AutoTech.modid + ":block/pipe/fluid/basic/core")),
                event.getModelRegistry().get(new ResourceLocation(AutoTech.modid + ":block/pipe/fluid/basic/extension")),
                event.getModelRegistry().get(new ResourceLocation(AutoTech.modid + ":block/pipe/fluid/basic/straight")),
                event.getModelRegistry().get(new ResourceLocation(AutoTech.modid + ":block/pipe/attachment/inventory_attachment")),
                attachmentModels
        ));

        on:
        for (ResourceLocation id : event.getModelRegistry().keySet()) {
            for (Map.Entry<ResourceLocation, PipeBakedModel> entry : pipeModels.entrySet()) {
                if (isPipeModel(id, entry.getKey())) {
                    event.getModelRegistry().put(id, entry.getValue());
                    continue on;
                }
            }
        }
    }

    private static boolean isPipeModel(ResourceLocation modelId, ResourceLocation pipeId) {
        return modelId instanceof ModelResourceLocation
                && modelId.getNamespace().equals(AutoTech.modid)
                && modelId.getPath().equals(pipeId.getPath())
                && !((ModelResourceLocation) modelId).getVariant().equals("inventory");
    }

}
