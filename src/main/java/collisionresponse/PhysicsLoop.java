package collisionresponse;

import collisiondetection.contactpoints.CollisionManifoldData;
import collisiondetection.epa.MinimumTranslationVector;
import collisiondetection.epa.SeparatingAxis;
import collisiondetection.shapes.Vector;
import drawing.Sketch;
import gameobjects.DestroyerBeam;
import gameobjects.GameObject;
import playercontrols.Player;
import world.HealthPowerUp;
import world.WeaponPowerUp;

import java.util.*;

public class PhysicsLoop {
    static final Vector GRAVITY = new Vector(0, 50);
    private final Sketch sketch;
    private double deltaTime;
    private int iterations;
    public List<GameObject> objects;
    private List<CollisionManifoldData> collisions = new ArrayList<>();
    private Set<GameObject> objectsToRemove = new HashSet<>();

    public PhysicsLoop(List<GameObject> objects, int iterations, double dt, Sketch sketch) {
        this.objects = objects;
        this.iterations = iterations;
        this.deltaTime = dt;
        this.sketch = sketch;
    }

    public void step() {
        calculateCollisionManifolds();
        applyForces();
        applyImpulses();
        applyVelocities();
        translateCollisionVectors();
        removeObjects();

        collisions = new ArrayList<>();
        objectsToRemove = new HashSet<>();
    }

    private void calculateCollisionManifolds() {
        for (int i = 0; i < objects.size(); i++) {
            GameObject a = objects.get(i);
            for (int j = i + 1; j < objects.size(); j++) {
                GameObject b = objects.get(j);


                if (a.physicsObject.isStatic() && b.physicsObject.isStatic()) {
                    continue;
                }
                double radiusSum = a.radiusMag + b.radiusMag;
                if (radiusSum < a.physicsObject.position.subtractN(b.physicsObject.position).mag()) {
                    continue;
                }

                SeparatingAxis axis = new SeparatingAxis();
                MinimumTranslationVector minimumTranslationVector = axis.separatingAxis(a, b);

                if (minimumTranslationVector != null) {
                    if ((a instanceof Player && b instanceof WeaponPowerUp)) {
                        sketch.addPowerUpInteractions((Player) a, (WeaponPowerUp) b);
                        objectsToRemove.add(b);
                    } else if (a instanceof WeaponPowerUp && b instanceof Player) {
                        sketch.addPowerUpInteractions((Player) b, (WeaponPowerUp) a);
                        objectsToRemove.add(a);
                    } else if (a instanceof HealthPowerUp && b instanceof Player) {
                        sketch.addPowerUpInteractions((Player) b, (HealthPowerUp) a);
                        objectsToRemove.add(a);
                    } else if (a instanceof Player && b instanceof HealthPowerUp) {
                        sketch.addPowerUpInteractions((Player) a, (HealthPowerUp) b);
                        objectsToRemove.add(b);
                    } else if (a instanceof DestroyerBeam && b instanceof Player) {
                        ((Player) b).destroy();
                    } else if (a instanceof Player && b instanceof DestroyerBeam) {
                        ((Player) a).destroy();
                    } else if (a instanceof DestroyerBeam && (b instanceof HealthPowerUp || b instanceof WeaponPowerUp)) {
                        objectsToRemove.add(b);
                    } else if ((a instanceof HealthPowerUp || a instanceof WeaponPowerUp) && (b instanceof DestroyerBeam)) {
                        objectsToRemove.add(a);
                    } else {
                        CollisionManifoldData manifoldData = a.tryCollision(sketch, b);
                        if (manifoldData != null && manifoldData.getPoints().size() > 0) {
                            collisions.add(manifoldData);
                        }
                    }

                }

            }
        }
    }

    private void removeObjects() {
        ListIterator<GameObject> objectListIterator = objects.listIterator();
        while (objectListIterator.hasNext()) {
            GameObject object = objectListIterator.next();
            if (objectsToRemove.contains(object)) {
                objectListIterator.remove();
            } else {
                object.resetForcesAndTorques();

                if (object.physicsObject.velocity.mag() > object.physicsObject.terminalVelocity) {
                    object.physicsObject.velocity.multiply(0.9);
                }
            }
        }
    }

    private void applyForces() {
        for (GameObject object : objects) {
            object.physicsObject.addForce("", object.physicsObject.mg, object.physicsObject.position, false);

            calculateForces(object, deltaTime);
        }
    }

    private void translateCollisionVectors() {
        for (CollisionManifoldData collision : collisions) {
            collision.translatePosition();
        }
    }

    private void applyVelocities() {
        for (GameObject object : objects) {
            calculateVelocities(object, deltaTime);
        }
    }

    private void applyImpulses() {
        for (CollisionManifoldData collision : collisions) {
            for (int i = 0; i < iterations; i++) {
                collision.applyImpulse();
            }
        }
    }

    private void calculateVelocities(GameObject gameObject, double deltaTime) {
        PhysicsObject physicsObject = gameObject.physicsObject;

        if(physicsObject.invMass == 0) {
            return;
        }

        physicsObject.position.addMultScalar(physicsObject.velocity, deltaTime);
        gameObject.shape.rotate((physicsObject.orientation + physicsObject.angularVelocity * deltaTime) - physicsObject.orientation);
        physicsObject.orientation += physicsObject.angularVelocity * deltaTime;
        physicsObject.angularVelocity*= physicsObject.rotationalDamping;
        physicsObject.velocity.multiply(physicsObject.linearDamping);
        physicsObject.velocity.x *= physicsObject.xVelDamping;
        physicsObject.velocity.y *= physicsObject.yVelDamping;

        calculateForces(gameObject, deltaTime);
    }

    private void calculateForces(GameObject gameObject, double deltaTime) {

        double deltaTimeTransformed = deltaTime * 0.5;
        PhysicsObject physicsObject = gameObject.physicsObject;

        if(physicsObject.invMass == 0) {
            return;
        }

        physicsObject.velocity.addMultScalar(physicsObject.calculateTotalForce(), physicsObject.invMass * deltaTimeTransformed);
        double totalTorque = physicsObject.calculateTotalTorque();
        physicsObject.angularVelocity += totalTorque * physicsObject.invInertia * deltaTimeTransformed;
    }
}
