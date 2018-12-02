package collisiondetection.epa;

import collisiondetection.shapes.Vector;

class Edge {
    public double distance;
    public Vector normal;
    public int index;

    public Edge() {
        distance = Double.POSITIVE_INFINITY;
        normal = null;
        index = -1;
    }
}
