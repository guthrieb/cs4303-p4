package collisiondetection.epa;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import gameobjects.GameObject;

public class SeparatingAxis {
    public boolean separatingAxis(GameObject object1, GameObject object2) {
        Shape shape1 = object1.shape;
        Shape shape2 = object2.shape;

        double overlap = Double.MAX_VALUE;

        Vector[] edges1 = getAbsoluteEdges(shape1, object1.physicsObject.position);
        Vector[] edges2 = getAbsoluteEdges(shape2, object2.physicsObject.position);
        Vector[] axes1 = new Vector[shape1.polygon.vertexCount];
        Vector[] axes2 = new Vector[shape2.polygon.vertexCount];

        for (int j = 0; j < edges1.length; j++) {
            axes1[j] = new Vector(-edges1[j].y, edges1[j].x);
            axes1[j].normalize();
        }

        for (int j = 0; j < edges2.length; j++) {
            axes2[j] = new Vector(-edges2[j].y, edges2[j].x);
            axes2[j].normalize();
        }

        for (Vector currentAxis : axes1) {
            Projection p1 = projectOntoAxis(shape1, object1.physicsObject.position, currentAxis);
            Projection p2 = projectOntoAxis(shape2, object2.physicsObject.position, currentAxis);

            if (!p1.overlaps(p2)) {
                //No collision
                return false;
            } else {
                double tmpOverlap = p1.getOverlap(p2);
                if (tmpOverlap < overlap) {
                    overlap = tmpOverlap;
                }
            }
        }
        return true;
    }

    private Projection projectOntoAxis(Shape shape, Vector position, Vector currentAxis) {
        double minVal = Vector.dot(shape.polygon.vertices[0].addN(position), currentAxis);
        double maxVal = minVal;

        for(int i = 1; i < shape.polygon.vertexCount; i++) {
            double vertexToAxes = Vector.dot(currentAxis, shape.polygon.vertices[i].addN(position));

            if (vertexToAxes < minVal) {
                minVal = vertexToAxes;
            } else if (vertexToAxes > maxVal) {
                maxVal = vertexToAxes;
            }
        }

        return new Projection(minVal, maxVal);
    }


    private Vector[] getAbsoluteEdges(Shape shape1, Vector position) {
        Vector[] edges = new Vector[shape1.polygon.vertexCount];
        for (int i = 0; i < shape1.polygon.vertexCount; i++) {
            Vector vertex1 = shape1.polygon.vertices[i].addN(position);

            int j;
            if ((j = i + 1) >= shape1.polygon.vertexCount) j = 0;
            Vector vertex2 = shape1.polygon.vertices[j];

            Vector edge = vertex2.subtractN(vertex1);
            edge = edge.addN(position);

            edges[i] = edge;
        }

        return edges;
    }
}
