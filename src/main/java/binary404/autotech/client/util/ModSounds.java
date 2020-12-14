package binary404.autotech.client.util;

import binary404.autotech.AutoTech;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = AutoTech.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSounds {

    public static final SoundEvent piston = makeSoundEvent("piston");
    public static final SoundEvent energy_wave = makeSoundEvent("energy_wave");

    public static SoundEvent makeSoundEvent(String name) {
        ResourceLocation sound = AutoTech.key(name);
        return new SoundEvent(sound).setRegistryName(sound);
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> r = event.getRegistry();
        r.register(piston);
        r.register(energy_wave);
    }

}
