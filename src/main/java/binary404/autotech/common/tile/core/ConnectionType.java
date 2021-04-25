package binary404.autotech.common.tile.core;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum ConnectionType implements IStringSerializable {

    NONE,
    IN,
    OUT,
    BOTH;


    @Override
    public String getString() {
        return name().toLowerCase(Locale.ROOT);
    }

    public boolean canReceive() {
        return this == IN || this == BOTH;
    }

    public boolean canExtract() {
        return this == OUT || this == BOTH;
    }
}
