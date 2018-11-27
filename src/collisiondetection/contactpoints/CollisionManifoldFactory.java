package collisiondetection.contactpoints;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;

import java.util.List;

public class CollisionManifoldFactory {
    public static Face getSignificantFace(Shape shape1, Vector collisionNormal) {
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

    public static CollisionManifoldData getCollisionManifold(Shape shape1, Shape shape2, Vector collisionNormal) {
        System.out.println("Collision Normal: " + collisionNormal);


        Face face1 = getSignificantFace(shape1, collisionNormal);
        Face face2 = getSignificantFace(shape2, collisionNormal.negateN());

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
            flip = true;
        }

        System.out.println("Ref face: " + referenceFace);
        System.out.println("Inc face: " +otherFace);

        return performClipping(referenceFace, otherFace, flip);
    }

    private static CollisionManifoldData performClipping(Face refFace, Face otherFace, boolean flip) {
        Vector refVector = refFace.getVector();

        System.out.println("Ref vector: " + refVector);


        refVector.normalize();


        double o1 = Vector.dot(refVector, refFace.getV1());


        CollisionManifoldData clip = CollisionManifoldData.clip(otherFace.getV1(), otherFace.getV2(), refVector, o1);

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

        if (flip) {
            System.out.println("Flipping");
            refNorm.negate();
        }

        double max = Vector.dot(refNorm, refFace.getProjected());

        System.out.println("Ref normal: " + refNorm);
        System.out.println("Clipped points: " + clippedPoints);

        double depth0 = Vector.dot(refNorm, clippedPoints.get(0)) - max;
        double depth1 = Vector.dot(refNorm, clippedPoints.get(1)) - max;


        if (depth0 < 0) {
            clippedPoints.remove(0);
        }

        if (depth1 < 0) {
            clippedPoints.remove(1);
        }

        return clip;
    }
}
