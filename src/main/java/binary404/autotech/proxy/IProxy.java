package binary404.autotech.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IProxy {

    default void registerEventHandlers() {
    }

    default void init() {

    }

    default PlayerEntity getClientPlayer() {
        return null;
    }

    default World getClientWorld() {
        return null;
    }

}
