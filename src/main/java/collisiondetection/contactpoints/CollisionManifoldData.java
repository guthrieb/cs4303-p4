package collisiondetection.contactpoints;

import collisiondetection.shapes.Vector;
import collisionresponse.PhysicsCollider;
import gameobjects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class CollisionManifoldData {
    private static final double TOLERANCE = 0.05;
    final List<Vector> points = new ArrayList<>();
    private Vector collisionNormal = null;
    private double depth;
    private GameObject object1;
    private GameObject object2;

    public void addNormal(Vector collisionNormal) {
        this.collisionNormal = collisionNormal;
    }

    static CollisionManifoldData clip(Vector vertex1, Vector vertex2, Vector normal, double o) {
        CollisionManifoldData collisionManifold = new CollisionManifoldData();
        double distance1 = Vector.dot(normal, vertex1) - o;
        double distance2 = Vector.dot(normal, vertex2) - o;

        if (distance1 >= 0) {
            collisionManifold.add(vertex1);
        }
        if (distance2 >= 0) {
            collisionManifold.add(vertex2);
        }

        if (distance1 * distance2 < 0) {
            Vector edge = vertex2.subtractN(vertex1);
            double percentageDistance = distance1 / (distance1 - distance2);
            edge.multiply(percentageDistance);
            edge.add(vertex1);

            collisionManifold.add(edge);
        }

        return collisionManifold;
    }

    public List<Vector> getPoints() {
        return points;
    }

    private void add(Vector v1) {
        points.add(v1);
    }
    public void addDepth(double depth) {
        this.depth = depth;
    }

    public void applyImpulse() {
        PhysicsCollider collider = new PhysicsCollider(object1, object2);
        collider.calculateImpulseMaths(points, collisionNormal);
    }

    public void addObjects(GameObject gameObject, GameObject object2) {
        this.object1 = gameObject;
        this.object2 = object2;
    }

    @Override
    public String toString() {
        return "CollisionManifoldData{" +
                "points=" + points +
                ", collisionNormal=" + collisionNormal +
                '}';
    }

    public void translatePosition() {
        double correction = Math.max(depth - TOLERANCE, 0.0)/ (object1.physicsObject.invMass + object2.physicsObject.invMass) * 0.4;

        object1.physicsObject.position.add(collisionNormal, -object1.physicsObject.invMass * correction);
        object2.physicsObject.position.add(collisionNormal, object2.physicsObject.invMass * correction);
    }
}
