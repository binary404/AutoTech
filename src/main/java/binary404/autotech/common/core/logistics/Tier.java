package binary404.autotech.common.core.logistics;

public enum Tier {
    LV(5000, 50, 300, 500, 100),
    MV(10000, 100, 250, 100, 120),
    HV(15000, 150, 200, 250, 140),
    EV(30000, 300, 150, 700, 180),
    IV(45000, 450, 100, 1000, 200),
    MaxV(75000, 750, 20, 2500, 220);

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
