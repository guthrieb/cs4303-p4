package gameobjects;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import drawing.Colour;

public class DestroyerBeam extends GameObject {
    public DestroyerBeam(String id, Shape shape, Vector position) {
        super(id, shape, position, 0, 0, new Colour(0, 0, 255, 100), new Colour(0, 0, 255, 100), false);
    }
}
