package binary404.autotech.common.tile.transfer;

import binary404.autotech.AutoTech;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = AutoTech.modid)
public class CableNetworkManager {

    private static final Collection<LazyOptional<CableNetwork>> NETWORK_LIST = Collections.synchronizedList(new ArrayList<>());

    private CableNetworkManager() {throw new IllegalAccessError("Utility class");}

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static CableNetwork get(IWorldReader world, BlockPos pos) {
        return getLazy(world, pos).orElse(null);
    }

    public static LazyOptional<CableNetwork> getLazy(IWorldReader world, BlockPos pos) {
        synchronized (NETWORK_LIST) {
            for (LazyOptional<CableNetwork> network : NETWORK_LIST) {
                if (network.isPresent()) {
                    CableNetwork net = network.orElseThrow(IllegalStateException::new);
                    if (net.contains(world, pos)) {
//                    SilentMechanisms.LOGGER.debug("get network {}", network);
                        return network;
                    }
                }
            }

            // Create new
            CableNetwork network = CableNetwork.buildNetwork(world, pos);
            LazyOptional<CableNetwork> lazy = LazyOptional.of(() -> network);
            NETWORK_LIST.add(lazy);
//            SilentMechanisms.LOGGER.debug("create network {}", network);
            return lazy;
        }
    }

    public static void invalidateNetwork(IWorldReader world, BlockPos pos) {
        Collection<LazyOptional<CableNetwork>> toRemove = NETWORK_LIST.stream()
                .filter(n -> n != null && n.isPresent() && n.orElseThrow(IllegalStateException::new).contains(world, pos))
                .collect(Collectors.toList());
        toRemove.forEach(CableNetworkManager::invalidateNetwork);
    }

    private static void invalidateNetwork(LazyOptional<CableNetwork> network) {
 //       SilentMechanisms.LOGGER.debug("invalidateNetwork {}", network);
        synchronized (NETWORK_LIST) {
            NETWORK_LIST.removeIf(n -> n.isPresent() && n.equals(network));
            network.ifPresent(CableNetwork::invalidate);
            network.invalidate();
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        // Send energy from wire networks to connected blocks
        synchronized (NETWORK_LIST) {
            NETWORK_LIST.stream()
                    .filter(n -> n != null && n.isPresent())
                    .forEach(n -> n.ifPresent(CableNetwork::sendEnergy));
        }
    }

}
