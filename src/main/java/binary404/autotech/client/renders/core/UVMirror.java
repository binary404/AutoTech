package binary404.autotech.client.renders.core;

import codechicken.lib.vec.uv.UV;
import codechicken.lib.vec.uv.UVTransformation;

public class UVMirror extends UVTransformation {

    public double minU;
    public double maxU;
    public double minV;
    public double maxV;

    public UVMirror(double minU, double maxU, double minV, double maxV) {
        this.minU = minU;
        this.maxU = maxU;
        this.minV = minV;
        this.maxV = maxV;
    }

    @Override
    public void apply(UV uv) {
        if (uv.u == minU) {
            uv.u = maxU;
        } else if (uv.u == maxU) {
            uv.u = minU;
        }
        if (uv.v == minV) {
            uv.v = maxV;
        } else if (uv.v == maxV) {
            uv.v = minV;
        }
    }

    @Override
    public UVTransformation inverse() {
        return null;
    }
}
