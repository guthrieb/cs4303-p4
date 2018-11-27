package collisionresponse;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import processing.core.PApplet;
import sun.security.provider.SHA;

import java.util.ArrayList;
import java.util.List;

public class PhysicsObject {
    public final double invMass;
    public Vector position;


    public Vector velocity = new Vector(0, 0);
    public List<Force> forces = new ArrayList<>();
    public double mass;
    public double momentOfInertia;

    public Shape shape;
    public double orientation;
    public double angularVelocity;
    public double invInertia;
    public double terminalVelocity = 10000;

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
    }

    public void addForce(String id, Vector force, Vector contactPoint, boolean angular) {
        forces.add(new Force(id, force, contactPoint, angular));
    }

    public void addForce(String id, Vector force, Vector contactPoint) {
        forces.add(new Force(id, force, contactPoint, true));
    }

    public Vector calculateTotalForce() {
        Vector totalForce = new Vector(0, 0);
        for (Force force : forces) {
            totalForce.add(force.directions);
        }
        return totalForce;
    }

    public double calculateTorque(Force force) {
        if(!force.angular) {
            return 0;
        }
        Vector radiusVector = force.contactPoint.subtractN(position);
        System.out.println("Force contact Point: " + force.contactPoint);
        System.out.println("Position: " + position);
        System.out.println("Radius Vector: " + radiusVector);
        return Vector.dot(radiusVector, force.directions);
    }

    public double calculateTotalTorque() {
        double totalTorque = 0;
        for (Force force : forces) {
            totalTorque += calculateTorque(force);
        }
        return totalTorque;
    }

    public void applyCollisionImpulse(Vector impulse, Vector contactVector) {
        velocity.addsi( impulse, invMass );

        angularVelocity += invInertia * Vector.cross( contactVector, impulse );

    }

    public void translate(double v, double v1) {
        position.x += v;
        position.y += v1;
    }

    public void draw(PApplet sketch) {
        sketch.strokeWeight(5);
        sketch.point((float)position.x, (float)position.y);
        sketch.strokeWeight(1);
    }

    public void moveToPosition(Vector position) {
        translate(position.x - this.position.x, position.y - this.position.y);
    }

    public void addRotationalAcceleration(double accelerationRate) {
        angularVelocity += accelerationRate;
    }
}
