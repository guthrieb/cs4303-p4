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
import world.HealthPowerUp;
import world.WeaponPowerUp;

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
    public LaserMode firingMode = LaserMode.megaLaserMode();


    private final Sketch sketch;


    private boolean firing = false;
    private boolean boosting;
    private boolean tethered;
    private Tether tether;

    private HashMap<String, Timer> timers = new HashMap<>();
    private Mode mode = Mode.MOVEMENT;
    private RotationDirection currentRotation = RotationDirection.NO_ROTATION;
    private static final double ACCELERATION_RATE = 0.1;


    private int maxHealth = 1000;
    private int remainingHealth = 1000;


    public void setRotating(RotationDirection direction) {
        this.currentRotation = direction;
    }

    private void attachTether(Vector vector) {
        tethered = true;
        this.tether = new Tether(vector, vector.subtractN(physicsObject.position).mag());
    }

    public Mode getMode() {
        return mode;
    }

    public void collect(WeaponPowerUp powerUp) {
        this.firingMode = powerUp.getMode();
    }

    public void collect(HealthPowerUp powerUp) {
        this.remainingHealth += powerUp.getHealth();
        System.out.println(this.remainingHealth);
//        System.out.println(this.maxHealth);
        if (remainingHealth > maxHealth) {
            remainingHealth = maxHealth;
        }
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
        Vector position = physicsObject.position;
        GameObject closestIntersectingObject = lineMath.getClosestIntersectingObject(this,
                position, position,
                position.addN(tetherDirection.multiplyN(TETHER_LENGTH)), tetherableObject, true);

        if (closestIntersectingObject != null) {
            attachTether(closestIntersectingObject.physicsObject.position);
        }
    }


    public void update(List<GameObject> objects) {
        physicsObject.addForce("", physicsObject.mg, physicsObject.position, false);

        if (mode == Mode.MOVEMENT) {
            updatePlayer(objects);
        } else if (mode == Mode.TETHER) {
            updateTetherMode();
        } else if (mode == Mode.DROPPER) {
            updateDropperMode();
        }
    }

    private void updateDropperMode() {
        analyseDamages();
        if (timers.get(DROPPER_PAUSE).completed()) {
            physicsObject.addForce("dropping_force", new Vector(0, DROP_FORCE), physicsObject.position, false);
            physicsObject.yVelDamping = 1;
        }
    }

    private void updateTetherMode() {
        addTetherForce();
    }

    private void updatePlayer(List<GameObject> objects) {
        analyseDamages();
        addBoostForce();
        addRotationForce();
        handleFiring(objects);
    }

    private void addBoostForce() {
        if (boosting) {
            double xForceComponent = BOOST_FORCE * Math.cos(physicsObject.orientation);
            double yForceComponent = BOOST_FORCE * Math.sin(physicsObject.orientation);

            Vector realWorldCenterPoint = shape.centerPoint().addN(physicsObject.position);
            Vector force = new Vector(xForceComponent, yForceComponent);

            physicsObject.addForce("boost", force, realWorldCenterPoint, false);

        }
    }

    private void addRotationForce() {
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


    private void analyseDamages() {
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


    private void addTetherForce() {
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

    private Vector collisionOfRay = null;

    private List<Vector> intersectingObject = new ArrayList<>();

    private void handleFiring(List<GameObject> objects) {
        double xComponent = Math.cos(physicsObject.orientation);
        double yComponent = Math.sin(physicsObject.orientation);

        Vector firingOrientation = new Vector(xComponent, yComponent);
        sketch.line(this.physicsObject.position.multiplyN(0.5), this.physicsObject.position.multiplyN(0.5).addN(firingOrientation.multiplyN(100)));

        if (firing) {
            if (!firingMode.automatic) {
                System.out.println("Firing off");
                firing = false;
            }

            if (firingMode.readyToFire()) {
                firingOrientation.normalize();


                List<Laser> lasers = new ArrayList<>();
                double rightLasers;
                double leftLasers;
                List<Vector> orientations = new ArrayList<>();
                if (firingMode.noOfLasers % 2 == 0) {
                    rightLasers = firingMode.noOfLasers / 2.0;
                    leftLasers = firingMode.noOfLasers / 2.0;
                } else {
                    rightLasers = (firingMode.noOfLasers - 1) / 2.0;
                    leftLasers = (firingMode.noOfLasers - 1) / 2.0;
                }
                orientations.add(firingOrientation);

                double leftOrientation = physicsObject.orientation;
                double spreadChange = firingMode.spreadBetweenLasers / rightLasers;
                for (int i = 0; i < leftLasers; i++) {
                    leftOrientation -= firingMode.spreadBetweenLasers / leftLasers;

                    double laserXComponent = Math.cos(leftOrientation);
                    double laserYComponent = Math.sin(leftOrientation);
                    orientations.add(new Vector(laserXComponent, laserYComponent));
                }

                double rightOrientation = physicsObject.orientation;
                for (int i = 0; i < rightLasers; i++) {
                    rightOrientation += spreadChange;

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
                sketch.playerHashMap.get(firingMode.soundKey).play();
                sketch.playerHashMap.get(firingMode.soundKey).rewind();
            }
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

        if (closestIntersection == null) {
            closestIntersection = physicsObject.position.addN(firingOrientation.multiplyN(distanceLimit));
        }

        double laserPower = 100000000;
        return new Laser(physicsObject.position.copy(), closestIntersection,
                firingOrientation.multiplyN(laserPower * firingMode.laserMod), closestObject, fillColour);
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

    private int getRemainingHealth() {
        return remainingHealth;
    }

    public double getPercentageRemainingHealth() {
        return (double) getRemainingHealth() / (double) maxHealth;
    }
}