package collisiondetection.shapes;



import collisionresponse.PhysicsObject;
import processing.core.PApplet;

import java.util.Arrays;
import java.util.List;

public class Shape {
    public Vector[] getVertices() {
        return polygon.vertices;
    }

    public Polygon polygon;
    double orientation;

    @Override
    public String toString() {
        return "Shape{" +
                "vertices=" + Arrays.toString(polygon.vertices) +
                '}';
    }

    public Shape(Vector... relativeVertices) {
        this.polygon = new Polygon(relativeVertices);
        this.orientation = 0;
    }

    public Shape(Polygon polygon, double orientation) {
        this.polygon = polygon;
        this.orientation = orientation;
    }

    public Vector support(Vector direction) {

        double furthestDistance = Double.NEGATIVE_INFINITY;
        Vector furthestVertex = null;

        for (int i = 0; i < polygon.vertexCount; i++) {
            Vector v = polygon.vertices[i];

            double distance = Vector.dot(v, direction);
            if (distance > furthestDistance) {
                furthestDistance = distance;
                furthestVertex = v;
            }
        }
        assert furthestVertex != null;
        return furthestVertex.copy();
    }

    public Vector centerPoint() {
        double x = 0;
        double y = 0;

        for (int i = 0; i < polygon.vertexCount; i++) {
            x += polygon.vertices[i].x;
            y += polygon.vertices[i].y;
        }

        return new Vector(x / polygon.vertexCount, y / polygon.vertexCount);
    }

    public void draw(PApplet sketch) {
        for (int i = 0; i < polygon.vertexCount; i++) {
            int j;
            if ((j = i + 1) >= polygon.vertexCount) {
                j = 0;
            }

            sketch.line((float) polygon.vertices[i].x, (float) polygon.vertices[i].y, (float) polygon.vertices[j].x, (float) polygon.vertices[j].y);
        }
    }

    public void translate(double deltaX, double deltaY) {
        for (int i = 0; i < polygon.vertexCount; i++) {
            Vector vertice = polygon.vertices[i];
            vertice.x += deltaX;
            vertice.y += deltaY;
        }
    }

    public List<Vector> getNeighbouringVertices(Vector support1) {
        int index = -1;
        for (int i = 0; i < polygon.vertexCount; i++) {
            if (polygon.vertices[i].equals(support1)) {
                index = i;
                break;
            }
        }

        int leftIndex = index - 1;
        int rightIndex = index + 1;

        if (leftIndex < 0) {
            leftIndex = polygon.vertexCount - 1;
        }

        if (rightIndex >= polygon.vertexCount) {
            rightIndex = 0;
        }
        Vector left = polygon.vertices[leftIndex];
        Vector right = polygon.vertices[rightIndex];

        return Arrays.asList(left, right);
    }

    public void translate(Vector delta) {
        translate(delta.x, delta.y);
    }

    public void rotateToOrientation(double orientation) {
        this.orientation = orientation;

        double theta = this.orientation - orientation;
        rotate(theta);
    }

    public void rotate(double theta) {
        Vector centerVector = centerPoint();
        double r00 = Math.cos(theta);
        double r10 = -Math.sin(theta);
        double r11 = Math.cos(theta);
        double r01 = Math.sin(theta);
        double[] xR = new double[]{r00, r10};
        double[] yR = new double[]{r01, r11};



        for (int i = 0; i < polygon.vertexCount; i++) {
            polygon.vertices[i].x = polygon.vertices[i].x - centerVector.x;
            polygon.vertices[i].y = polygon.vertices[i].y - centerVector.y;
        }


        matrixMultiply(centerVector, xR, yR);
    }

    private void matrixMultiply(Vector centerVector, double[] xR, double[] yR) {
        for (int i = 0; i < polygon.vertexCount; i++) {
            Vector vertex = polygon.vertices[i];
            double xPoint = vertex.x;
            double yPoint = vertex.y;

            double xMult1 = xPoint * xR[0];
            double xMult2 = yPoint * xR[1];
            double yMult1 = xPoint * yR[0];
            double yMult2 = yPoint * yR[1];


            double xRes = xMult1 + xMult2;
            double yRes = yMult1 + yMult2;

            vertex.x = (xRes + centerVector.x);
            vertex.y = (yRes + centerVector.y);
        }
    }

    public Shape translateN(Vector subtractN) {
        Shape shape = new Shape(polygon.copy(), this.orientation);
        shape.translate(subtractN);
        return shape;
    }

//    public Shape copy() {
//        Vector[] copyVertices = new Vector[polygon.vertices.length];
//        for (int i = 0; i < polygon.vertexCount; i++) {
//            copyVertices[i] = polygon.vertices[i].copy();
//        }
//        return new Shape(copyVertices);
//    }

}
