package binary404.autotech.common.core.util;

import com.google.common.base.Ticker;
import net.minecraft.nbt.CompoundNBT;

public class Counter {

    private double maxTicks;
    private double ticks;

    public Counter(double max) {
        this.maxTicks = max;
    }

    public static Counter empty() {
        return new Counter(0);
    }

    public boolean isEmpty() {
        return this.ticks <= 0;
    }

    public boolean isFull() {
        return this.ticks >= this.maxTicks;
    }

    public boolean ended() {
        return this.ticks >= this.maxTicks;
    }

    public void add(double ticks) {
        this.ticks = Math.min(Math.max(0, this.ticks + ticks), this.maxTicks);
    }

    public void onward() {
        if (this.ticks < this.maxTicks) {
            this.ticks++;
        }
    }

    public void back() {
        if (this.ticks > 0) {
            this.ticks--;
        }
    }

    public void back(double value) {
        if (this.ticks > 0) {
            this.ticks -= Math.min(this.ticks, value);
        }
    }

    public void reset() {
        this.ticks = 0;
    }

    public static boolean delayed(double delay) {
        return System.currentTimeMillis() % (delay * 5) == 0;
    }

    public void read(CompoundNBT compound, String key) {
        this.ticks = compound.getDouble(key + "_ticks");
        this.maxTicks = compound.getDouble(key + "_max_ticks");
    }

    public void write(CompoundNBT compound, String key) {
        compound.putDouble(key + "_ticks", this.ticks);
        compound.putDouble(key + "_max_ticks", this.maxTicks);
    }

    public double getMax() {
        return this.maxTicks;
    }

    public void setMax(double max) {
        this.maxTicks = max;
    }

    public double getTicks() {
        return this.ticks;
    }

    public void setTicks(double ticks) {
        this.ticks = ticks;
    }

    public void setAll(double ticks) {
        this.maxTicks = ticks;
        this.ticks = ticks;
    }

    public double getEmpty() {
        return this.maxTicks - this.ticks;
    }

    public double perCent() {
        return this.ticks * 100.0D / this.maxTicks;
    }

    public float subSized() {
        return (float) (this.ticks / this.maxTicks);
    }

    public float antiSubSized() {
        return (float) (this.maxTicks / this.ticks);
    }

}
