package binary404.autotech.common.core.logistics;

public enum Tier {
    LV(10000, 30, 300, 60, 100),
    MV(100000, 90, 150, 160, 120),
    HV(1000000, 400, 60, 600, 140),
    EV(50000000, 800, 40, 800, 180);

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
