package collisiondetection.shapes;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class Simplex {
    List<Vector> vertices = new ArrayList<>();
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
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i) != lastAdded) {
                vectors.add(vertices.get(i));
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
