package collisiondetection.epa;

import collisiondetection.shapes.Polygon;
import gameobjects.GameObject;

public class CollisionPointDetection {
    public static final float BIAS_RELATIVE = 0.95f;
    public static final float BIAS_ABSOLUTE = 0.01f;
    public void findCollision(GameObject object1, GameObject object2) {
        int refIndex;
        boolean flip;

        Polygon ref;
        Polygon inc;

//        if(gt())
    }

    public boolean gt(double a,  double b) {
        return a >= b *BIAS_RELATIVE + a*BIAS_ABSOLUTE;
    }
}
