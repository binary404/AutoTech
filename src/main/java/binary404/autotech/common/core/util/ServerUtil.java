package binary404.autotech.common.core.util;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public class ServerUtil {

    public static boolean hasPlayers() {
        return !get().getPlayerList().getPlayers().isEmpty();
    }

    public static boolean isSinglePlayer() {
        return !isMultiPlayer();
    }

    public static boolean isMultiPlayer() {
        return get().isDedicatedServer();
    }

    public static MinecraftServer get() {
        return LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
    }

}
