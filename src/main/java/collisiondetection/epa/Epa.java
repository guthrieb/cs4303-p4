package collisiondetection.epa;

import collisiondetection.gjk.Gjk;
import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Simplex;
import collisiondetection.shapes.Vector;

import java.util.List;

public class Epa {
    private static final double COLLISION_TOLERANCE = 0.001;

    public enum CollisionType {
        INTERPENETRATION, COLLISION
    }

    private static final double TOLERANCE = 0.00001;
    public Vector normal;
    public double depth;

    public Epa() {
    }

    private static Edge getClosestEdge(Simplex simplex) {
        Edge closestEdge = new Edge();

        List<Vector> vertices = simplex.getVertices();
        for(int i = 0; i < vertices.size(); i++) {
            int j;
            if((j = i + 1) >= vertices.size()) j = 0;

            Vector vector1 = vertices.get(i);
            Vector vector2 = vertices.get(j);

            Vector edge = vector2.subtractN(vector1);
            Vector vector1Copy = vector1.copy();
            Vector normal = Vector.tripleProduct(edge, vector1Copy, edge);
            normal.normalize();
            double distance = Vector.dot(normal, vector1);

            if (distance < closestEdge.distance) {
                closestEdge.distance = distance;
                closestEdge.normal = normal;
                closestEdge.index = j;
            }
        }

        return closestEdge;
    }

    public void execute(Shape shape1, Shape shape2, Simplex simplex) {
        while (true) {
            Edge edge = getClosestEdge(simplex);

            Vector support = Gjk.support(shape1, shape2, edge.normal);

            double supportAlongNormal = Vector.dot(support, edge.normal);

            if (supportAlongNormal - edge.distance < TOLERANCE) {
                this.normal = edge.normal;
                this.depth = supportAlongNormal;
                return;
            } else {
                simplex.insert(support, edge.index);
            }
        }
    }
}
