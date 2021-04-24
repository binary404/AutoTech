package binary404.autotech.common.core.logistics;

public enum Tier {
    LV(320, 32),
    MV(640, 64),
    HV(1280, 128),
    EV(5120, 512),
    IV(20480, 2048),
    UV(81920, 8192),
    MaxV(327680, 32768);

    public final int maxPower;
    public final int use;

    private Tier(int maxPower, int use) {
        this.maxPower = maxPower;
        this.use = use;
    }

    public static Tier getTierByVoltage(long voltage) {
        int tier = 0;
        while (++tier < Tier.values().length) {
            if (voltage == Tier.values()[tier].use) {
                return Tier.values()[tier];
            } else if (voltage < Tier.values()[tier].use) {
                return Tier.values()[Math.max(0, tier - 1)];
            }
        }
        return Tier.values()[Math.min(Tier.values().length - 1, tier)];
    }

}
