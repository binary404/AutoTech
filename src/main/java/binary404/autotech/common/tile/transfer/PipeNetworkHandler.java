package binary404.autotech.common.tile.transfer;

import binary404.autotech.AutoTech;
import binary404.autotech.common.tile.transfer.network.NetworkManager;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AutoTech.modid)
public class PipeNetworkHandler {

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (!event.world.isRemote && event.phase == TickEvent.Phase.END) {
            NetworkManager.get(event.world).getNetworks().forEach(n -> n.update(event.world));
        }
    }

}
