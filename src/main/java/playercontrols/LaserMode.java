package playercontrols;

import ddf.minim.AudioPlayer;
import drawing.Sketch;
import helpers.Timer;

public class LaserMode {

    public LaserMode(int noOfLasers, long fireRate, boolean automatic, int laserLength, double laserMod, double spreadBetweenLasers, String soundKey) {
        this.noOfLasers = noOfLasers;
        this.laserLength = laserLength;
        this.laserMod = laserMod;
        this.spreadBetweenLasers = spreadBetweenLasers;
        this.automatic = automatic;
        this.fireRate = new Timer(fireRate);
        this.soundKey = soundKey;
    }

    int noOfLasers;
    int laserLength;
    double laserMod;
    double spreadBetweenLasers;
    Timer fireRate;
    boolean automatic;
    String soundKey;

    public boolean readyToFire() {
        if(fireRate.completed()) {
            fireRate.reset();
            return true;
        }
        return false;
    }

    public static LaserMode standardLaserMode() {
        return new LaserMode(1, 1000, false, 100000000, 3, 0, Sketch.REGULARLASER_KEY);
    }

    public static LaserMode megaLaserMode() {
        return new LaserMode(8, 4000, false, 100000000, 0.5, 0.005, Sketch.MEGALASER_KEY);
    }

    public static LaserMode shotgunLasers() {
        return new LaserMode(10, 1000, false, 700, 0.5, 0.2, Sketch.SHOTGUN_KEY);
    }
    public static LaserMode machineGunLasers() {
        return new LaserMode(1, 200, true, 700, 0.5, 0.2, Sketch.MACHINE_GUN_KEY);
    }

    public double percentageReady() {
        double v = fireRate.percentageRemaining();
        if(v < 0.0) {
            v = 0.0;
        }
        return v;
    }
}
