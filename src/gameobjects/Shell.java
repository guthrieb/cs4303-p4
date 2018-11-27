package gameobjects;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;

public class Shell extends GameObject {
    public Shell(Shape shape, Vector position, double mass, double momentOfInertia) {
        super(shape, position, mass, momentOfInertia);
    }
}
