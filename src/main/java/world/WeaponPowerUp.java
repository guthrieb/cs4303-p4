package world;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import drawing.Colour;
import gameobjects.GameObject;
import playercontrols.LaserMode;

public class WeaponPowerUp extends GameObject {
    public final String name;
    private final LaserMode mode;

    public WeaponPowerUp(String id, String name, Vector position, double mass, double momentOfInertia, LaserMode mode, Colour fillColour, Colour lineColour) {
        super(id, new Shape(MapComponentVertices.SQUARE), position, mass, momentOfInertia, fillColour, lineColour, false);
        this.mode = mode;
        this.physicsObject.rotationalDamping = 0.99;
        this.name = name;
    }

    public LaserMode getMode() {
        return mode;
    }
}
