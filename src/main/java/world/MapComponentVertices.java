package world;

import collisiondetection.shapes.Vector;

public class MapComponentVertices {
    public static final Vector[] WALL_SHAPE_2 = new Vector[]{
            new Vector(-2000, -100),
            new Vector(2000, -100),
            new Vector(2000, 100),
            new Vector(-2000, 100)
    };
    public static final Vector[] CORNER_WEDGE_1 = new Vector[]{
            new Vector(0, -50),
            new Vector(0, 0),
            new Vector(50, 0)
    };

    public static final Vector[] CORNER_WEDGE_2 = new Vector[]{
            new Vector(0, -50),
            new Vector(0, 0),
            new Vector(-50, 0)
    };
    public static final Vector[] RECTANGLE = new Vector[]{
            new Vector(-10, -80),
            new Vector(10, -80),
            new Vector(10, 80),
            new Vector(-10, 80)
    };

    public static final Vector[] DESTROYER_BEAM_HORIZONTAL = new Vector[]{
            new Vector(-2000, -5),
            new Vector(2000, -5),
            new Vector(2000, 5),
            new Vector(-2000, 5)
    };

    public static final Vector[] DESTROYER_BEAM_VERTICAL = new Vector[]{
            new Vector(-5, -2000),
            new Vector(5, -2000),
            new Vector(5, 2000),
            new Vector(-5, 2000)
    };

    public static Vector[] WALL_SHAPE_1 = new Vector[]{
            new Vector(-100, -1500),
            new Vector(100, -1500),
            new Vector(100, 1500),
            new Vector(-100, 1500)
    };
//
//    public static Vector[] WEDGE = new Vector[]{
//            new Vector(0, -40),
//            new Vector(30, 0),
//            new Vector(-30, 0)
//    };

    public static Vector[] WEDGE = new Vector[]{
            new Vector(-40.0, 20.0),
            new Vector(0.0, -20.0),
            new Vector(40.0, 20.0)
    };

    public static Vector[] WEDGE_2 = new Vector[]{
            new Vector(20, 20),
            new Vector(20, -20),
            new Vector(-40, 0)
    };

    public static Vector[] WEDGE_3 = new Vector[]{
            new Vector(-2, -1),
            new Vector(0, 1),
            new Vector(2, -1)
    };

    public static Vector[] SQUARE = new Vector[]{
            new Vector(-20, -20),
            new Vector(20, -20),
            new Vector(20, 20),
            new Vector(-20, 20)
    };

    public static Vector[] OCTAGON = new Vector[]{
            new Vector(-10.0, 30.0),
            new Vector(-30.0, 10.0),
            new Vector(-30.0, -10.0),
            new Vector(-10.0, -30.0),
            new Vector(10.0, -30.0),
            new Vector(30.0, -10.0),
            new Vector(30.0, 10.0),
            new Vector(10.0, 30.0),
    };

    public static Vector[] scale(Vector[] toScale, double scale) {
        Vector[] vectors = new Vector[toScale.length];

        for (int i = 0; i < toScale.length; i++) {
            Vector vector = toScale[i];
            vectors[i] = vector.multiplyN(scale);
        }
        return vectors;
    }

    public static void main(String[] args) {
        Vector[] scaled = scale(WEDGE, 10);
        for (Vector scalUp : scaled) {
            System.out.println("new Vector(" + scalUp.x + "," + scalUp.y + "),");
        }
    }
}
