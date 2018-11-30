package collisiondetection.epa;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import drawing.Sketch;
import gameobjects.GameObject;

public class SeparatingAxis {
    private final Sketch sketch;

    public SeparatingAxis(Sketch sketch) {
        this.sketch = sketch;
    }

    public MinimumTranslationVector separatingAxis(GameObject object1, GameObject object2) {
        Shape shape1 = object1.shape;
        Shape shape2 = object2.shape;

        double overlap = Double.MAX_VALUE;
        Vector smallestAxis = null;

        Vector[] edges1 = getAbsoluteEdges(shape1, object1.physicsObject.position);
        Vector[] edges2 = getAbsoluteEdges(shape2, object2.physicsObject.position);
        Vector[] axes1 = new Vector[shape1.polygon.vertexCount];
        Vector[] axes2 = new Vector[shape2.polygon.vertexCount];

        for (int j = 0; j < edges1.length; j++) {
            axes1[j] = new Vector(-edges1[j].y, edges1[j].x);
            axes1[j].normalize();;
        }

        for (int j = 0; j < edges2.length; j++) {
            axes2[j] = new Vector(-edges2[j].y, edges2[j].x);
            axes2[j].normalize();
        }

        for (int i = 0; i < axes1.length; i++) {
            Vector currentAxis = axes1[i];
            Projection p1 = project(shape1, object1.physicsObject.position, currentAxis);
            Projection p2 = project(shape2, object2.physicsObject.position, currentAxis);

            if (!p1.overlaps(p2)) {
                //No collision
                return null;
            } else {
                double tmpOverlap = p1.getOverlap(p2);

                if (tmpOverlap < overlap) {
                    overlap = tmpOverlap;
                    smallestAxis = currentAxis;
                }
            }
        }
        return new MinimumTranslationVector(overlap, smallestAxis);
    }

    private Projection project(Shape shape, Vector position, Vector currentAxis) {
        double minVal = Vector.dot(shape.polygon.vertices[0].addN(position), currentAxis);
        double maxVal = minVal;

        for(int i = 1; i < shape.polygon.vertexCount; i++) {
            double p = Vector.dot(currentAxis, shape.polygon.vertices[i].addN(position));

            if(p < minVal) {
                minVal = p;
            } else if (p > maxVal) {
                maxVal = p;
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
