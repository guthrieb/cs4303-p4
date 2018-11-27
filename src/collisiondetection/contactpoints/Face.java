package collisiondetection.contactpoints;

import collisiondetection.shapes.Vector;

public class Face {
    private final Vector projectedVertex;
    private final Vector face1;
    private final Vector face2;

    public Face(Vector projectedVertex, Vector face1, Vector face2) {

        this.projectedVertex = projectedVertex;
        this.face1 = face1;
        this.face2 = face2;
    }

    public Vector getVector() {
        return face2.subtractN(face1);
    }

    public Vector getV1() {
        return face1;
    }

    public Vector getV2() {
        return face2;
    }

    public Vector getProjected() {
        return projectedVertex;
    }

    @Override
    public String toString() {
        return "Face{" +
                "projectedVertex=" + projectedVertex +
                ", face1=" + face1 +
                ", face2=" + face2 +
                '}';
    }
}
