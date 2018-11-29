package playercontrols;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import drawing.Colour;
import drawing.Sketch;
import gameobjects.GameObject;
import helpers.Timer;
import movement.LineMath;
import movement.Tether;
import movement.TetherDirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player extends GameObject {
    private static final double BOOSTER_ROTATIONAL_DAMPING = 0.96;
    private static final double BOOSTER_LINEAR_DAMPING = 0.989;
    private static final double DROPPER_ROTATIONAL_DAMPING = 0;
    private static final double DROPPER_LINEAR_DAMPING = 1;
    private static final double TETHER_LINEAR_DAMPING = 1;
    private static final double TETHER_ROTATIONAL_DAMPING = 1;
    private static final String DROPPER_PAUSE = "dropper_pause";
    private static final double DROP_FORCE = 3000000;
    private static final double BOOST_FORCE = 2000000;
    private static final int TIME_BEFORE_DROP = 500;
    private static final double DAMAGE_SPEED_LIMIT = 400;
    private static final int TETHER_LENGTH = 500;
    private LaserMode firingMode = LaserMode.shotgunLasers();

    static int shellNo = 0;


    private final Sketch sketch;


    private boolean firing = false;
    private boolean boosting;
    private boolean dropping;
    private boolean tethered;
    public Tether tether;

    private HashMap<String, Timer> timers = new HashMap<>();
    private Mode mode = Mode.MOVEMENT;
    private RotationDirection currentRotation = RotationDirection.NO_ROTATION;
    private static final double ACCELERATION_RATE = 0.1;


    int maxHealth = 1000;
    int remainingHealth = 1000;
    private double laserPower = 100000000;


    public void setRotating(RotationDirection direction) {
        this.currentRotation = direction;
    }

    public void attachTether(Vector vector) {
        tethered = true;
        this.tether = new Tether(vector, vector.subtractN(physicsObject.position).mag());
    }

    public void untether() {
        tethered = false;
    }

    public Mode getMode() {
        return mode;
    }

    public enum RotationDirection {
        LEFT, RIGHT, NO_ROTATION
    }

    public void setBoosting(boolean b) {
        this.boosting = b;
    }


    public enum Mode {
        MOVEMENT, DROPPER, TETHER
    }

    public void addTether(TetherDirection direction, List<GameObject> tetherableObject) {

        double tetherOrientation;
        if (direction == TetherDirection.LEFT) {
            tetherOrientation = physicsObject.orientation - Math.PI / 2;

        } else {
            tetherOrientation = physicsObject.orientation + Math.PI / 2;
        }
        double tetherDirectionX = Math.cos(tetherOrientation);
        double tetherDirectionY = Math.sin(tetherOrientation);

        Vector tetherDirection = new Vector(tetherDirectionX, tetherDirectionY);

        LineMath lineMath = new LineMath(tetherDirection.multiplyN(TETHER_LENGTH).mag());
        GameObject closestIntersectingObject = lineMath.getClosestIntersectingObject(this,
                physicsObject.position, physicsObject.position,
                physicsObject.position.addN(tetherDirection.multiplyN(TETHER_LENGTH)), tetherableObject, true);

        if (closestIntersectingObject != null) {
            attachTether(closestIntersectingObject.physicsObject.position);
        }
    }


    public void update(List<GameObject> objects) {
        physicsObject.addForce("", physicsObject.mg, physicsObject.position, false);

        if (mode == Mode.MOVEMENT) {
            updateMovementMode(objects);
        } else if (mode == Mode.TETHER) {
            updateTetherMode();
        } else if (mode == Mode.DROPPER) {
            updateDropperMode();
        }
    }

    private void updateDropperMode() {
        analyseDamages();
        if (timers.get(DROPPER_PAUSE).completed()) {
            physicsObject.addForce("Dropping", new Vector(0, DROP_FORCE), physicsObject.position, false);
            physicsObject.yVelDamping = 1;
        }
    }

    private void updateTetherMode() {
        addTetherForce();
    }

    private void updateMovementMode(List<GameObject> objects) {
        analyseDamages();
        addBoostForce();
        addRotationForce();


        handleFiring(objects);
//        LineMath lineMath = new LineMath(Double.MAX_VALUE);

//        if((shell = handleFiring()) != null){
//            PhysicsLoop loop = sketch.physicsLoop;
//            loop.objects.add(shell);
//        }
    }

    public void addBoostForce() {
        if (boosting) {
            double xForceComponent = BOOST_FORCE * Math.cos(physicsObject.orientation);
            double yForceComponent = BOOST_FORCE * Math.sin(physicsObject.orientation);

            Vector realWorldCenterPoint = shape.centerPoint().addN(physicsObject.position);
            Vector force = new Vector(xForceComponent, yForceComponent);

            physicsObject.addForce("boost", force, realWorldCenterPoint, false);

        }
    }

    public void addRotationForce() {
        if (currentRotation != RotationDirection.NO_ROTATION) {
            if (RotationDirection.RIGHT == currentRotation) {
                physicsObject.addRotationalAcceleration(ACCELERATION_RATE);
            } else {
                physicsObject.addRotationalAcceleration(-ACCELERATION_RATE);
            }
        }
    }

    public Player(String id, Sketch sketch, Shape shape, Vector position, double mass, double momentOfInertia, Colour fillColour, Colour lineColour) {
        super(id, shape, position, mass, momentOfInertia, true, fillColour, lineColour);
        this.sketch = sketch;
        addTimers();
    }

    private void addTimers() {
        timers.put(DROPPER_PAUSE, new Timer(TIME_BEFORE_DROP));
    }

    public void analyseDamages() {
        for (double contactVelocity : physicsObject.getImpulseCollisions()) {
            double absContactVel = Math.abs(contactVelocity);
            if (absContactVel > DAMAGE_SPEED_LIMIT) {
                remainingHealth -= (absContactVel - DAMAGE_SPEED_LIMIT) * 0.1;
            }
        }
        physicsObject.setImpulseCollisions(new ArrayList<>());
    }


    public void changeMode(Mode toChange) {
        if (this.mode != toChange) {
            this.mode = toChange;
            this.boosting = false;
            this.dropping = false;
            switch (toChange) {
                case MOVEMENT:
                    changeToBooster();
                    break;
                case TETHER:
                    changeToTether();
                    break;
                case DROPPER:
                    changeToDropper();
                    break;
            }
        }
    }

    private void changeToBooster() {
        this.physicsObject.orientation = getMovementDirection();

        this.shape = new Shape(PlayerShapes.BOOSTING_VERTICES);
        this.shape.rotate(physicsObject.orientation);

        this.physicsObject.rotationalDamping = BOOSTER_ROTATIONAL_DAMPING;
        this.physicsObject.linearDamping = BOOSTER_LINEAR_DAMPING;
        physicsObject.resetDirectionalDamping();

        this.mode = Mode.MOVEMENT;
    }

    private double getMovementDirection() {
        return Math.atan2(physicsObject.velocity.y, physicsObject.velocity.x);
    }

    private void changeToDropper() {
        this.physicsObject.orientation = 3 * Math.PI / 2;
        this.shape = new Shape(PlayerShapes.DROPPER_VERTICES);
        this.shape.rotate(physicsObject.orientation);

        this.physicsObject.rotationalDamping = DROPPER_ROTATIONAL_DAMPING;
        this.physicsObject.linearDamping = DROPPER_LINEAR_DAMPING;
        this.physicsObject.xVelDamping = 0.9;
        this.physicsObject.yVelDamping = 0.9;
        timers.get(DROPPER_PAUSE).reset();

        this.mode = Mode.DROPPER;
    }

    private void changeToTether() {
        this.shape = new Shape(PlayerShapes.TETHERED_VERTICES);
        this.shape.rotate(physicsObject.orientation);

        this.physicsObject.rotationalDamping = TETHER_ROTATIONAL_DAMPING;
        this.physicsObject.linearDamping = TETHER_LINEAR_DAMPING;
        physicsObject.resetDirectionalDamping();


        tethered = false;
        this.mode = Mode.TETHER;
    }


    public void addTetherForce() {
        if (tethered) {
            sketch.point(tether.getPosition());


            double mass = physicsObject.mass;
            double radius = tether.getLength();
            Vector gravForce = physicsObject.mg;
            Vector position = physicsObject.position;

            Vector radiusVector = position.subtractN(tether.getPosition());

            sketch.line(tether.getPosition(), tether.getPosition().addN(radiusVector));

            Vector perpVector = radiusVector.cross(-1);

            perpVector.normalize();

            double velocityInPerp = Vector.dot(perpVector, physicsObject.velocity) / perpVector.mag();


            double theta = Math.atan2(position.y - tether.getPosition().y, position.x - tether.getPosition().x);

            double componentOfGravity = gravForce.mag() * Math.sin(theta);

            double forceMag = Math.abs((velocityInPerp * velocityInPerp * mass) / radius + componentOfGravity);


            Vector thetaComponents = new Vector(Math.cos(theta), Math.sin(theta)).multiplyN(200);
            Vector thetaComponents2 = thetaComponents.negateN();
            thetaComponents2.normalize();


            if (radiusVector.mag() > tether.getLength()) {
                double componentInDirection = Vector.dot(physicsObject.velocity, radiusVector) / radiusVector.mag();

                double x = componentInDirection * Math.cos(Math.atan2(radiusVector.y, radiusVector.x));
                double y = componentInDirection * Math.sin(Math.atan2(radiusVector.y, radiusVector.x));

                physicsObject.velocity.subtract(new Vector(x, y));

                radiusVector.multiply(1 - ((radiusVector.mag() - tether.getLength()) / radiusVector.mag()));
                physicsObject.position = tether.getPosition().addN(radiusVector);

            }

            Vector force = thetaComponents2.multiplyN(forceMag);
            physicsObject.addForce("id", force, physicsObject.position, false);

        }
    }

    public Vector collisionOfRay = null;

    List<Vector> intersectingObject = new ArrayList<>();

    public void handleFiring(List<GameObject> objects) {

        double xComponent = Math.cos(physicsObject.orientation);
        double yComponent = Math.sin(physicsObject.orientation);

        Vector firingOrientation = new Vector(xComponent, yComponent);
        sketch.line(this.physicsObject.position.multiplyN(0.5), this.physicsObject.position.multiplyN(0.5).addN(firingOrientation.multiplyN(100)));
        if (firing) {
            firing = false;
            firingOrientation.normalize();


            List<Laser> lasers = new ArrayList<>();
            double rightLasers;
            double leftLasers;
            List<Vector> orientations = new ArrayList<>();
            if(firingMode.noOfLasers % 2 == 0) {
                rightLasers = firingMode.noOfLasers/2.0;
                leftLasers = firingMode.noOfLasers/2.0;
            } else {
                rightLasers = (firingMode.noOfLasers - 1)/2.0;
                leftLasers = (firingMode.noOfLasers - 1)/2.0;
            }
            orientations.add(firingOrientation);

            double leftOrientation = physicsObject.orientation;
            double spreadChange = firingMode.spreadBetweenLasers / rightLasers;
            for(int i = 0; i < leftLasers; i++) {
                leftOrientation -= firingMode.spreadBetweenLasers/leftLasers;
                System.out.println(leftOrientation);

                double laserXComponent = Math.cos(leftOrientation);
                double laserYComponent = Math.sin(leftOrientation);
                orientations.add(new Vector(laserXComponent, laserYComponent));
            }

            double rightOrientation = physicsObject.orientation;
            for(int i = 0; i < rightLasers; i++) {
                rightOrientation += spreadChange;
                System.out.println(rightOrientation);

                double laserXComponent = Math.cos(rightOrientation);
                double laserYComponent = Math.sin(rightOrientation);
                orientations.add(new Vector(laserXComponent, laserYComponent));
            }

            for (Vector orientation : orientations) {
                Laser laserCollision = getLaserCollision(objects, orientation, firingMode.laserLength);
                lasers.add(laserCollision);
                laserCollision.execute();
            }
            sketch.lasers.addAll(lasers);
        }
    }

    private Laser getLaserCollision(List<GameObject> objects, Vector firingOrientation, double distanceLimit) {
        double closestIntersectionDist = Double.MAX_VALUE;
        Vector closestIntersection = null;
        GameObject closestObject = null;


        for (GameObject object : objects) {
            if (!object.equals(this)) {
                boolean intersected = LineMath.objectIntersected(this.physicsObject.position, firingOrientation, object);


                if (intersected) {

                    Vector centerPoint = object.physicsObject.position;
                    intersectingObject.add(centerPoint);

                    Vector intersection = LineMath.getClosestIntersection(this.physicsObject.position, firingOrientation, object);
                    sketch.point(intersection.multiplyN(Sketch.SCALE));
                    Vector thisToIntersection = intersection.subtractN(this.physicsObject.position);

                    double mag = thisToIntersection.mag();
                    if (mag < closestIntersectionDist && mag < distanceLimit) {
                        closestObject = object;
                        closestIntersectionDist = mag;
                        closestIntersection = intersection;
                    }
                }
            }
        }

        if(closestIntersection == null) {
            closestIntersection = physicsObject.position.addN(firingOrientation.multiplyN(distanceLimit));
        }

        return new Laser(physicsObject.position.copy(), closestIntersection,
                firingOrientation.multiplyN(laserPower*firingMode.laserMod), closestObject);
//        return closestIntersection;
    }


    @Override
    public void draw(Sketch sketch, double scale) {
        super.draw(sketch, scale);
        if (mode == Mode.TETHER && tethered) {
            sketch.line(tether.getPosition().multiplyN(scale), physicsObject.position.multiplyN(scale));
        }

        if (collisionOfRay != null) {
            sketch.point(collisionOfRay.multiplyN(scale));
            sketch.point(collisionOfRay.multiplyN(scale));
            sketch.line(collisionOfRay.multiplyN(scale), physicsObject.position.multiplyN(scale));
        }

        for (Vector vector : intersectingObject) {
            sketch.point(vector.multiplyN(scale));
        }
        intersectingObject = new ArrayList<>();
    }

    public void setFiring(boolean firing) {
        this.firing = firing;
    }

    public int getRemainingHealth() {
        return remainingHealth;
    }

    public double getPercentageRemainingHealth() {
        return (double)getRemainingHealth()/(double)maxHealth;
    }
}
