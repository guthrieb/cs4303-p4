package world;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import drawing.Colour;
import gameobjects.GameObject;
import playercontrols.LaserMode;

public class WeaponPowerUp extends GameObject {
    private LaserMode mode;

    public WeaponPowerUp(String id, Vector position, double mass, double momentOfInertia, LaserMode mode, Colour fillColour, Colour lineColour) {
        super(id, new Shape(MapComponentVertices.SQUARE), position, mass, momentOfInertia, false, fillColour, lineColour);
        this.mode = mode;
        this.physicsObject.rotationalDamping = 0;
    }

    public LaserMode getMode() {
        return mode;
    }
}
