package playercontrols;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import drawing.Sketch;
import gameobjects.GameObject;

public class Player extends GameObject {
    private static double BOOST_FORCE = 2000000;
    private final Sketch sketch;
    boolean boosting;
    boolean dropping;
    boolean rotating;
    Mode mode = Mode.MOVEMENT;
    private double rotationTorque;
    private RotationDirection currentRotation = RotationDirection.NO_ROTATION;
    private static final double ACCELERATION_RATE = 0.1;
    private Shape boostingShape = new Shape(PlayerShapes.BOOSTING_SHAPE);
    private Shape tetheredShape = new Shape(PlayerShapes.TETHERED_SHAPE);
    private Shape droppingShape = new Shape(PlayerShapes.DROPPER_VERTICES);

    public void setRotating(RotationDirection direction) {
        this.currentRotation = direction;
    }

    public enum RotationDirection {
        LEFT, RIGHT, NO_ROTATION
    }

    public void setBoosting(boolean b) {
        this.boosting = b;
    }


    public enum Mode{
        MOVEMENT, DROPPER, TETHER
    }

    public void update() {
        addBoostForce();
        addRotationForce();
    }

    public void addBoostForce() {
        if(boosting) {
            double xForceComponent = BOOST_FORCE *Math.cos(physicsObject.orientation);
            double yForceComponent = BOOST_FORCE *Math.sin(physicsObject.orientation);


            Vector realWorldCenterPoint = shape.centerPoint().addN(physicsObject.position);
            Vector force = new Vector(xForceComponent, yForceComponent);
//            sketch.point(physicsObject.position);
//            sketch.line(realWorldCenterPoint, realWorldCenterPoint.addN(force));

            physicsObject.addForce("", force, realWorldCenterPoint, false);

        }
    }

    public void addRotationForce() {
        if(currentRotation != RotationDirection.NO_ROTATION) {
            if(RotationDirection.RIGHT == currentRotation) {
                physicsObject.addRotationalAcceleration(ACCELERATION_RATE);
            } else {
                physicsObject.addRotationalAcceleration(-ACCELERATION_RATE);
            }
        }
    }

    public Player(Sketch sketch, Shape shape, Vector position, double mass, double momentOfInertia) {
        super(shape, position, mass, momentOfInertia);
        this.sketch = sketch;
    }

    public void changeMode(Mode toChange) {
        this.mode = toChange;
        this.boosting = false;
        this.dropping = false;
        switch (toChange) {
            case MOVEMENT:
                this.shape = new Shape(PlayerShapes.BOOSTING_SHAPE);
                this.shape.rotate(physicsObject.orientation);
                break;
            case TETHER:
                this.shape = new Shape(PlayerShapes.TETHERED_SHAPE);
                this.shape.rotate(physicsObject.orientation);
                break;
            case DROPPER:
                this.shape = new Shape(PlayerShapes.DROPPER_VERTICES);
                this.shape.rotate(physicsObject.orientation);
                break;
        }
    }

    public GameObject fire() {
        if(mode == Mode.MOVEMENT) {
//            return new Shell();
        }
        return null;
    }

    public void setOrientation(double theta) {
        shape.rotate(theta - physicsObject.orientation);
        physicsObject.orientation = theta;
    }


}
