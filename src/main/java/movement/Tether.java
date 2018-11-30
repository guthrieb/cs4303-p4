package movement;

import collisiondetection.shapes.Vector;

public class Tether {
    private final Vector position;
    private final double length;

    public Vector getPosition() {
        return position;
    }

    public double getLength() {
        return length;
    }

    public Tether(Vector position, double length) {
        this.position = position;
        this.length = length;
    }
}
