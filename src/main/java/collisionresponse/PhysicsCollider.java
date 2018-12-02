package collisionresponse;

import collisiondetection.shapes.Vector;
import gameobjects.GameObject;

import java.util.List;

public class PhysicsCollider {
    private final GameObject gameObject1;
    private final GameObject gameObject2;

    public PhysicsCollider(GameObject object1, GameObject object2) {
        this.gameObject1 = object1;
        this.gameObject2 = object2;
    }

    public void calculateImpulseMaths(List<Vector> pointsOfCollision, Vector normalOfCollision) {
        PhysicsObject physicsObject1 = gameObject1.physicsObject;
        PhysicsObject physicsObject2 = gameObject2.physicsObject;
        for (int i = 0; i < pointsOfCollision.size(); ++i) {

            Vector position1 = physicsObject1.position;
            Vector position2 = physicsObject2.position;

            Vector radiusVector1 = pointsOfCollision.get(i).subtractN(position1);
            Vector radiusVector2 = pointsOfCollision.get(i).subtractN(position2);


            Vector relativeVelocity = physicsObject2.velocity.addN(
                    Vector.cross(physicsObject2.angularVelocity, radiusVector2))
                    .subtract(physicsObject1.velocity)
                    .subtract(Vector.cross(physicsObject1.angularVelocity, radiusVector1));


            double contactVelocity = Vector.dot(relativeVelocity, normalOfCollision);

            if (contactVelocity > 0) {
                return;
            }

            double radiusVector1CrossNormal = Vector.cross(radiusVector1, normalOfCollision);
            double radiusVector2CrossNormal = Vector.cross(radiusVector2, normalOfCollision);

            double invMassSum = physicsObject1.invMass + physicsObject2.invMass;
            invMassSum += (radiusVector1CrossNormal * radiusVector1CrossNormal * physicsObject1.invInertia);
            invMassSum += (radiusVector2CrossNormal * radiusVector2CrossNormal * physicsObject2.invInertia);

            //Calculate impulse maths
            double j = -(1.0f + Math.max(physicsObject1.elasticity, physicsObject2.elasticity)) * contactVelocity;
            j = j / invMassSum;
            j = j / pointsOfCollision.size();

            Vector impulse = normalOfCollision.multiplyN(j);

            gameObject1.applyImpulse(impulse.negateN(), radiusVector1);
            physicsObject1.getImpulseCollisions().add(contactVelocity);
            gameObject2.applyImpulse(impulse, radiusVector2);
            physicsObject2.getImpulseCollisions().add(contactVelocity);
        }
    }
}
