package binary404.autotech.common.tile;

import binary404.autotech.common.core.logistics.Redstone;

public interface IRedstoneInteract {

    Redstone getRedstoneMode();

    void setRedstoneMode(Redstone mode);

    default void nextRedstoneMode() {
        setRedstoneMode(getRedstoneMode().next());
    }

}
