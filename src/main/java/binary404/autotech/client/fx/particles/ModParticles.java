package binary404.autotech.client.fx.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FireworkParticle;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static binary404.autotech.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticles {

    public static final ParticleType<BasicParticleType> SPARKLE = new SparkleParticleType();

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> event) {
        register(event.getRegistry(), SPARKLE, "sparkle");
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class FactoryHandler {
        @SubscribeEvent
        public static void registerFactories(ParticleFactoryRegisterEvent event) {
            Minecraft.getInstance().particles.registerFactory(ModParticles.SPARKLE, SparkleParticleType.Factory::new);
        }
    }

}
