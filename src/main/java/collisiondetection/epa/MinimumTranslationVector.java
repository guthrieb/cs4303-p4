package collisiondetection.epa;

import collisiondetection.shapes.Vector;

public class MinimumTranslationVector {
    private final double overlap;
    private final Vector smallestAxis;

    public MinimumTranslationVector(double overlap, Vector smallestAxis) {
        this.overlap = overlap;
        this.smallestAxis = smallestAxis;
    }
}
