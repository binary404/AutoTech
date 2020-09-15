package binary404.autotech.common.entity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import static binary404.autotech.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = "autotech", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final EntityType<EarthElemental> EARTH_ELEMENTAL = EntityType.Builder.<EarthElemental>create(
            EarthElemental::new, EntityClassification.MONSTER)
            .size(0.25F, 0.25F)
            .setUpdateInterval(1)
            .setTrackingRange(32)
            .setShouldReceiveVelocityUpdates(true)
            .build("");

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        IForgeRegistry<EntityType<?>> r = event.getRegistry();
        register(r, EARTH_ELEMENTAL, "earth_elemental");
    }

    public static void registerAttributes() {
        GlobalEntityTypeAttributes.put(ModEntities.EARTH_ELEMENTAL, MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 100F).create());
    }

}
