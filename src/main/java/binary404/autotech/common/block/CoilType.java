package binary404.autotech.common.block;

import net.minecraft.util.IStringSerializable;

public enum CoilType implements IStringSerializable {
    CUPRONICKEL("cupronickel", 1800);

    private final String name;
    private final int coilTemperature;

    CoilType(String name, int coilTemperature) {
        this.name = name;
        this.coilTemperature = coilTemperature;
    }


    @Override
    public String getString() {
        return this.name;
    }

    public int getCoilTemperature() {
        return coilTemperature;
    }
}
