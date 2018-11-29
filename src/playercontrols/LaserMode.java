package playercontrols;

public class LaserMode {
    public LaserMode(int noOfLasers, int laserLength, double laserMod, double spreadBetweenLasers) {
        this.noOfLasers = noOfLasers;
        this.laserLength = laserLength;
        this.laserMod = laserMod;
        this.spreadBetweenLasers = spreadBetweenLasers;
    }

    int noOfLasers;
    int laserLength;
    double laserMod;
    double spreadBetweenLasers;

    public static LaserMode standardLaserMode() {
        return new LaserMode(1, 100000000, 1, 0);
    }

    public static LaserMode shotgunLasers() {
        return new LaserMode(10, 700, 0.8, 0.2);
    }
}
