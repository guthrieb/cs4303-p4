package playercontrols;

import drawing.Colour;
import collisiondetection.shapes.Vector;
import drawing.Sketch;
import gameobjects.GameObject;
import helpers.Timer;

public class Laser {
    private static final int TIME_LIMIT = 500;
    private final Vector pointOfOrigin;
    private final Vector pointOfCollision;
    private final GameObject colliding;
    private final Colour colour;
    private final Timer timeToExists = new Timer(TIME_LIMIT);
    private final Vector force;
    private boolean faded = false;
    private double forceMod;

    public Laser(Vector pointOfOrigin, Vector pointOfCollision, GameObject colliding, Vector force, Colour colour) {
        this.force = force;
        this.pointOfOrigin = pointOfOrigin;
        this.pointOfCollision = pointOfCollision;
        this.colliding = colliding;
        this.colour = colour;
        this.timeToExists.reset();
    }

    public Laser(Vector pointOfOrigin, Vector pointOfCollision, Vector force, GameObject colliding) {
        this.force = force;
        this.pointOfOrigin = pointOfOrigin;
        this.pointOfCollision = pointOfCollision;
        this.colliding = colliding;
        this.colour = new Colour(255, 0 ,0);
        this.timeToExists.reset();
    }

    public void execute() {
        if(colliding != null) {
            colliding.physicsObject.addForce("laser_collision", force, pointOfCollision, true);
        }
    }

    public void draw(Sketch sketch, double scale) {
        double remaining = timeToExists.percentageRemaining();
        remaining *= 255;
        sketch.strokeWeight(3);
        sketch.stroke(colour.r, colour.g, colour.b, (float)remaining);
        sketch.line(pointOfOrigin.multiplyN(scale), pointOfCollision.multiplyN(scale));
        sketch.strokeWeight(1);
    }

    public boolean isFaded() {
        return timeToExists.percentageRemaining() <= 0;
    }
}
