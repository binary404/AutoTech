package binary404.autotech.common.core.logistics;

public enum Tier {
    LV(20000, 30, 300, 30, 100),
    MV(50000, 100, 250, 100, 120),
    HV(100000, 250, 200, 250, 140),
    EV(500000, 700, 150, 700, 180),
    IV(800000, 1000, 100, 1000, 200),
    MaxV(1000000, 2000, 20, 2500, 220);

    public long maxPower;
    public int use, speed, gen, genSpeed;

    private Tier(long maxPower, int use, int speed, int gen, int genSpeed) {
        this.maxPower = maxPower;
        this.use = use;
        this.speed = speed;
        this.gen = gen;
        this.genSpeed = genSpeed;
    }

}
