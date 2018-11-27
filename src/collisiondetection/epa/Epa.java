package collisiondetection.epa;

import collisiondetection.gjk.Gjk;
import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Simplex;
import collisiondetection.shapes.Vector;
import drawing.Sketch;

import java.util.List;

public class Epa {
    private static final double COLLISION_TOLERANCE = 0.001;
    private final Sketch sketch;

    public enum CollisionType {
        INTERPENETRATION, COLLISION
    }

    public CollisionType type;
    public static final double TOLERANCE = 0.00001;
    public Vector normal;
    public double depth;

    public Epa(Sketch sketch) {
        this.sketch = sketch;
    }

    public void execute(Shape shape1, Shape shape2, Simplex simplex) {
        while (true) {
            Edge edge = getClosestEdge(simplex);

            Vector p = Gjk.support(shape1, shape2, edge.normal);

            double d = Vector.dot(p, edge.normal);

            if(d - edge.distance < TOLERANCE) {
                this.normal = edge.normal;
                this.depth = d;


                if(depth < COLLISION_TOLERANCE) {
                    type = CollisionType.COLLISION;
                } else {
                    type = CollisionType.INTERPENETRATION;
                }
                return;
            } else {
                simplex.insert(p, edge.index);
            }
        }
    }

    private static Edge getClosestEdge(Simplex simplex) {
        Edge closest = new Edge();

        List<Vector> vertices = simplex.getVertices();
        for(int i = 0; i < vertices.size(); i++) {
            int j;
            if((j = i + 1) >= vertices.size()) j = 0;

            Vector a = vertices.get(i);
            Vector b = vertices.get(j);

            Vector e = b.subtractN(a);

            Vector aCopy = a.copy();
            Vector n = Vector.tripleProduct(e, aCopy, e);
            n.normalize();

            double distance = Vector.dot(n, a);

            if(distance < closest.distance) {
                closest.distance = distance;
                closest.normal = n;
                closest.index = j;
            }
        }

        return closest;
    }
}
