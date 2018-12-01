package collisiondetection.contactpoints;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import drawing.Sketch;

import java.util.List;

public class ClippingPoints {
    private final Sketch sketch;

    public ClippingPoints(Sketch sketch) {
        this.sketch = sketch;
    }

    public Face getRelevantFace(Shape shape1, Vector collisionNormal) {
        Vector projected = shape1.support(collisionNormal);
        List<Vector> neighbours = shape1.getNeighbouringVertices(projected);
        Vector leftV = neighbours.get(0);
        Vector rightV = neighbours.get(1);

        Vector leftNorm = projected.subtractN(leftV);
        Vector rightNorm = projected.subtractN(rightV);

        leftNorm.normalize();
        rightNorm.normalize();


        if (Vector.dot(collisionNormal, rightNorm) >= Vector.dot(collisionNormal, leftNorm)) {
            return new Face(projected, leftV, projected);
        } else {
            return new Face(projected, projected, rightV);
        }
    }

    public CollisionManifoldData getCollisionManifold(Shape shape1, Shape shape2, Vector collisionNormal) {

        Face face1 = getRelevantFace(shape1, collisionNormal);
        Face face2 = getRelevantFace(shape2, collisionNormal.negateN());

        Face referenceFace;
        Face otherFace;
        boolean flip = false;

        Vector faceVector1 = face1.getVector();
        Vector faceVector2 = face2.getVector();


        if (Math.abs(Vector.dot(collisionNormal, faceVector1)) <= Math.abs(Vector.dot(collisionNormal, faceVector2))) {
            referenceFace = face1;
            otherFace = face2;
        } else {
            referenceFace = face2;
            otherFace = face1;
        }


        return performClipping(referenceFace, otherFace);
    }

    private CollisionManifoldData performClipping(Face refFace, Face incFace) {
        Vector refVector = refFace.getVector();
        refVector.normalize();

        double o1 = Vector.dot(refVector, refFace.getV1());

        CollisionManifoldData clip = CollisionManifoldData.clip(incFace.getV1(), incFace.getV2(), refVector, o1);

        List<Vector> clippedPoints = clip.points;

        if (clippedPoints.size() < 2) {
            return null;
        }

        double o2 = Vector.dot(refVector, refFace.getV2());

        clip = CollisionManifoldData.clip(clippedPoints.get(0), clippedPoints.get(1), refVector.negateN(), -o2);

        if (clip.points.size() < 2) {
            return null;
        }

        clippedPoints = clip.points;

        Vector refNorm = refVector.cross(-1);
        refNorm.normalize();

        double max = Vector.dot(refNorm, refFace.getProjected());

        double depth0 = Vector.dot(refNorm, clippedPoints.get(0)) - max;
        double depth1 = Vector.dot(refNorm, clippedPoints.get(1)) - max;


        if (depth0 < 0) {
            clippedPoints.remove(0);
        }


        if (depth1 < 0) {
            clippedPoints.remove(clippedPoints.size() - 1);

        }
        return clip;
    }
}
