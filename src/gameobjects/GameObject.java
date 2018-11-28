package gameobjects;

import collisiondetection.contactpoints.CollisionManifoldData;
import collisiondetection.contactpoints.ClippingPoints;
import collisiondetection.epa.Epa;
import collisiondetection.gjk.Gjk;
import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import collisionresponse.PhysicsObject;
import drawing.Sketch;
import processing.core.PApplet;

import java.util.ArrayList;

public class GameObject {
    public Shape shape;
    public collisionresponse.PhysicsObject physicsObject;


    public GameObject(Shape shape, Vector position, double mass, double momentOfInertia) {
        this.shape = shape;
        this.physicsObject = new PhysicsObject(shape, position, mass, momentOfInertia);
    }

    public void applyImpulse(Vector impulse, Vector normal) {
        physicsObject.applyCollisionImpulse(impulse, normal);
    }


    public void draw(PApplet sketch, double scale) {
        if (colliding) {
            sketch.fill(255, 0, 0);
        }

        Vector[] toDraw = new Vector[shape.polygon.vertexCount];
        for (int i = 0; i < shape.polygon.vertexCount; i++) {
            Vector v = shape.polygon.vertices[i].copy();
            v.multiply(scale);

            v.add(physicsObject.position);
            toDraw[i] = v;
        }
//        System.out.println(physicsObject.position);


        for (int i = 0; i < toDraw.length; i++) {
            int j;
            if (((j = i + 1) == toDraw.length)) {
                j = 0;
            }

            sketch.line((float) toDraw[i].x, (float) toDraw[i].y, (float) toDraw[j].x, (float) toDraw[j].y);
        }

        sketch.strokeWeight(1);
        sketch.fill(0, 0, 0);
    }

    boolean colliding = false;

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


            if (collisionManifold.getPoints().size() > 0) {

                collisionManifold.addNormal(epa.normal);
//                System.out.println(epa.type);
                collisionManifold.addDepth(epa.depth);
                System.out.println(epa.depth);


                sketch.stroke(0, 255, 0);
                sketch.line(collisionManifold.getPoints().get(0), collisionManifold.getPoints().get(0).addN(epa.normal.multiplyN(100)));
                sketch.stroke(0, 0, 0);
                return collisionManifold;
            }
        }
        return null;
    }

    public void addForce(String id, Vector vector) {
        physicsObject.addForce(id, vector, shape.centerPoint());
    }

    public void resetForcesAndTorques() {
        physicsObject.forces = new ArrayList<>();
    }

    public Vector getCenterPoint() {
        return shape.centerPoint().addN(physicsObject.position);
    }

    public void drawOrientation(Sketch sketch) {

        Vector centerPoint = getCenterPoint();
        Vector orientationPoint = new Vector(20*Math.cos(physicsObject.orientation), 20*Math.sin(physicsObject.orientation));

        sketch.line(centerPoint, centerPoint.addN(orientationPoint));
    }

}
