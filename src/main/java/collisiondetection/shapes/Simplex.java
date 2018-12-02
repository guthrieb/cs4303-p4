package collisiondetection.shapes;

import java.util.ArrayList;
import java.util.List;

public class Simplex {
    private final List<Vector> vertices = new ArrayList<>();
    private Vector lastAdded = null;

    public void add(Vector toAdd) {
        vertices.add(toAdd);
        lastAdded = toAdd;
    }

    public Vector getLast() {
        return lastAdded;
    }

    public int npoints() {
        return vertices.size();
    }


    public List<Vector> getOtherVertices() {
        List<Vector> vectors = new ArrayList<>();
        for (Vector vertice : vertices) {
            if (vertice != lastAdded) {
                vectors.add(vertice);
            }
        }
        return vectors;
    }

    public void remove(Vector c) {
        vertices.remove(c);
    }

    @Override
    public String toString() {
        return "Simplex{" +
                "vertices=" + vertices +
                '}';
    }

    public List<Vector> getVertices() {
        return vertices;
    }

    public void insert(Vector p, int index) {
        vertices.add(index, p);
    }

}
