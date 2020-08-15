package binary404.autotech.common.core.logistics;

public enum Tier {
    BASIC(100000, 40, 200),
    MID(1000000, 120, 100),
    ADVANCED(10000000, 400, 25);

    public long maxPower;
    public int use, speed;

    private Tier(long maxPower, int use, int speed) {
        this.maxPower = maxPower;
        this.use = use;
        this.speed = speed;
    }

}
