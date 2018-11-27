package collisiondetection.contactpoints;

import collisiondetection.epa.Epa;
import collisiondetection.shapes.Vector;
import collisionresponse.PhysicsCollider;
import gameobjects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class CollisionManifoldData {
    private static final double TOLERANCE = 0.05;
    List<Vector> points = new ArrayList<>();
    Vector collisionNormal = null;
    private double depth;
    private GameObject object1;
    private GameObject object2;
    private Epa.CollisionType collisionType = null;


    public double getDepth() {
        return depth;
    }

    public void addNormal(Vector collisionNormal) {
        this.collisionNormal = collisionNormal;
    }

    public static CollisionManifoldData clip(Vector v1, Vector v2, Vector normal, double o){
        CollisionManifoldData cp = new CollisionManifoldData();
        double d1 = Vector.dot(normal, v1) - o;
        double d2 = Vector.dot(normal, v2) - o;

        if(d1 >= 0) {
            cp.add(v1);
        }
        if(d2 >= 0) {
            cp.add(v2);
        }

        if(d1*d2 < 0) {
            Vector e = v2.subtractN(v1);
            double u = d1/(d1-d2);
            e.multiply(u);
            e.add(v1);

            cp.add(e);
        }

        return cp;
    }

    public List<Vector> getPoints() {
        return points;
    }


    private void add(Vector v1) {
        points.add(v1);
    }

    public Vector getCollisionNormal() {
        return collisionNormal;
    }

    public void addDepth(double depth) {
        this.depth = depth;
    }

    public void addType(Epa.CollisionType type) {
        this.collisionType = type;
    }

    public Epa.CollisionType getType() {
        return collisionType;
    }

    public GameObject getObject1() {
        return object1;
    }

    public GameObject getObject2() {
        return object2;
    }

    public void applyImpulse() {
        PhysicsCollider collider = new PhysicsCollider(object1, object2);
        collider.collide(points, collisionNormal);
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

    public void positionalCorrection() {
        double correction = Math.max(depth - TOLERANCE, 0.0)/ (object1.physicsObject.invMass + object2.physicsObject.invMass) * 0.4;

        object1.physicsObject.position.adds(collisionNormal, -object1.physicsObject.invMass * correction);
        object2.physicsObject.position.adds(collisionNormal, object2.physicsObject.invMass * correction);
    }
}
