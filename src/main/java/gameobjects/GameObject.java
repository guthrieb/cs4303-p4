package gameobjects;

import collisiondetection.contactpoints.ClippingPoints;
import collisiondetection.contactpoints.CollisionManifoldData;
import collisiondetection.epa.Epa;
import collisiondetection.gjk.Gjk;
import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import collisionresponse.PhysicsObject;
import drawing.Colour;
import drawing.Sketch;
import processing.core.PConstants;
import processing.core.PShape;

import java.util.ArrayList;
import java.util.Objects;

public class GameObject {
    public String id;
    public Shape shape;
    public collisionresponse.PhysicsObject physicsObject;
    private boolean tetherable;
    protected Colour fillColour;
    protected Colour lineColour;
    public double radiusMag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameObject object = (GameObject) o;
        return Objects.equals(id, object.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public GameObject(String id, Shape shape, Vector position, double mass, double momentOfInertia, Colour fillColour, Colour lineColour, boolean tetherable) {
        this.id = id;
        this.shape = shape;
        this.physicsObject = new PhysicsObject(shape, position, mass, momentOfInertia);
        this.fillColour = fillColour;
        this.lineColour = lineColour;
        this.tetherable = tetherable;
        this.radiusMag = getMaxRadius(shape);
    }

    private double getMaxRadius(Shape shape) {
        double maxDistance = 0;

        for(int i = 0; i < shape.polygon.vertexCount; i++) {
            Vector vertex = shape.polygon.vertices[i];
            double mag = vertex.mag();
            if(mag > maxDistance) {
                maxDistance = mag;
            }
        }
        return maxDistance;
    }

    public GameObject(String id, Shape shape, Vector position, double mass, double momentOfInertia, Colour fillColour, Colour lineColour) {
        this.id = id;
        this.shape = shape;
        this.physicsObject = new PhysicsObject(shape, position, mass, momentOfInertia);
        this.fillColour = fillColour;
        this.lineColour = lineColour;
        this.tetherable = true;
        this.radiusMag = getMaxRadius(shape);

    }

    public void applyImpulse(Vector impulse, Vector normal) {
        physicsObject.applyCollisionImpulse(impulse, normal);
    }


    public void draw(Sketch sketch, double scale) {
        Vector[] toDraw = new Vector[shape.polygon.vertexCount];
        for (int i = 0; i < shape.polygon.vertexCount; i++) {
            Vector v = shape.polygon.vertices[i].copy();

            v.multiply(scale);
            v.add(physicsObject.position.multiplyN(scale));
            toDraw[i] = v;
        }

        PShape pShape = sketch.createShape();
        pShape.beginShape();

        pShape.stroke(lineColour.r, lineColour.g, lineColour.b, lineColour.alpha);
        pShape.fill(fillColour.r, fillColour.g, fillColour.b, fillColour.alpha);


        for (Vector aToDraw : toDraw) {
            pShape.vertex((float) aToDraw.x, (float) aToDraw.y);
        }

        pShape.endShape(PConstants.CLOSE);

        sketch.shape(pShape);
        sketch.strokeWeight(1);
        sketch.fill(0, 0, 0);
    }

    public CollisionManifoldData tryCollision(Sketch sketch, GameObject object2) {

        CollisionManifoldData collisionManifest = getCollisionManifest(sketch, this, object2);

        if (collisionManifest != null) {
            collisionManifest.addObjects(this, object2);
        }

        return collisionManifest;
    }


    private CollisionManifoldData getCollisionManifest(Sketch sketch, GameObject object1, GameObject object2) {
        Shape thisShape = object1.shape;
        Shape thatShape = object2.shape;

        if(object1.isStatic() && object2.isStatic()) {
            return null;
        }

        Vector object1PositionDiff = this.physicsObject.position.subtractN(thisShape.centerPoint());
        Vector object2PositionDiff = object2.physicsObject.position.subtractN(thatShape.centerPoint());

        Shape shape1 = thisShape.translateN(object1PositionDiff);
        Shape shape2 = thatShape.translateN(object2PositionDiff);

        Gjk gjk = new Gjk(shape1, shape2, sketch);
        boolean collision = gjk.collision();
        if (collision) {
            Epa epa = new Epa(sketch);
            epa.execute(shape1, shape2, gjk.getSimplex());

            ClippingPoints clippingPoints = new ClippingPoints(sketch);
            CollisionManifoldData collisionManifold = clippingPoints.getCollisionManifold(shape1, shape2, epa.normal);

            if (collisionManifold != null && collisionManifold.getPoints().size() > 0) {
                collisionManifold.addNormal(epa.normal);
                collisionManifold.addDepth(epa.depth);
                return collisionManifold;
            }
        }
        return null;
    }

    private boolean isStatic() {
        return physicsObject.mass == 0.0;
    }

    public void resetForcesAndTorques() {
        physicsObject.forces = new ArrayList<>();
    }

    public boolean tetherable() {
        return tetherable;
    }
}
