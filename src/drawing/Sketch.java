package drawing;


import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import collisiondetection.shapes.VectorConstants;
import collisionresponse.PhysicsLoop;
import gameobjects.GameObject;
import playercontrols.Player;
import playercontrols.PlayerShapes;
import playercontrols.ShapeConstants;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class Sketch extends PApplet {
//    private List<GameObject> objects = new ArrayList<>();
//    private List<Shape> shapes = new ArrayList<>();
    private PhysicsLoop physicsLoop;
    private List<Player> players = new ArrayList<>();
    private static final double SCALE = 1;
//    private Shape shape1;
//    private Shape shape2;


    public static void main(String[] args) {
        PApplet.main("drawing.Sketch");
    }

    public void settings() {
        size(500, 500);
        buildShapes();

    }

    private void buildShapes() {
        physicsLoop = new PhysicsLoop(new ArrayList<>(), 10, 1 / 60.0, this);

        Shape shape2 = new Shape(VectorConstants.VECTORS_4_EDIT);
        Shape shape3 = new Shape(VectorConstants.VECTORS_3_EDIT);
        Shape playerShape = new Shape(PlayerShapes.TETHERED_SHAPE);
//        Shape playerShape = new Shape(ShapeConstants.BOOSTING_SHAPE);
        Shape shape1 = new Shape(VectorConstants.VECTORS_20);

//        physicsLoop.objects.add(new GameObject(shape2, new Vector(width / 2.0, height / 2.0), 0, 0));
//        physicsLoop.objects.add(new GameObject(shape1, new Vector(470, 194), 2132.3557, 838101.4));

        Player player = new Player(this, playerShape, new Vector(width/2.0, height/2.0), 2132, 838101.4);
//        player.physicsObject.angularVelocity+=0.5;
        double orientation = player.physicsObject.orientation;
        System.out.println(orientation);
//        player.setOrientation(3.0*Math.PI/2.0);
        physicsLoop.objects.add(player);
        players.add(player);
        physicsLoop.objects.add(new GameObject(shape3, new Vector(500, 500), 0, 0));
//        physicsLoop.objects.add(new GameObject(new Shape(shape1.polygon.copy(), 0.0), new Vector(470, 114), 2132.3557, 838101.4));

    }

    public void keyPressed() {
        if(key == 'w') {
            players.get(0).setBoosting(true);

        }
        if(key == 'd') {
            players.get(0).setRotating(Player.RotationDirection.RIGHT);

        }
        if(key == 'a') {
            players.get(0).setRotating(Player.RotationDirection.LEFT);

        }
        if(key == '1') {
            players.get(0).changeMode(Player.Mode.MOVEMENT);
        }
        if(key == '2') {
            players.get(0).changeMode(Player.Mode.TETHER);
        }
        if(key == '3') {
            players.get(0).changeMode(Player.Mode.DROPPER);
        }
    }

    public void keyReleased() {
        if(key == 'w') {
            players.get(0).setBoosting(false);
        }
        if(key == 'a' || key == 'd') {
            players.get(0).setRotating(Player.RotationDirection.NO_ROTATION);
        }
    }

    public void draw() {
;;
        background(100, 100, 100);

        for(Player player : players) {
            player.update();
        }

        for (GameObject object : physicsLoop.objects) {
            object.draw(this, SCALE);
        }
        physicsLoop.step();
    }

    public void point(Vector centerPoint) {
        point(centerPoint.x, centerPoint.y);
    }


    public void point(double x, double y) {
        strokeWeight(5);
        point((float)x, (float)y);
        strokeWeight(1);
    }

    public void line(Vector centerPoint, Vector orientationPoint) {
        line(centerPoint.x, centerPoint.y, orientationPoint.x, orientationPoint.y);
    }

    private void line(double x, double y, double x1, double y1) {
        line((float)x, (float)y, (float)x1, (float)y1);
    }
}
