package world;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import drawing.Colour;
import gameobjects.GameObject;

public class HealthPowerUp extends GameObject {
    private int health;
    private boolean used = false;

    public HealthPowerUp(String id, Vector position, double mass, double momentOfInertia, int health, Colour fillColour, Colour lineColour) {
        super(id, new Shape(MapComponentVertices.OCTAGON), position, mass, momentOfInertia, false, fillColour, lineColour, false);
        this.health = health;
        this.physicsObject.rotationalDamping = 1;
    }

    public int getHealth() {
        if(!used) {
            used = true;
            return health;
        } else {
            return 0;
        }
    }
}
