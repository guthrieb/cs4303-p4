package collisiondetection.gjk;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Simplex;
import collisiondetection.shapes.Vector;
import collisiondetection.shapes.VectorConstants;
import drawing.Sketch;
import processing.core.PApplet;

import java.util.List;

public class Gjk {
    private final Shape shape1;
    private final Shape shape2;
    private Sketch sketch;
    private Simplex simplex;
    private Vector direction;

    public Gjk(Shape shape1, Shape shape2, Sketch sketch) {
        this.shape1 = shape1;
        this.shape2 = shape2;
        this.sketch = sketch;
        simplex = new Simplex();
    }

    public static Vector support(Shape shape1, Shape shape2, Vector direction) {
        Vector v1 = shape1.support(direction);
        Vector v2 = shape2.support(direction.negateN());

        return v1.subtractN(v2);
    }

    public boolean collision() {
        direction = shape2.centerPoint().subtractN(shape1.centerPoint());

        Vector support = support(shape1, shape2, direction);
        simplex.add(support);
        direction.negate();
        while(true) {
            Vector support1 = support(shape1, shape2, direction);
            simplex.add(support1);

            if(Vector.dot(support1, direction) <= 0) {
                return false;
            } else {
                if(simplexContainsOrigin()) {
                    return true;
                }
            }
        }
    }

    private boolean simplexContainsOrigin() {
        Vector a = simplex.getLast();
        Vector ao = a.negateN();

        if(simplex.npoints() == 3) {
            List<Vector> vertices = simplex.getOtherVertices();
            Vector b = vertices.get(0);
            Vector c = vertices.get(1);

            Vector ab = b.subtractN(a);
            Vector ac = c.subtractN(a);


            Vector abPerpendicular = Vector.tripleProduct(ac, ab, ab);
            Vector acPerpendicular = Vector.tripleProduct(ab, ac, ac);

            if(Vector.dot(abPerpendicular, ao) > 0) {
                simplex.remove(c);
                direction = abPerpendicular;
            } else {
                if(Vector.dot(abPerpendicular, ao) > 0) {
                    simplex.remove(b);
                    direction = acPerpendicular;
                } else {
                    return true;
                }
            }
        } else {
            List<Vector> vertices = simplex.getOtherVertices();
            Vector b = vertices.get(0);
            Vector ab = b.subtractN(a);



            direction.set(-ab.y, ab.x);
            if (!sameDirection(direction, ao))
                direction.negate();
        }
        return false;
    }

    private static boolean sameDirection(final Vector a, final Vector b) {
        return Vector.dot(a, b) > 0;
    }

    public Simplex getSimplex() {
        return simplex;
    }
}
