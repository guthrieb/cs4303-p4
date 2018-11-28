package playercontrols;

import collisiondetection.shapes.Vector;

public class PlayerShapes {
    public static Vector[] DROPPER_VERTICES = new Vector[]{
            new Vector(-20, -30),
            new Vector(20, -20),
            new Vector(20, 20),
            new Vector(-20, 30)
    };

    public static Vector[] BOOSTING_SHAPE = new Vector[] {
            new Vector(-20, 20),
            new Vector(-20, -20),
            new Vector(40, 0)
    };

    public static Vector[] TETHERED_SHAPE = new Vector[] {
            new Vector(-20, 0),
            new Vector(-15, -5),
            new Vector(-10, -5),
            new Vector(-5, -20),
            new Vector(0, -20),
            new Vector(5, -20),
            new Vector(10, -5),
            new Vector(15, -5),
            new Vector(20, 0),
            new Vector(20, -0),
            new Vector(15, 5),
            new Vector(10, 5),
            new Vector(5, 20),
            new Vector(-0, 20),
            new Vector(-5, 20),
            new Vector(-10, 5),
            new Vector(-15, 5),
            new Vector(-20, -0),
    };

}
