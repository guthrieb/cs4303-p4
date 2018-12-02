package playercontrols;

import collisiondetection.shapes.Vector;

public class PlayerShapes {
    public static Vector[] SHELL_SHAPES = new Vector[] {
            new Vector(-20, -20),
            new Vector(20, -20),
            new Vector(20, 20),
            new Vector(-20, 20)
    };
    public static final Vector[] DROPPER_VERTICES = new Vector[]{
            new Vector(-20, -30),
            new Vector(20, -20),
            new Vector(20, 20),
            new Vector(-20, 30)
    };

    public static final Vector[] BOOSTING_VERTICES = new Vector[]{
            new Vector(-20, 20),
            new Vector(-20, -20),
            new Vector(40, 0)
    };

    public static Vector[] BOOSTING_VERTICES_ADJUST = new Vector[] {
            new Vector(-200.0, 200.0),
            new Vector(-200.0, -200.0),
            new Vector(400.0, 0.0)
    };

//    public static Vector[] TETHERED_VERTICES = new Vector[] {
//            new Vector(-25, -5),
//            new Vector(-15, -10),
//            new Vector(-10, -10),
//            new Vector(-10, -25),
//            new Vector(0, -25),
//            new Vector(10, -25),
//            new Vector(10, -10),
//            new Vector(15, -10),
//            new Vector(25, 5),
//            new Vector(40, 0),
//            new Vector(25, -5),
//            new Vector(15, 10),
//            new Vector(10, 10),
//            new Vector(10, 25),
//            new Vector(-0, 25),
//            new Vector(-10, 25),
//            new Vector(-10, 10),
//            new Vector(-15, 10),
//            new Vector(-25, 5),
//    };

    public static final Vector[] TETHERED_VERTICES = new Vector[]{
            new Vector(-20, -30),
            new Vector(20, -30),
            new Vector(40, 0),
            new Vector(20, 30),
            new Vector(-20, 30),

    };

    public static Vector[] TETHERED_VERTICES_ADJUST = new Vector[] {
            new Vector( -250.0,-50.0),
            new Vector( -150.0,-100.0),
            new Vector( -100.0,-100.0),
            new Vector( -100.0,-250.0),
            new Vector( 0.0,-250.0),
            new Vector( 100.0,-250.0),
            new Vector( 100.0,-100.0),
            new Vector( 150.0,-100.0),
            new Vector( 250.0,50.0),
            new Vector( 250.0,-50.0),
            new Vector( 150.0,100.0),
            new Vector( 100.0,100.0),
            new Vector( 100.0,250.0),
            new Vector( 0.0,250.0),
            new Vector( -100.0,250.0),
            new Vector( -100.0,100.0),
            new Vector( -150.0,100.0),
            new Vector( -250.0,50.0),
    };

    public static Vector[] DROPPER_VERTICES_ADJUST = new Vector[]{
            new Vector( -200.0,-300.0),
            new Vector( 200.0,-200.0),
            new Vector( 200.0,200.0),
            new Vector( -200.0,300.0)
    };

    public static void main(String[] args) {
        for (Vector DROPPER_VERTICE : DROPPER_VERTICES) {
            Vector x = DROPPER_VERTICE.multiplyN(10);
        }
    }
}
