package collisiondetection.shapes;

import java.util.Arrays;

public class Polygon {

    public final int vertexCount;
    public final Vector[] vertices = new Vector[64];

    Polygon(Vector[] vertices) {
        int furthestRightPointIndex = 0;
        double highestXPoint = vertices[0].x;

        for(int i = 1; i < vertices.length; i++) {
            double x = vertices[i].x;

            if(x > highestXPoint) {
                highestXPoint = x;
                furthestRightPointIndex = i;
            } else if (x == highestXPoint
                    && vertices[furthestRightPointIndex].y > vertices[i].y) {
                furthestRightPointIndex = i;
            }
        }



        int[] hull = new int[64];
        int outCount = 0;
        int indexhull = furthestRightPointIndex;

        while(true) {
            hull[outCount]= indexhull;

            int nextHullIndex = 0;

            for(int i = 1; i < vertices.length; i++) {
                if(nextHullIndex == indexhull) {
                    nextHullIndex = i;
                } else {
                    Vector e1 = vertices[nextHullIndex].subtractN(vertices[hull[outCount]]);
                    Vector e2 = vertices[i].subtractN(vertices[hull[outCount]]);

                    double crossed = Vector.cross(e1, e2);
                    if(crossed < 0.0) {
                        nextHullIndex = i;
                    } else if (crossed == 0.0 && e2.squaredLength() > e1.squaredLength()){
                        nextHullIndex = i;
                    }
                }
            }

            outCount++;
            indexhull = nextHullIndex;

            if(nextHullIndex == furthestRightPointIndex) {
                vertexCount = outCount;
                break;
            }
        }

        for(int i = 0; i < vertexCount; i++) {
            Vector vertex = vertices[hull[i]];
            this.vertices[i] = vertex.copy();
        }

    }

    @Override
    public String toString() {
        return "Polygon{" +
                "vertices=" + Arrays.toString(vertices) +
                '}';
    }

    Polygon copy() {
        Vector[] vertices = new Vector[vertexCount];

        for(int i = 0; i < vertexCount; i++) {
            vertices[i] = this.vertices[i].copy();
        }

        return new Polygon(vertices);
    }
}
