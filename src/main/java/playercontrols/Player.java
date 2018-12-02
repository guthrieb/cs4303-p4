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
    private static final int BOOSTER_MASS = 2132;
    private static final int BOOSTER_MOMENT_INERTIA = 838101;

    public LaserMode firingMode = LaserMode.laserRifleMode();


    private final Sketch sketch;
    public final Colour playerColour;


    private boolean firing = false;
    private boolean boosting;
    private boolean tethered;
    private Tether tether;

    private final HashMap<String, Timer> timers = new HashMap<>();
    private Mode mode = Mode.MOVEMENT;
    private RotationDirection currentRotation = RotationDirection.NO_ROTATION;
    private static final double ACCELERATION_RATE = 0.1;


    private final int maxHealth = 1000;
    private int remainingHealth = 1000;
    private boolean dead = false;
    private final Timer trailTimer = new Timer(100);


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
        Sketch.noOfPowerups--;
    }

    public void collect(HealthPowerUp powerUp) {
        this.remainingHealth += powerUp.getHealth();

        if (remainingHealth > maxHealth) {
            remainingHealth = maxHealth;
        }
        Sketch.noOfPowerups--;

    }

    public boolean dead() {
        return dead;
    }

    public void destroy() {
        sketch.playerHashMap.get(Sketch.DESTROYED_KEY).play();
        sketch.playerHashMap.get(Sketch.DESTROYED_KEY).rewind();

        remainingHealth = 0;
        lineColour = playerColour;
        fillColour = new Colour(0, 0, 0);
        boosting = false;
        currentRotation = RotationDirection.NO_ROTATION;
        this.dead = true;
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

    public void addTether(TetherDirection direction, List<GameObject> objects) {

        double tetherOrientation;
        if (direction == TetherDirection.LEFT) {
            tetherOrientation = physicsObject.orientation - Math.PI / 2;
        } else {
            tetherOrientation = physicsObject.orientation + Math.PI / 2;
        }


        double tetherDirectionX = Math.cos(tetherOrientation);
        double tetherDirectionY = Math.sin(tetherOrientation);

        Vector tetherDirection = new Vector(tetherDirectionX, tetherDirectionY);

        double closestIntersectionDist = Double.MAX_VALUE;
        Vector closestIntersection = null;
        GameObject closestIntersectingObject = null;
        for (GameObject object : objects) {
            if (!object.equals(this) && object.tetherable()) {
                boolean intersected = LineMath.objectIntersected(this.physicsObject.position, tetherDirection, object);


                if (intersected) {

                    Vector intersection = LineMath.getClosestIntersection(this.physicsObject.position, tetherDirection, object);
                    Vector thisToIntersection = intersection.subtractN(this.physicsObject.position);

                    double mag = thisToIntersection.mag();
                    if (mag < closestIntersectionDist && mag < TETHER_LENGTH) {
                        closestIntersectingObject = object;
                        closestIntersectionDist = mag;
                        closestIntersection = intersection;
                    }
                }
            }
        }

        if (closestIntersectingObject != null) {
            attachTether(closestIntersection);
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
        analyseDamages(1);
        if (timers.get(DROPPER_PAUSE).completed()) {
            physicsObject.addForce("dropping_force", new Vector(0, DROP_FORCE), physicsObject.position, false);
            physicsObject.yVelDamping = 1;
        }
    }

    private void updateTetherMode() {
        analyseDamages(0.4);
        addTetherForce();
    }


    private void updateMovementMode(List<GameObject> objects) {
        analyseDamages(1);
        addBoostForce();
        addRotationForce();
        handleFiring(objects);


        if(boosting && trailTimer.completed()) {
            double xComponent = Math.cos(physicsObject.orientation);
            double yComponent = Math.sin(physicsObject.orientation);
            Vector orientationVector = new Vector(xComponent, yComponent);
            orientationVector.normalize();

            sketch.addTrail(new Trail(this.sketch, this.physicsObject.position.addN(orientationVector.multiplyN(10).negateN()), playerColour));
            trailTimer.reset();
        }
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
        super(id, shape, position, mass, momentOfInertia, fillColour, lineColour, false);
        this.playerColour = fillColour;
        this.sketch = sketch;
        addTimers();
    }

    private void addTimers() {
        timers.put(DROPPER_PAUSE, new Timer(TIME_BEFORE_DROP));
    }


    private void analyseDamages(double mod) {
        for (double contactVelocity : physicsObject.getImpulseCollisions()) {
            double absContactVel = Math.abs(contactVelocity);
            if (absContactVel > DAMAGE_SPEED_LIMIT) {
                remainingHealth -= ((absContactVel - DAMAGE_SPEED_LIMIT) * 0.1)*mod;
            }
        }


        if(remainingHealth <= 0) {
            if(remainingHealth < 0) {
                remainingHealth = 0;
            }
            if(!dead) {
                destroy();
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
        physicsObject.invMass = 1.0/ BOOSTER_MASS;
        physicsObject.invInertia = 1.0/ BOOSTER_MOMENT_INERTIA;

        this.physicsObject.rotationalDamping = BOOSTER_ROTATIONAL_DAMPING;
        this.physicsObject.linearDamping = BOOSTER_LINEAR_DAMPING;
        physicsObject.resetDirectionalDamping();
        this.physicsObject.elasticity = 0;
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
        this.physicsObject.elasticity = 0;

        timers.get(DROPPER_PAUSE).reset();
        this.mode = Mode.DROPPER;
    }

    private void changeToTether() {
        this.shape = new Shape(PlayerShapes.TETHERED_VERTICES);
        this.shape.rotate(physicsObject.orientation);
        this.physicsObject.invMass = 1.0/3000.0;
        this.physicsObject.invInertia = 1.0/1570000.0;

        this.physicsObject.rotationalDamping = TETHER_ROTATIONAL_DAMPING;
        this.physicsObject.linearDamping = TETHER_LINEAR_DAMPING;
        physicsObject.resetDirectionalDamping();
        this.physicsObject.elasticity = 0.5;

        tethered = false;
        this.mode = Mode.TETHER;
    }


    private void addTetherForce() {
        if (tethered) {

            double mass = physicsObject.mass;
            double radius = tether.getLength();
            Vector gravForce = physicsObject.mg;
            Vector position = physicsObject.position;

            Vector radiusVector = position.subtractN(tether.getPosition());

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
            physicsObject.addForce("tether_force", force, physicsObject.position, false);
        }
    }

    private final Vector collisionOfRay = null;
    private void handleFiring(List<GameObject> objects) {
        double xComponent = Math.cos(physicsObject.orientation);
        double yComponent = Math.sin(physicsObject.orientation);

        Vector firingOrientation = new Vector(xComponent, yComponent);

        if (firing) {
            if (!firingMode.automatic) {
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
    }



    public void draw(Sketch sketch, double scale) {
        super.draw(sketch, scale);
        if (mode == Mode.TETHER) {
            if(tethered) {
                sketch.stroke(125, 249, 255, 100);
                sketch.strokeWeight(5);
                sketch.line(tether.getPosition().multiplyN(scale), physicsObject.position.multiplyN(scale));
                sketch.strokeWeight(1);
                sketch.stroke(0, 0, 0);
            } else {
                double tetherOrientation1;
                double tetherOrientation2;

                tetherOrientation1 = physicsObject.orientation - Math.PI / 2;
                tetherOrientation2 = physicsObject.orientation + Math.PI / 2;


                double tetherDirectionX1 = Math.cos(tetherOrientation1);
                double tetherDirectionX2 = Math.cos(tetherOrientation2);
                double tetherDirectionY1 = Math.sin(tetherOrientation1);
                double tetherDirectionY2 = Math.sin(tetherOrientation2);

                Vector potentialTether1 = new Vector(tetherDirectionX1, tetherDirectionY1).multiplyN(TETHER_LENGTH).addN(physicsObject.position);
                Vector potentialTether2 = new Vector(tetherDirectionX2, tetherDirectionY2).multiplyN(TETHER_LENGTH).addN(physicsObject.position);

                sketch.stroke(playerColour.r, playerColour.g, playerColour.b, 100);
                sketch.fill(playerColour.r, playerColour.g, playerColour.b, 100);
                sketch.line(physicsObject.position.multiplyN(scale), potentialTether1.multiplyN(scale));
                sketch.line(physicsObject.position.multiplyN(scale), potentialTether2.multiplyN(scale));
                sketch.stroke(0, 0, 0);
                sketch.strokeWeight(0);
            }

        }

        if (collisionOfRay != null) {
            sketch.point(collisionOfRay.multiplyN(scale));
            sketch.point(collisionOfRay.multiplyN(scale));
            sketch.line(collisionOfRay.multiplyN(scale), physicsObject.position.multiplyN(scale));
        }
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
