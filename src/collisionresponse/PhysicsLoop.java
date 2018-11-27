package collisionresponse;

import collisiondetection.contactpoints.CollisionManifoldData;
import collisiondetection.epa.MinimumTranslationVector;
import collisiondetection.epa.SeparatingAxis;
import collisiondetection.shapes.Vector;
import drawing.Sketch;
import gameobjects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class PhysicsLoop {
    private static final Vector GRAVITY = new Vector(0, 200);
    private final Sketch sketch;
    public List<GameObject> objects;
    List<CollisionManifoldData> collisions = new ArrayList<>();
    private double deltaTime;
    private int iterations;
    private static final double ROTATIONAL_DAMPING = 0.96;
    private static final double VELOCITY_DAMPING = 0.989;

    public PhysicsLoop(List<GameObject> objects, int iterations, double dt, Sketch sketch) {
        this.objects = objects;
        this.iterations = iterations;
        this.deltaTime = dt;
        this.sketch = sketch;
    }

    public void step() {
        for(int i = 0; i < objects.size() ; i++) {
            GameObject a = objects.get(i);
            for(int j = i + 1; j < objects.size(); j++) {
                GameObject b = objects.get(j);

                SeparatingAxis axis = new SeparatingAxis(sketch);
                MinimumTranslationVector minimumTranslationVector = axis.separatingAxis(a, b);

                if(minimumTranslationVector != null) {
                    CollisionManifoldData manifoldData = a.tryCollision(sketch, b);
                    if(manifoldData != null && manifoldData.getPoints().size() > 0) {
                        collisions.add(manifoldData);
                    }
                }

            }
        }


        for (GameObject object : objects) {
            integrateForces(object, deltaTime);
        }

        for (CollisionManifoldData collision : collisions) {
            for(Vector collisionPoint : collision.getPoints()) {
                sketch.fill(255, 255, 255);
                sketch.point(collisionPoint.x, collisionPoint.y);
                sketch.fill(0, 0, 0);
            }

            for (int i = 0; i < iterations; i++) {
                collision.applyImpulse();
            }
        }

        for (GameObject object : objects) {
            integrateVelocity(object, deltaTime);
        }

        for (CollisionManifoldData collision : collisions) {
            collision.positionalCorrection();
        }

        for (GameObject object : objects) {
            object.resetForcesAndTorques();
            if(object.physicsObject.velocity.mag() > object.physicsObject.terminalVelocity) {
                object.physicsObject.velocity.multiply(0.9);
            }
        }

        collisions = new ArrayList<>();

//        for(int i = 0 ; i < objects.size(); i++) {
//            objects.get(i).shape.moveToPosition(objects.get(i).physicsObject.position);
//        }


    }

    private void integrateVelocity(GameObject gameObject, double dt) {
        PhysicsObject physicsObject = gameObject.physicsObject;

        if(physicsObject.invMass == 0) {
            return;
        }

        physicsObject.position.addsi(physicsObject.velocity, dt);

        gameObject.shape.rotate((physicsObject.orientation + physicsObject.angularVelocity*dt) - physicsObject.orientation);
        physicsObject.orientation += physicsObject.angularVelocity *dt;

        physicsObject.angularVelocity*= ROTATIONAL_DAMPING;
        physicsObject.velocity.multiply(VELOCITY_DAMPING);

        integrateForces(gameObject, dt);
    }

    private void integrateForces(GameObject gameObject, double dt) {

        double dts = dt*0.5;
        PhysicsObject physicsObject = gameObject.physicsObject;

        if(physicsObject.invMass == 0) {
            return;
        }

//        System.out.println(physicsObject.calculateTotalForce());
        physicsObject.velocity.addsi(physicsObject.calculateTotalForce(), physicsObject.invMass*dts);

//        System.out.println("Before gravity: " + physicsObject.velocity);
        physicsObject.velocity.addsi(GRAVITY, dts);
//        System.out.println("After gravity: " + physicsObject.velocity);
        double totalTorque = physicsObject.calculateTotalTorque();
        physicsObject.angularVelocity += totalTorque * physicsObject.invInertia * dts;
    }
}
