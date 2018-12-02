package collisionresponse;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;

import java.util.ArrayList;
import java.util.List;

public class PhysicsObject {
    public double invMass;
    public Vector position;

    public Vector velocity = new Vector(0, 0);
    public List<Force> forces = new ArrayList<>();
    public double mass;
    private double momentOfInertia;

    public Shape shape;
    public double orientation;
    double angularVelocity;
    public double invInertia;
    double terminalVelocity = 100000;
    public Vector mg;
    public double elasticity;
    public double linearDamping = 0.989;
    public double rotationalDamping = 0.96;
    public double xVelDamping = 1;
    public double yVelDamping = 1;
    private List<Double> impulseCollisions = new ArrayList<>();

    public PhysicsObject(Shape shape, Vector position, double mass, double momentOfInertia) {
        this.shape = shape;
        this.position = position;
        this.mass = mass;
        this.momentOfInertia = momentOfInertia;
        if(mass != 0) {
            this.invMass = 1/mass;
        } else {
            this.invMass = 0;
        }

        if(this.momentOfInertia != 0) {
            this.invInertia = 1/ this.momentOfInertia;
        } else {
            this.invInertia = 0;
        }
        this.orientation = 0;
        this.mg = PhysicsLoop.GRAVITY.multiplyN(mass);
        this.elasticity = 0;
    }

    public void addForce(String id, Vector force, Vector contactPoint, boolean angular) {
        forces.add(new Force(id, force, contactPoint, angular));
    }

    public void addForce(String id, Vector force, Vector contactPoint) {
        forces.add(new Force(id, force, contactPoint, true));
    }

    Vector calculateTotalForce() {
        Vector totalForce = new Vector(0, 0);
        for (Force force : forces) {
            totalForce.add(force.directions);
        }
        return totalForce;
    }

    private double calculateTorque(Force force) {
        if(!force.angular) {
            return 0;
        }
        Vector radiusVector = force.contactPoint.subtractN(position);
        return Vector.dot(radiusVector, force.directions);
    }

    double calculateTotalTorque() {
        double totalTorque = 0;
        for (Force force : forces) {
            totalTorque += calculateTorque(force);
        }
        return totalTorque;
    }

    public void applyCollisionImpulse(Vector impulse, Vector contactVector) {


        velocity.addMultScalar(impulse, invMass);

        angularVelocity += invInertia * Vector.cross( contactVector, impulse );

    }

    public void addRotationalAcceleration(double accelerationRate) {
        angularVelocity += accelerationRate;
    }

    public void resetDirectionalDamping() {
        this.xVelDamping = 1;
        this.yVelDamping = 1;
    }

    public List<Double> getImpulseCollisions() {
        return impulseCollisions;
    }

    public void setImpulseCollisions(List<Double> impulseCollisions) {
        this.impulseCollisions = impulseCollisions;
    }

    public boolean isStatic() {
        return mass == 0;
    }
}
