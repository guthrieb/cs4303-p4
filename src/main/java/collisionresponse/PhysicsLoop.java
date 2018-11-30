package collisionresponse;

import collisiondetection.contactpoints.CollisionManifoldData;
import collisiondetection.epa.MinimumTranslationVector;
import collisiondetection.epa.SeparatingAxis;
import collisiondetection.shapes.Vector;
import drawing.Sketch;
import gameobjects.GameObject;
import world.HealthPowerUp;
import playercontrols.Player;
import world.WeaponPowerUp;

import java.util.*;

public class PhysicsLoop {
    public static final Vector GRAVITY = new Vector(0, 200);
    private final Sketch sketch;
    public List<GameObject> objects;
    List<CollisionManifoldData> collisions = new ArrayList<>();
    private double deltaTime;
    private int iterations;
    Set<GameObject> objectsToRemove = new HashSet<>();


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
                    if((a instanceof Player && b instanceof WeaponPowerUp)){
                        sketch.addPowerUpInteractions((Player)a, (WeaponPowerUp)b);
                        objectsToRemove.add(b);
                    } else if (a instanceof WeaponPowerUp && b instanceof Player){
                        sketch.addPowerUpInteractions((Player)b, (WeaponPowerUp)a);
                        objectsToRemove.add(a);
                    } else if (a instanceof HealthPowerUp && b instanceof Player) {
                        sketch.addPowerUpInteractions((Player) b, (HealthPowerUp) a);
                        objectsToRemove.add(a);
                    } else if (a instanceof Player && b instanceof HealthPowerUp) {
                        sketch.addPowerUpInteractions((Player)a, (HealthPowerUp)b);
                        objectsToRemove.add(b);

                    } else {
                        CollisionManifoldData manifoldData = a.tryCollision(sketch, b);
                        if(manifoldData != null && manifoldData.getPoints().size() > 0) {
                            collisions.add(manifoldData);
                        }
                    }

                }

            }
        }


        for (GameObject object : objects) {



            object.physicsObject.addForce("",object.physicsObject.mg, object.physicsObject.position, false);

            integrateForces(object, deltaTime);
        }

        applyImpulses();

        for (GameObject object : objects) {
            integrateVelocity(object, deltaTime);
        }

        for (CollisionManifoldData collision : collisions) {
            collision.positionalCorrection();
        }

        ListIterator<GameObject> objectListIterator = objects.listIterator();
        while (objectListIterator.hasNext()) {
            GameObject object = objectListIterator.next();
            if(objectsToRemove.contains(object)) {
                objectListIterator.remove();
            } else {
                object.resetForcesAndTorques();


                if(object.physicsObject.velocity.mag() > object.physicsObject.terminalVelocity) {
                    object.physicsObject.velocity.multiply(0.9);
                }
            }
        }
//
//        for (GameObject object : objects) {
//            object.resetForcesAndTorques();
//
//            System.out.println("orientation: " + object.physicsObject.orientation);
//            if(object.physicsObject.velocity.mag() > object.physicsObject.terminalVelocity) {
//                object.physicsObject.velocity.multiply(0.9);
//            }
//        }

        collisions = new ArrayList<>();
        objectsToRemove = new HashSet<>();

//        for(int i = 0 ; i < objects.size(); i++) {
//            objects.get(i).shape.moveToPosition(objects.get(i).physicsObject.position);
//        }



    }

    private void applyImpulses() {
        for (CollisionManifoldData collision : collisions) {
            for (int i = 0; i < iterations; i++) {
                collision.applyImpulse();
            }
        }
    }

    private void integrateVelocity(GameObject gameObject, double dt) {
        PhysicsObject physicsObject = gameObject.physicsObject;

        if(physicsObject.invMass == 0) {
            return;
        }

        physicsObject.position.addsi(physicsObject.velocity, dt);

        gameObject.shape.rotate((physicsObject.orientation + physicsObject.angularVelocity*dt) - physicsObject.orientation);
        physicsObject.orientation += physicsObject.angularVelocity *dt;

        physicsObject.angularVelocity*= physicsObject.rotationalDamping;
        physicsObject.velocity.multiply(physicsObject.linearDamping);
        physicsObject.velocity.x *= physicsObject.xVelDamping;
        physicsObject.velocity.y *= physicsObject.yVelDamping;

        integrateForces(gameObject, dt);
    }

    private void integrateForces(GameObject gameObject, double dt) {

        double dts = dt*0.5;
        PhysicsObject physicsObject = gameObject.physicsObject;

        if(physicsObject.invMass == 0) {
            return;
        }

        physicsObject.velocity.addsi(physicsObject.calculateTotalForce(), physicsObject.invMass*dts);

//        System.out.println("Before gravity: " + physicsObject.velocity);
//        physicsObject.velocity.addsi(GRAVITY.multiplyN(gameObject.physicsObject.gravMod), dts);
//        System.out.println("After gravity: " + physicsObject.velocity);
        double totalTorque = physicsObject.calculateTotalTorque();
        physicsObject.angularVelocity += totalTorque * physicsObject.invInertia * dts;
    }
}
