package movement;

import collisiondetection.shapes.Polygon;
import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import gameobjects.GameObject;

import java.util.List;

public class LineMath {
    double minimumDistance;


    public LineMath(double minimumDistance) {
        this.minimumDistance = minimumDistance;
    }

    public GameObject getClosestIntersectingObject(GameObject currentObject, Vector position, Vector linePoint1, Vector linePoint2, List<GameObject> objects, boolean central) {
        double closestDistance = Double.MAX_VALUE;
        GameObject closestObject = null;
        for (GameObject object : objects) {
            if (!object.equals(currentObject)) {
                Vector otherPosition = object.physicsObject.position;

                Vector distanceVector = otherPosition.subtractN(position);
                double distanceToObject = distanceVector.mag();

                if (distanceToObject < minimumDistance && distanceToObject < closestDistance) {
                    Polygon shape = object.shape.polygon.copy();

                    for (int i = 0; i < shape.vertexCount; i++) {
                        int j;
                        if ((j = i + 1) >= shape.vertexCount) j = 0;

                        Vector vertex1 = shape.vertices[i];
                        Vector vertex2 = shape.vertices[j];

                        Vector vertex1WorldSpace = vertex1.addN(otherPosition);
                        Vector vertex2WorldSpace = vertex2.addN(otherPosition);


                        boolean linesIntersect = checkIfLinesIntersect(vertex1WorldSpace, vertex2WorldSpace, linePoint1, linePoint2);
                        if (linesIntersect) {
                            closestObject = object;
                            if (!central) {
                                closestDistance = getIntersection(vertex1WorldSpace, vertex2WorldSpace, linePoint1, linePoint2).mag();
                            } else {
                                closestDistance = distanceToObject;
                            }
//                            break;
                        }
                    }
                }
            }
        }

        return closestObject;
    }

    public static boolean objectIntersected(Vector position, Vector direction, GameObject object) {
        Polygon polygon = object.shape.polygon;

        for (int i = 0; i < polygon.vertexCount; i++) {
            int j;
            if ((j = i + 1) >= polygon.vertexCount) j = 0;

            Vector vertex1 = polygon.vertices[i];
            Vector vertex2 = polygon.vertices[j];

            Vector vertex1WorldSpace = vertex1.addN(object.physicsObject.position);
            Vector vertex2WorldSpace = vertex2.addN(object.physicsObject.position);

            Vector linePoint2 = position.addN(direction.multiplyN(1000000000));
            boolean linesIntersect = checkIfLinesIntersect(vertex1WorldSpace, vertex2WorldSpace, position, linePoint2);
            if(linesIntersect) {
                return true;
            }
        }
        return false;
    }


    private static boolean checkIfLinesIntersect(Vector p1, Vector q1, Vector p2, Vector q2) {
        int orientation1 = orientation(p1, q1, p2);
        int orientation2 = orientation(p1, q1, q2);
        int orientation3 = orientation(p2, q2, p1);
        int orientation4 = orientation(p2, q2, q1);

//        System.out.println("line 1 point1: " +p1);
//        System.out.println("line 1 point2: "+ q1);
//        System.out.println("line 2 point1: " + p2);
//        System.out.println("line 2 point2: " +q2);
////        System.out.println("o1: " + orientation1);
//        System.out.println("o2: " + orientation2);
//        System.out.println("o3: " + orientation3);
//        System.out.println("o4: " + orientation4);

        if (orientation1 != orientation2 && orientation3 != orientation4) {
            return true;
        } else if (orientation1 == 0 && onSegment(p1, p2, q1)) {
            return true;
        } else if (orientation2 == 0 && onSegment(p1, q2, q1)) {
            return true;
        } else if (orientation3 == 0 && onSegment(p2, p1, q2)) {
            return true;
        } else if (orientation4 == 0 && onSegment(p2, q1, q2)) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean onSegment(Vector p, Vector q, Vector r) {
        return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);

    }

    //TODO CITE GEEKS FOR GEEKS
    private static int orientation(Vector p, Vector q, Vector r) {
        double val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) {
            return 0;
        } else if (val > 0) {
            return 1;
        } else {
            return 2;
        }
    }

    public static Vector getClosestIntersection(Vector point, Vector direction, GameObject object) {
        Polygon polygon = object.shape.polygon;

        double closestDistance = Double.MAX_VALUE;
        Vector closestPoint = null;
        for(int i = 0; i < polygon.vertexCount; i++) {
            int j;
            if((j = i + 1) >= polygon.vertexCount) {
                j = 0;
            }

            Vector vertice1 = polygon.vertices[i].addN(object.physicsObject.position);
            Vector vertice2 = polygon.vertices[j].addN(object.physicsObject.position);

            boolean intersect = checkIfLinesIntersect(vertice1, vertice2, point, point.addN(direction.multiplyN(1000000000)));

            if(intersect) {
                Vector intersection = getIntersection(point, point.addN(direction.multiplyN(100000000)), vertice1, vertice2);
                Vector pointDist = intersection.subtractN(point);
                double intersectionDistance = pointDist.mag();
                if (intersectionDistance < closestDistance) {
                    closestDistance = intersectionDistance;
                    closestPoint = intersection;
                }
            }

        }
        return closestPoint;
    }

    public static Vector getIntersection(Vector line1p1, Vector line1p2, Vector line2p1, Vector line2p2) {
        double x1 = line1p2.x - line1p1.x;
        double x2 = line2p2.x - line2p1.x;

        double m1 = (line1p2.y - line1p1.y) / x1;
        double m2 = (line2p2.y - line2p1.y) / x2;
        double b1 = (line1p1.y - m1 * line1p1.x);
        double b2 = (line2p1.y - m2 * line2p1.x);



        double xIntersect;
        double yIntersect;
        Vector refVector = null;
        if (x1 == 0) {
            //If v1 is vertical
            m1 = m2;
            b1 = b2;

            xIntersect = line1p1.x;
            refVector = line2p1;
        } else if (x2 == 0) {
            //If p2 is vertical
            xIntersect = line2p1.x;
            refVector = line1p1;
        } else {
            //If neither are vertical
            xIntersect = (b2 - b1) / (m1 - m2);
            refVector = line1p1;
        }

        if(m1 == 0) {
            yIntersect = refVector.y;
        } else {
            yIntersect = (m1 * xIntersect + b1);
        }


        return new Vector(xIntersect, yIntersect);
    }

}
