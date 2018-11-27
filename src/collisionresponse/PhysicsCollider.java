package collisionresponse;

import collisiondetection.shapes.Vector;
import gameobjects.GameObject;

import java.util.List;

public class PhysicsCollider {
    private static final double ELASTICITY = 0.2;
    GameObject gameObject1;
    GameObject gameObject2;



    public PhysicsCollider(GameObject object1, GameObject object2) {
        this.gameObject1 = object1;
        this.gameObject2 = object2;
    }

    private Vector getRv(Vector rb, Vector ra, Vector velocity1, Vector velocity2, double angularVelocity1, double angularVelocity2) {
        Vector result = velocity2.copy();

        Vector cross1 = Vector.cross(angularVelocity2, rb, new Vector(0, 0));
        result.add(cross1);

        result.subtract(velocity1);
        Vector cross2 = Vector.cross(angularVelocity1, ra, new Vector(0, 0));
        result.subtract(cross2);

        return result;
    }

    public void collide(List<Vector> pointsOfCollision, Vector normalOfCollision) {
        for (int i = 0; i < pointsOfCollision.size(); ++i) {

            Vector radiusVector1 = pointsOfCollision.get(i).subtractN(gameObject1.physicsObject.position);
            Vector radiusVector2 = pointsOfCollision.get(i).subtractN(gameObject2.physicsObject.position);


            Vector relativeVelocity = gameObject2.physicsObject.velocity.addN(
                    Vector.cross(gameObject2.physicsObject.angularVelocity, radiusVector2))
                    .subtract(gameObject1.physicsObject.velocity)
                    .subtract(Vector.cross(gameObject1.physicsObject.angularVelocity, radiusVector1));


            double contactVel = Vector.dot(relativeVelocity, normalOfCollision);

            if (contactVel > 0) {
                return;
            }
            double raCrossN = Vector.cross(radiusVector1, normalOfCollision);
            double rbCrossN = Vector.cross(radiusVector2, normalOfCollision);

            double invMassSum = gameObject1.physicsObject.invMass + gameObject2.physicsObject.invMass
                    + (raCrossN * raCrossN) * gameObject1.physicsObject.invInertia
                    + (rbCrossN * rbCrossN) * gameObject2.physicsObject.invInertia;

            double j = -(1.0f + 0.0) * contactVel;
            j /= invMassSum;
            j /= pointsOfCollision.size();

            Vector impulse = normalOfCollision.multiplyN(j);

            gameObject1.applyImpulse(impulse.negateN(), radiusVector1);
            gameObject2.applyImpulse(impulse, radiusVector2);
        }
    }
}
